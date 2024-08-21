package net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset;

import java.util.Random;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NaItemUtils;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.TagHelper;
import net.sodiumstudio.nautils.debug.Debug;


public abstract class HandlerItemGivingProgress extends HandlerItemGiving{

	protected Random rnd = new Random();

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		Mob target = args.getTarget();
		Player player = args.getPlayer();
		BefriendableMobInteractionResult result = new BefriendableMobInteractionResult();

		args.execServer((l) -> {

			if (!player.isShiftKeyDown() 
					&& (isItemAcceptable(player.getMainHandItem()) || player.getMainHandItem().is(Items.DEBUG_STICK)) 
					&& args.isMainHand() 
					&& !(target.isPassenger() && this.shouldBlockOnRiding())
					&& additionalConditions(player, target)) {
				// Block if in hatred
				if (l.isInHatred(player) && !shouldIgnoreHatred()) {
					sendParticlesOnHatred(target);
					Debug.printToScreen("Hatred cooldown: " + Integer.toString(args.asCap().getHatredDuration(player) / 20) + " s."
							, player);
					result.setHandled();

				}
				// Block if in cooldown
				else if (l.getPlayerTimer(player, "item_cooldown") > 0) {
					Debug.printToScreen(
							"Action cooldown " + Integer.toString(l.getPlayerTimer(player, "item_cooldown") / 20) + " s.",
							player);
					sendParticlesOnActionCooldown(target);
					// result.setHandled();
				} 
				else
				{
					ItemStack mainhand = player.getMainHandItem();
					ItemStack givenCopy = mainhand.copy();
					boolean isDebugStick = mainhand.is(Items.DEBUG_STICK);
					// Put a zero data first, otherwise if fulfilled after giving only one item, something unexpected
					// may happen due to missing proc_value tag
					// Because this tag is also used to indicate whether the player is in process
					if (!NbtHelper.containsPlayerData(l.getPlayerDataNbt(), player, "proc_value"))
						NbtHelper.putPlayerData(DoubleTag.valueOf(0), l.getPlayerDataNbt(), player,
								"proc_value");
					// Get amount already given
					DoubleTag currentValueTag = (DoubleTag) NbtHelper.getPlayerData(l.getPlayerDataNbt(), player, "proc_value");
					double procValue = currentValueTag == null ? 0 : currentValueTag.getAsDouble();
					double lastProcValue = procValue;	
					if (isDebugStick)
					{
						procValue += 1.01;
						// Immediately update tag, otherwise unexpected error occurs due to out-of-date tag value
						// (possibly 0.0)
						NbtHelper.putPlayerData(DoubleTag.valueOf(procValue), l.getPlayerDataNbt(), player, "proc_value");
					}
					else
					{
						procValue += getProcValueToAdd(mainhand, player, target, lastProcValue);
						if (procValue <= 0)
							procValue = 0;
						if (!player.isCreative() && shouldItemConsume(player.getMainHandItem()))
							player.getMainHandItem().shrink(1);
						NaItemUtils.giveOrDrop(player, getReturnedItem(player, target, givenCopy, lastProcValue, procValue));
						if (procValue > 0)
							NbtHelper.putPlayerData(DoubleTag.valueOf(procValue), l.getPlayerDataNbt(), player, "proc_value");
						else interrupt(player, target, true);
					}
					Debug.printToScreen("Progress Value: " + Double.toString(procValue), player);
					if (procValue >= 0.9999999999d)
					{	// 1.0 actually, avoiding potential float errors
						// Satisfied
						finalActions(player, target);
						result.setHandled();
					} 
					else
					{
						// Not satisfied, put data
						sendParticlesOnItemReceived(target);
						sendProgressHeart(target, lastProcValue, procValue, deltaProcPerHeart());
						l.setPlayerTimer(player, "item_cooldown", this.getItemGivingCooldownTicks()); // Set cooldown
						this.afterItemGiven(player, target, givenCopy);
						this.onItemGiven(player, target, givenCopy, lastProcValue, procValue);
						result.setHandled();
					}
				}
			}
		});

