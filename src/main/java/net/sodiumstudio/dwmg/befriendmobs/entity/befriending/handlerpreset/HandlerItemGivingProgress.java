package net.sodiumstudio.dwmg.befriendmobs.entity.befriending.handlerpreset;

import java.util.Random;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;


public abstract class HandlerItemGivingProgress extends HandlerItemGiving{

	protected Random rnd = new Random();

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		Mob target = args.getTarget();
		Player player = args.getPlayer();
		BefriendableMobInteractionResult result = new BefriendableMobInteractionResult();

		args.execServer((l) -> {

			if (!player.isShiftKeyDown() 
					&& (isItemAcceptable(player.getMainHandItem()) 
					&& args.isMainHand() 
					&& additionalConditions(player, target))) {
				// Block if in hatred
				if (l.isInHatred(player) && !shouldIgnoreHatred()) {
					EntityHelper.sendAngryParticlesToLivingDefault(target);
					Debug.printToScreen("Unable to befriend: in hatred list.", player, target);
					result.setHandled();

				}
				// Block if in cooldown
				else if (l.getPlayerTimer(player, "item_cooldown") > 0) {
					// EntityHelper.sendSmokeParticlesToMob(target);
					Debug.printToScreen(
							"Action cooldown " + Integer.toString(l.getPlayerTimer(player, "item_cooldown") / 20) + " s.",
							player, target);
					// result.setHandled();
				} 
				else
				{
					ItemStack mainhand = player.getMainHandItem();
					// Get amount already given
					DoubleTag currentValueTag = (DoubleTag) NbtHelper.getPlayerData(l.getPlayerDataNbt(), player,
							"proc_value");
					double procValue = currentValueTag == null ? 0 : currentValueTag.getAsDouble();
					double lastProcValue = procValue;	
					if (!player.isCreative())
						player.getMainHandItem().shrink(1);
					procValue += getProcValueToAdd(mainhand);
					Debug.printToScreen("Progress Value: " + Double.toString(procValue), player, target);
					if (procValue >= 0.99999d) {	// 1.0 actually, avoiding potential float errors
						// Satisfied
						finalActions(player, target);
						result.setHandled();
					} else {
						EntityHelper.sendGlintParticlesToLivingDefault(target);
						// Not satisfied, put data
						NbtHelper.putPlayerData(DoubleTag.valueOf(procValue), l.getPlayerDataNbt(), player,
								"proc_value");
						sendSingleHeart(target, lastProcValue, procValue, deltaProcPerHeart());
						l.setPlayerTimer(player, "item_cooldown", this.getItemGivingCooldownTicks()); // Set cooldown
						result.setHandled();
					}
				}
			}
		});

		// ...................................
		args.execClient((l) -> {
			{
				if (!l.isInHatred(player)) {
					if (!player.isShiftKeyDown() && isItemAcceptable(player.getMainHandItem().getItem())
							&& args.isMainHand() && player.hasEffect(DwmgEffects.UNDEAD_AFFINITY.get()))
						result.handled = true;
				}
			}
		});
		// ==============================
		return result;
	}

	protected abstract double getProcValueToAdd(ItemStack item);
	
	protected void sendSingleHeart(Mob target, double procBefore, double procAfter, double deltaProcPerHeart)
	{
		int times = (int)(procAfter / deltaProcPerHeart) - (int)(procBefore / deltaProcPerHeart);
		for (int i = 0; i < times; ++i)
		{
			EntityHelper.sendParticlesToEntity(target, ParticleTypes.HEART, target.getBbHeight() - 0.5, 0.2d, 1, 1d);
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
				EntityHelper.sendAngryParticlesToLivingDefault(mob);
			}
			l.removePlayerData(player, "proc_value");			
		});
	}
	
	@Override
	public boolean isInProcess(Player player, Mob mob)
	{
		CBefriendableMob l = CBefriendableMob.getCap(mob);
		return l.hasPlayerData(player, "proc_value") && l.getPlayerDataDouble(player, "proc_value") > 0d;
	}
	
}
