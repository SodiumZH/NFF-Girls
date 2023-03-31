package net.sodiumstudio.dwmg.befriendmobs.entity.befriending.handlerpreset;

import java.util.UUID;

import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;

/**
 * @author SodiumZH
 * Do item giving process which requires to give a certain count of items.
 * In this preset item-giving process handler, mob receives a given count of items, and
 * after the count demand is satisfied, it does the final actions (customizable, befriend by default)
 * 
 * Related NBT tags:
 * (Player Data) overall_amount: the demanded count. It will not change for one player but varies for different players.
 * (Player Data) already_given: the items player already given. 
 * (Player Timer) item_cooldown: cooldown ticks before the next item can be given.
 */

public abstract class HandlerItemGivingCount extends HandlerItemGiving
{

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		Mob target = args.getTarget();
		Player player = args.getPlayer();
		BefriendableMobInteractionResult result = new BefriendableMobInteractionResult();

		args.execServer((l) ->
		{
			if (!player.isShiftKeyDown() 
					&& isItemAcceptable(player.getMainHandItem()) 
					&& args.isMainHand()
					&& additionalConditions(player, target))
			{
				// Block if in hatred
				if (l.isInHatred(player) && !shouldIgnoreHatred())
				{
					sendParticlesOnHatred(target);
					Debug.printToScreen("Unable to befriend: in hatred list.", player, target);
					result.setHandled();

				}
				// Block if in cooldown
				else if (l.getPlayerTimer(player, "item_cooldown") > 0)
				{
					Debug.printToScreen(
							"Action cooldown " + Integer.toString(l.getPlayerTimer(player, "item_cooldown") / 20) + " s.",
							player, target);
					sendParticlesOnActionCooldown(target);
				} else
				{
					// Get overall cake amount needed, or create if not existing
					IntTag overallAmountTag = (IntTag) NbtHelper.getPlayerData(l.getPlayerDataNbt(), player,
							"overall_amount");
					int overallAmount;
					if (overallAmountTag == null)
					{
						overallAmount = getRequiredAmount(); 						
						NbtHelper.putPlayerData(IntTag.valueOf(overallAmount), l.getPlayerDataNbt(), player,
								"overall_amount");
					} else
						overallAmount = overallAmountTag.getAsInt();
					// Get amount already given
					IntTag alreadyGivenTag = (IntTag) NbtHelper.getPlayerData(l.getPlayerDataNbt(), player,
							"already_given");
					int alreadyGiven = alreadyGivenTag == null ? 0 : alreadyGivenTag.getAsInt();
					// Give cake
					if (!player.isCreative())
						player.getMainHandItem().shrink(1);
					alreadyGiven++;
					MiscUtil.printToScreen(
							"Item(s) given: " + Integer.toString(alreadyGiven) + " / " + Integer.toString(overallAmount),
							player, target);
					if (alreadyGiven == overallAmount)
					{
						// Satisfied
						finalActions(player, target);
						result.setHandled();
					} else
					{
						sendParticlesOnItemReceived(target);
						// Not satisfied, put data
						NbtHelper.putPlayerData(IntTag.valueOf(alreadyGiven), l.getPlayerDataNbt(), player,
								"already_given");
						l.setPlayerTimer(player, "item_cooldown", this.getItemGivingCooldownTicks()); // Set 10s cooldown
						result.setHandled();
					}
				}
			}
		});

		// ...................................
		args.execClient((l) ->
		{
			{
				if (l.isInHatred(player) && !shouldIgnoreHatred())
				{
					if (!player.isShiftKeyDown() 
							&& isItemAcceptable(player.getMainHandItem()) 
							&& args.isMainHand()
							&& additionalConditions(player, target))
						result.handled = true;
				} else
				{
				}
			}
		});
		// ==============================
		return result;
	}

	public abstract int getRequiredAmount(); 
	
	@Override
	public void serverTick(Mob mob)
	{
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			if (!shouldIgnoreHatred())
			{
				for (UUID uuid: l.getHatred())
				{
					if (isInProcess(mob.level.getPlayerByUUID(uuid), mob))
						this.interrupt(mob.level.getPlayerByUUID(uuid), mob, false);					
				}
			}
		});
	}
	
	@Override
	public void interrupt(Player player, Mob mob, boolean isQuiet) {
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
		{
			if (l.hasPlayerData(player, "already_given") 
					&& l.getPlayerDataInt(player, "already_given") > 0
					&& !isQuiet)
			{
				sendParticlesOnInterrupted(mob);
			}
			l.removePlayerData(player, "already_given");
		});
	}
	
	@Override
	public boolean isInProcess(Player player, Mob mob)
	{
		CBefriendableMob l = CBefriendableMob.getCap(mob);
		return l.hasPlayerData(player, "already_given") && l.getPlayerDataInt(player, "already_given") > 0;
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
	
}