		// ...................................
		/*args.execClient((l) -> {
			{
				if (shouldIgnoreHatred() || !l.isInHatred(player)) {
					if (!player.isShiftKeyDown() && isItemAcceptable(player.getMainHandItem())
							&& args.isMainHand())
					result.handled = true;
				}
			}
		});*/
		// ==============================
		return result;
	}
	
	protected abstract double getProcValueToAdd(ItemStack item, Player player, Mob mob, double oldProc);
	
	protected void sendProgressHeart(Mob target, double procBefore, double procAfter, double deltaProcPerHeart)
	{
		int times = (int)(procAfter / deltaProcPerHeart) - (int)(procBefore / deltaProcPerHeart);
		for (int i = 0; i < times; ++i)
		{
			sendParticlesForProgressHeart(target);
		}
	}

	protected double deltaProcPerHeart()
	{
		return 0.2d;
	}

	@Override
	public void serverTick(Mob mob)
	{
		/*mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			if (!shouldIgnoreHatred())
			{
				for (UUID uuid: l.getHatred())
				{
					if (l.hasPlayerData(mob.level.getPlayerByUUID(uuid), "proc_value")
							&& l.getPlayerDataDouble(mob.level.getPlayerByUUID(uuid), "proc_value") > 0d)
						this.interrupt(mob.level.getPlayerByUUID(uuid), mob, false);					
				}
			}
		});*/
	}
	
	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) {
		mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			if (isInProcess(player, mob) && !isQuiet)
			{
				sendParticlesOnInterrupted(mob);
			}
			l.removePlayerData(player, "proc_value");			
		});
	}
	
	@Override
	public boolean interruptAll(Mob mob, boolean isQuiet)
	{
		boolean res = super.interruptAll(mob, isQuiet);
		if (res && !isQuiet)
			sendParticlesOnInterrupted(mob);
		return res;
	}
	
	@Override
	public boolean isInProcess(Player player, Mob mob)
	{
		CBefriendableMob l = CBefriendableMob.getCap(mob);
		return NbtHelper.containsPlayerData(l.getPlayerDataNbt(), player, "proc_value")
			&& ((DoubleTag) (NbtHelper.getPlayerData(l.getPlayerDataNbt(), player, "proc_value"))).getAsDouble() > 0;
	}
	
	/**
	 * Get progress value for a player.
	 * @return Progress value, or -1 if player is not in process.
	 */
	public double getProgressValue(Mob mob, Player player)
	{
		if (!isInProcess(player, mob))
			return -1;
		else return ((DoubleTag) (NbtHelper.getPlayerData(CBefriendableMob.getCap(mob).getPlayerDataNbt(), player, "proc_value"))).getAsDouble();
	}

	/**
	 * Add a delta value to a progress value.
	 * WARNING: this method will do nothing if the player is not in process.
	 * WARNING: this method will not handle interruption or befriending even if the progress reaches 0 or 1.
	 */
	public void addProgressValue(Mob mob, Player player, double deltaValue)
	{
		double oldValue = getProgressValue(mob, player);
		NbtHelper.putPlayerData(DoubleTag.valueOf(oldValue + deltaValue), 
				CBefriendableMob.getCap(mob).getPlayerDataNbt(), player, "proc_value");
	}
	
	
	
	public void sendParticlesOnHatred(Mob target)
	{
		EntityHelper.sendAngryParticlesToLivingDefault(target);
	}
	
	public void sendParticlesOnActionCooldown(Mob target)
	{
		EntityHelper.sendSmokeParticlesToLivingDefault(target);
	}
	
	public void sendParticlesOnItemReceived(Mob target)
	{
		EntityHelper.sendGlintParticlesToLivingDefault(target);
	}

	public void sendParticlesOnInterrupted(Mob target)
	{
		EntityHelper.sendAngryParticlesToLivingDefault(target);
	}
	
	public void sendParticlesForProgressHeart(Mob target)
	{
		EntityHelper.sendParticlesToEntity(target, ParticleTypes.HEART, target.getBbHeight() - 0.5, 0.2d, 1, 1d);
	}

	/**
	 * Get the item stack that should be given to the player after giving an item.
	 * No item by default.
	 * @param itemGivenCopy ItemStack before giving.
	 * @param procBefore Progress value before giving.
	 * @param procAfter Progress value after giving.
	 */
	public ItemStack getReturnedItem(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter)
	{
		return ItemStack.EMPTY;
	}
	
	/**
	 * Invoked after an item is given and after {@link HandlerItemGiving#afterItemGiven(Player, Mob, ItemStack)}.
	 * Not executed when the condition is satisfied after giving. Handle this case in finalActions().
	 * @param itemGivenCopy ItemStack before giving.
	 * @param procBefore Progress value before giving.
	 * @param procAfter Progress value after giving.
	 */
	public void onItemGiven(Player player, Mob mob, ItemStack itemGivenCopy, double procBefore, double procAfter) {}
	
}
