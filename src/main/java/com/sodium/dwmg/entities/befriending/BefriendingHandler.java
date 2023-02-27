package com.sodium.dwmg.entities.befriending;

import java.util.Random;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.registries.ModCapabilities;
import com.sodium.dwmg.registries.ModEffects;
import com.sodium.dwmg.registries.ModEntityTypes;
import com.sodium.dwmg.registries.ModItems;
import com.sodium.dwmg.util.Debug;
import com.sodium.dwmg.util.EntityHelper;
import com.sodium.dwmg.util.NbtHelper;

import net.minecraft.nbt.IntTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.fml.LogicalSide;

public class BefriendingHandler 
{
	public BefriendingHandler()
	{	
	}

	public EntityType<?> getTypeAfterBefriending(EntityType<?> type)
	{
		if (type == com.github.mechalopa.hmag.registry.ModEntityTypes.ZOMBIE_GIRL.get())
			return ModEntityTypes.ZOMBIE_GIRL_FRIENDLY.get();
		// Add more types here using else if
		
		else return null;
	}
	
	public IBefriendedMob befriend(Player player, LivingEntity target)
	{
		// Don't execute on client
		if (target.level.isClientSide())
			return null;
		// Don't execute on mobs already befriended
		if (target instanceof IBefriendedMob)
			return null;		

		if(!target.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new RuntimeException("Befriending: Target living entity not having CapBefriendableMob capability attached.");
		EntityType<?> newType = getTypeAfterBefriending(target.getType());
		if(newType == null)
			throw new RuntimeException("Befriending: Entity type after befriending is not valid. Check BefriendingMethod.getTypeAfterBefriending function.");
		Entity resultRaw = EntityHelper.replaceLivingEntity(newType, target);
		if(!(resultRaw instanceof LivingEntity))
			throw new RuntimeException("Befriending: Entity type after befriending is not a living entity. Check BefriendingMethod.getTypeAfterBefriending function.");
		if(!(resultRaw instanceof IBefriendedMob))
			throw new RuntimeException("Befriending: Entity type after befriending not implementing IBefriendedMob interface.");
		IBefriendedMob result = (IBefriendedMob)resultRaw;
		result.init(player, target);		
		Debug.printToScreen("Mob "+target.toString()+" befriended", player, target);
		return result;
	}

	public BefriendableMobInteractionResult onBefriendableMobInteract(BefriendableMobInteractArguments args)
	{
		LivingEntity target = (LivingEntity)args.getTarget();
		EntityType<?> type = target.getType();
		Player player = args.getPlayer();
		BefriendableMobInteractionResult result = new BefriendableMobInteractionResult();
		
			// ==============================
			if (type == com.github.mechalopa.hmag.registry.ModEntityTypes.ZOMBIE_GIRL.get())
			{
				args.execServer((l) -> {
			
						if (!player.isShiftKeyDown() && player.getMainHandItem().getItem() == ModItems.SOUL_CAKE_SLICE.get() && player.hasEffect(ModEffects.DEATH_AFFINITY.get()))
						{
							// Block if in hatred
							if (l.isInHatred(player))
							{
								EntityHelper.sendAngryParticlesToMob(target);
								Debug.printToScreen("Unable to befriend: in hatred list.", player, target);
								result.setHandled();
								
							}
							// Block if in cooldown 
							else if (l.getTimerPS(player, "cake_cooldown") > 0)
							{
								//EntityHelper.sendSmokeParticlesToMob(target);
								Debug.printToScreen("Action cooldown " + Integer.toString(l.getTimerPS(player, "cake_cooldown") / 20) + " s.", player, target);
								// result.setHandled();
							}
							else
							{
								// Get overall cake amount needed, or create if not existing
								IntTag overallAmountTag = (IntTag)NbtHelper.getPlayerData(l.getPlayerData(), player, "overall_amount");
								int overallAmount;
								if (overallAmountTag == null)
								{
									float rnd = new Random().nextFloat();
									overallAmount = rnd < 0.1 ? 1 : (rnd < 0.4 ? 2 : 3);	// 10% for 1, 30% for 2, 60% for 3
									NbtHelper.putPlayerData(IntTag.valueOf(overallAmount), l.getPlayerData(), player, "overall_amount");
								}
								else
									overallAmount = overallAmountTag.getAsInt();
								// Get amount already given
								IntTag alreadyGivenTag = (IntTag)NbtHelper.getPlayerData(l.getPlayerData(), player, "already_given");
								int alreadyGiven = alreadyGivenTag == null ? 0 : alreadyGivenTag.getAsInt();
								// Give cake
								if (!player.isCreative())
									player.getMainHandItem().shrink(1);
								alreadyGiven ++;
								Debug.printToScreen("Cakes given: " + Integer.toString(alreadyGiven) + " / " + Integer.toString(overallAmount), player, target);
								if (alreadyGiven == overallAmount)
								{
									// Satisfied
									EntityHelper.sendHeartParticlesToMob(target);
									result.befriendedMob = befriend(player, target);
									result.setHandled();
								}
								else
								{
									EntityHelper.sendStarParticlesToMob(target);
									// Not satisfied, put data
									NbtHelper.putPlayerData(IntTag.valueOf(alreadyGiven), l.getPlayerData(), player, "already_given");
									l.setTimerPS(player, "cake_cooldown", 200);	// Set 10s cooldown
									result.setHandled();
								}
							}
						}
					
				});	
			}
			//...................................
				args.execClient((l) -> 
				{
					{
						if (!l.isInHatred(player))
						{
							if (!player.isShiftKeyDown() && player.getMainHandItem().getItem() == ModItems.SOUL_CAKE_SLICE.get() && player.hasEffect(ModEffects.DEATH_AFFINITY.get()))
								result.handled = true;
						}
						else
						{
						}
					}
				});
			// ==============================
		return result;
	}
	
}
