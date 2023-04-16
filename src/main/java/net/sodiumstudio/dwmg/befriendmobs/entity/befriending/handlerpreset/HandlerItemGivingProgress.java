package net.sodiumstudio.dwmg.befriendmobs.entity.befriending.handlerpreset;

import java.util.Random;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;


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
					&& additionalConditions(player, target)) {
				// Block if in hatred
				if (l.isInHatred(player) && !shouldIgnoreHatred()) {
					sendParticlesOnHatred(target);
					Debug.printToScreen("Unable to befriend: in hatred list.", player, target);
					result.setHandled();

				}
				// Block if in cooldown
				else if (l.getPlayerTimer(player, "item_cooldown") > 0) {
					Debug.printToScreen(
							"Action cooldown " + Integer.toString(l.getPlayerTimer(player, "item_cooldown") / 20) + " s.",
							player, target);
					sendParticlesOnActionCooldown(target);
					// result.setHandled();
				} 
				else
				{
					ItemStack mainhand = player.getMainHandItem();
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
						procValue += getProcValueToAdd(mainhand);
						if (!player.isCreative())
							player.getMainHandItem().shrink(1);
						NbtHelper.putPlayerData(DoubleTag.valueOf(procValue), l.getPlayerDataNbt(), player, "proc_value");
					}
					Debug.printToScreen("Progress Value: " + Double.toString(procValue), player, target);
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
						result.setHandled();
					}
				}
			}
		});

		// ...................................
		args.execClient((l) -> {
			{
				if (shouldIgnoreHatred() || !l.isInHatred(player)) {
					if (!player.isShiftKeyDown() && isItemAcceptable(player.getMainHandItem().getItem())
							&& args.isMainHand())
					result.handled = true;
				}
			}
		});
		// ==============================
		return result;
	}

	protected abstract double getProcValueToAdd(ItemStack item);
	
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
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			if (!shouldIgnoreHatred())
			{
				for (UUID uuid: l.getHatred())
				{
					if (l.hasPlayerData(mob.level.getPlayerByUUID(uuid), "proc_value")
							&& l.getPlayerDataDouble(mob.level.getPlayerByUUID(uuid), "proc_value") > 0d)
						this.interrupt(mob.level.getPlayerByUUID(uuid), mob, false);					
				}
			}
		});
	}
	
	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) {
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
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

}
