package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import java.util.Random;

import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class HandlerZombieGirl extends AbstractBefriendingHandler
{

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) {
		Mob target = args.getTarget();
		Player player = args.getPlayer();
		BefriendableMobInteractionResult result = new BefriendableMobInteractionResult();

		args.execServer((l) ->
		{

			if (!player.isShiftKeyDown() && player.getMainHandItem().is(DwmgItems.SOUL_CAKE_SLICE.get()) && args.isMainHand()
					&& player.hasEffect(DwmgEffects.DEATH_AFFINITY.get()))
			{
				// Block if in hatred
				if (l.isInHatred(player))
				{
					EntityHelper.sendAngryParticlesToLivingDefault(target);
					Debug.printToScreen("Unable to befriend: in hatred list.", player, target);
					result.setHandled();

				}
				// Block if in cooldown
				else if (l.getTimerPS(player, "cake_cooldown") > 0)
				{
					// EntityHelper.sendSmokeParticlesToMob(target);
					Debug.printToScreen(
							"Action cooldown " + Integer.toString(l.getTimerPS(player, "cake_cooldown") / 20) + " s.",
							player, target);
					// result.setHandled();
				} else
				{
					// Get overall cake amount needed, or create if not existing
					IntTag overallAmountTag = (IntTag) NbtHelper.getPlayerData(l.getPlayerData(), player,
							"overall_amount");
					int overallAmount;
					if (overallAmountTag == null)
					{
						float rnd = new Random().nextFloat();
						// 5% for 1, 15% for 2, 80% for 3
						overallAmount = 
								rnd < 0.05 ? 1 : (
								rnd < 0.2 ? 2 : 3
										); 						
						NbtHelper.putPlayerData(IntTag.valueOf(overallAmount), l.getPlayerData(), player,
								"overall_amount");
					} else
						overallAmount = overallAmountTag.getAsInt();
					// Get amount already given
					IntTag alreadyGivenTag = (IntTag) NbtHelper.getPlayerData(l.getPlayerData(), player,
							"already_given");
					int alreadyGiven = alreadyGivenTag == null ? 0 : alreadyGivenTag.getAsInt();
					// Give cake
					if (!player.isCreative())
						player.getMainHandItem().shrink(1);
					alreadyGiven++;
					Debug.printToScreen(
							"Cakes given: " + Integer.toString(alreadyGiven) + " / " + Integer.toString(overallAmount),
							player, target);
					if (alreadyGiven == overallAmount)
					{
						// Satisfied
						EntityHelper.sendHeartParticlesToLivingDefault(target);
						result.befriendedMob = befriend(player, target);
						result.setHandled();
					} else
					{
						EntityHelper.sendGreenStarParticlesToLivingDefault(target);
						// Not satisfied, put data
						NbtHelper.putPlayerData(IntTag.valueOf(alreadyGiven), l.getPlayerData(), player,
								"already_given");
						l.setTimerPS(player, "cake_cooldown", 200); // Set 10s cooldown
						result.setHandled();
					}
				}
			}

		});

		// ...................................
		args.execClient((l) ->
		{
			{
				if (!l.isInHatred(player))
				{
					if (!player.isShiftKeyDown() && player.getMainHandItem().is(DwmgItems.SOUL_CAKE_SLICE.get()) && args.isMainHand()
							&& player.hasEffect(DwmgEffects.DEATH_AFFINITY.get()))
						result.handled = true;
				} else
				{
				}
			}
		});
		// ==============================
		return result;
	}
	
}
