package com.sodium.dwmg.entities.befriending;

import java.util.Random;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.registries.ModItems;
import com.sodium.dwmg.util.Debug;
import com.sodium.dwmg.util.NbtHelper;
import com.sodium.dwmg.util.Util;

import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

//@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BefriendableMobInteractHandler {

	//@SubscribeEvent
	public boolean handleBefriendableMobInteract(BefriendableMobInteractArguments args)
	{
		Util.GlobalBoolean handled = new Util.GlobalBoolean(false);
		Player player = args.getPlayer();
		LivingEntity target = args.getTarget();
		EntityType<?> type = target.getType();
		BefriendingHandler method = Dwmg.befriendingHandlerGetter.method;
		
		args.execServer((l) -> {
			
			if (!l.isInHatred(args.getPlayer()))
			{
				
				if (type == ModEntityTypes.ZOMBIE_GIRL.get())
				{
					if (!player.isShiftKeyDown() && args.getPlayer().getMainHandItem().getItem() == ModItems.SOUL_CAKE_SLICE.get())
					{
						// Get overall cake amount needed, or create if not existing
						IntTag overallAmountTag = (IntTag)NbtHelper.getPlayerData(l.getPlayerData(), args.getPlayer(), "overall_amount");
						int overallAmount;
						if (overallAmountTag == null)
						{
							float rnd = new Random().nextFloat();
							overallAmount = rnd < 0.1 ? 1 : (rnd < 0.4 ? 2 : 3);	// 10% for 1, 30% for 2, 60% for 3
							NbtHelper.putPlayerData(IntTag.valueOf(overallAmount), l.getPlayerData(), args.getPlayer(), "overall_amount");
						}
						else
							overallAmount = overallAmountTag.getAsInt();
						// Get amount already given
						IntTag alreadyGivenTag = (IntTag)NbtHelper.getPlayerData(l.getPlayerData(), args.getPlayer(), "already_given");
						int alreadyGiven = alreadyGivenTag == null ? 0 : alreadyGivenTag.getAsInt();
						// Give cake
						args.getPlayer().getMainHandItem().shrink(1);
						alreadyGiven ++;
						Debug.printToScreen("Cakes given: " + Integer.toString(alreadyGiven) + " / " + Integer.toString(overallAmount), player, target);
						if (alreadyGiven == overallAmount)
						{
							// Satisfied
							method.befriend(player, target);
							handled.set(true);
						}
						else
						{
							// Not satisfied, put data
							NbtHelper.putPlayerData(IntTag.valueOf(alreadyGiven), l.getPlayerData(), player, "already_given");
							handled.set(true);
						}
					}
				}
				
			}	
		});
		return handled.get();
	}
	

	
}
