package net.sodiumstudio.dwmg.befriendmobs.entity.befriending;

import java.util.HashSet;
import java.util.function.BiConsumer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;

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
		if(!target.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new RuntimeException("Befriending: Target living mob not having CBefriendableMob capability attached.");
		
		// Get new type, and do check
		@SuppressWarnings("unchecked")
		EntityType<? extends Mob> newType = BefriendingTypeRegistry.getConvertTo((EntityType<? extends Mob>) target.getType());
		if (newType == null)
			throw new RuntimeException("Befriending: Entity type after befriending is not valid. Check if the befriendable mob has been registered to BefriendingTypeRegistry.");
		Mob newMob = EntityHelper.replaceMob(newType, target);
		if(!(newMob instanceof IBefriendedMob))
			throw new RuntimeException("Befriending: Entity type after befriending not implementing IBefriendedMob interface.");
		IBefriendedMob newBefMob = (IBefriendedMob)newMob;
		newBefMob.init(player.getUUID(), target);
		newBefMob.setInventoryFromMob();
		Debug.printToScreen("Mob \""+target.getDisplayName().getString()+"\" befriended", player);
		newBefMob.setInit();
		return newBefMob;
	}
	
	public abstract BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args);
	
	public void serverTick(Mob mob){}
	
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
	
	// Indicates if the player is in befriending process of the mob.
	public abstract boolean isInProcess(Player player, Mob mob);
	
	// Indecated if any player is in befriending process.
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
	 * Fired in EntityEvents and no need to manually invoke
	 * This function doesn't check if player is in hatred
	 * No action by default
	 *
	 * @param damageGiven Whether the attack gave any real damage.
	 */
	public void onAttackProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
	}
	
	// Execute when the mob is attacked by the player in befriending process with it
	// Fired in EntityEvents and no need to manually invoke
	// This function doesn't check if player is in hatred
	// By default no action. Usually it will interrupt because of hatred
	public void onAttackedByProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
	}
	
	// Execute when the mob added player into hatred list
	// Fired in CBefriendableMob::addHatredWithReason and no need to manually invoke
	// Interrupt by default
	public void onAddingHatred(Mob mob, Player player, BefriendableAddHatredReason reason)
	{
		if (isInProcess(player, mob))
			interrupt(player, mob, false);
	}
	
	// Get reasons for adding hatred in mob
	// If reasons are not in this list, no adding hatred
	public abstract HashSet<BefriendableAddHatredReason> getAddHatredReasons();
	
	// Duration of hatred added
	// -1 means permanent
	public int getHatredDurationTicks(BefriendableAddHatredReason reason)
	{
		return 18000;
	}
	
	/* Util */
	public void forAllPlayersInProcess(Mob mob, BiConsumer<Player, Mob> todo)
	{
		for (Player player: mob.level.players())
		{
			if (isInProcess(player, mob))
				todo.accept(player, mob);
		}
	}


}
