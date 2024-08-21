package net.sodiumstudio.befriendmobs.entity.befriending;

import java.util.HashSet;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.befriendmobs.bmevents.BMHooks;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.debug.Debug;
import net.sodiumstudio.nautils.math.RandomSelection;
import net.sodiumstudio.nautils.math.RndUtil;

public abstract class BefriendingHandler 
{
	public BefriendingHandler()
	{	
	}

	public void initCap(CBefriendableMob cap)
	{
	}	
	
	/** If this method is overridden, it should invalidate the input target living mob.
	 */
	public IBefriendedMob befriend(Player player, Mob target)
	{
		// Don't execute on client
		if (target.level.isClientSide())
			return null;
		// Don't execute on mobs already befriended
		if (target instanceof IBefriendedMob)
			return null;		

		// Check if befriendable capability is attached
		if(!target.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new RuntimeException("Befriending: Target living mob not having CBefriendableMob capability attached.");
		
		// Get new type, and do check
		@SuppressWarnings("unchecked")
		EntityType<? extends Mob> newType = BefriendingTypeRegistry.getConvertTo((EntityType<? extends Mob>) target.getType());
		if (newType == null)
			throw new RuntimeException("Befriending: Entity type after befriending is not valid. Check if the befriendable mob has been registered to BefriendingTypeRegistry.");
		
		// Record the old mob's some properties for initializing new mob on client UNIMPLEMENTED
		//ClientboundBefriendingInitPacket packet = new ClientboundBefriendingInitPacket(target);
		
		// Do conversion
		Mob newMob = EntityHelper.replaceMob(newType, target);
		if(!(newMob instanceof IBefriendedMob))
			throw new RuntimeException("Befriending: Entity type after befriending not implementing IBefriendedMob interface.");
		IBefriendedMob bm = (IBefriendedMob)newMob;
		bm.setOwner(player);
		bm.getData().setOwnerName(player.getName().getString());
		bm.init(player.getUUID(), target);
		bm.setInventoryFromMob();
		bm.getData().generateIdentifier();
		bm.getData().recordEntityType();
		bm.getData().recordEncounteredDate();
		//Debug.printToScreen("Mob \""+target.getDisplayName().getString()+"\" befriended", player);
		BMHooks.Befriending.onMobBefriended(target, bm);
		bm.setInit();
		// Sync the recorded properties UNIMPLEMENTED
		//NaNetworkUtils.sendToAllPlayers(newBefMob.asMob().level, BMChannels.BM_CHANNEL, packet);
		return bm;
	}
	
	public abstract BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args);
	
	/**
	 * Invoked on mob tick in server.
	 * <p> For custom tick actions, override {@code serverTick} instead.
	 */
	public final void serverTickInternal(Mob mob)
	{
		if (persistantIfInProcess())
		{
			mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent(c -> 
			{
				c.setForcePersistent(isInProcess(mob));
			});
		}
		serverTick(mob);
	}

	/**
	 * Invoked on mob tick in server.
	 */
	public void serverTick(Mob mob)
	{}
	
	public abstract void interrupt(Player player, Mob mob, boolean isQuiet);
	
	// interrupt all
	// This will not send any particles no matter isQuiet or not.
	// If particles are needed, override this function.
	// @return if any process is interrupted.
	public boolean interruptAll(Mob mob, boolean isQuiet)
	{
		boolean res = false;
		for (Player player: mob.level.players())
		{
			if (player != null && isInProcess(player, mob))
			{
				interrupt(player, mob, true);
				res = true;
			}
		}
		return res;
	}
	
	public boolean dontInterruptOnPlayerDie()
	{
		return false;
	}
	
	/** Indicates if the player is in befriending process of the mob. */
	public abstract boolean isInProcess(Player player, Mob mob);
	
	/** Indicates if any player is in befriending process. */
	public boolean isInProcess(Mob mob)
	{
		if (mob.level.isClientSide)
			return false;
		for (Player player: mob.level.players())
		{
			if (isInProcess(player, mob))
				return true;
		}
		return false;
	}
	
	/** Execute when the mob attacks the player in befriending process with it
	 * Fired in BMEntityEvents and no need to manually invoke
	 * This function doesn't check if player is in hatred
	 * No action by default
	 *
	 * @param damageGiven Whether the attack gave any real damage.
	 */
	public void onAttackProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
	}
	
	/** Execute when the mob is attacked by the player in befriending process with it
	* Fired in BMEntityEvents and no need to manually invoke
	* This function doesn't check if player is in hatred
	* By default no action. Usually it will interrupt because of hatred
	* */
	public void onAttackedByProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
	}
	
	/** Execute when the mob added player into hatred list
	* Fired in CBefriendableMob::addHatredWithReason and no need to manually invoke
	* Interrupt if attacked by default
	* */
	public void onAddingHatred(Mob mob, Player player, BefriendableAddHatredReason reason)
	{
		if (isInProcess(player, mob) && reason == BefriendableAddHatredReason.ATTACKED)
			interrupt(player, mob, false);
	}
	
	// Get reasons for adding hatred in mob
	// If reasons are not in this list, no adding hatred
	public abstract HashSet<BefriendableAddHatredReason> getAddHatredReasons();
	
	// Duration of hatred added
	// -1 means permanent
	public int getHatredDurationTicks(BefriendableAddHatredReason reason)
	{
		return 300 * 20;
	}
	
	/**
	 * If true, the mob will not despawn if any player in the level is in process with it.
	 */
	public boolean persistantIfInProcess()
	{
		return true;
	}
	
	/**
	 * Action when related mob fired a timer-up event, posted after the timer-up Forge event.
	 * @param mob	Related mob.
	 * @param timerKey	String key of the timer.
	 * @param player	Related player if it's a player-specified timer. Null if it isn't.
	 */
	public void onBefriendableMobTimerUp(Mob mob, String timerKey, @Nullable Player player) {}
	
	
	/* Util */
	/**
	 * Do an action for all players in process.
	 * @deprecated use consumer version instead
	 */
	@Deprecated
	public void forAllPlayersInProcess(Mob mob, BiConsumer<Player, Mob> todo)
	{
		for (Player player: mob.level.players())
		{
			if (isInProcess(player, mob))
				todo.accept(player, mob);
		}
	}

	/**
	 * Do an action for all players in process.
	 */
	public void forAllPlayersInProcess(Mob mob, Consumer<Player> todo)
	{
		for (Player player: mob.level.players())
		{
			if (isInProcess(player, mob))
				todo.accept(player);
		}
	}

	// Utilities

	public static final double rndDouble(double min, double max)
	{
		return RndUtil.rndRangedDouble(min, max);
	}
	
	public static final float rndFloat(float min, float max)
	{
		return RndUtil.rndRangedFloat(min, max);
	}
	
	public static final Supplier<Double> rndDoubleSupplier(double min, double max)
	{
		return () -> rndDouble(min, max);
	}
	
	public static final Supplier<Float> rndFloatSupplier(float min, float max)
	{
		return () -> rndFloat(min, max);
	}
	
	public static final <T> T getFromProbabilityTable(Map<T, Double> probabilityTable, T defaultValue)
	{
		RandomSelection<T> rs = new RandomSelection<>(defaultValue);
		for (T t: probabilityTable.keySet())
		{
			rs.add(t, probabilityTable.get(t));
		}
		return rs.select();
	}

}
