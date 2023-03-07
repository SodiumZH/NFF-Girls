package com.sodium.dwmg.registries;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.befriendmobsapi.entitiy.befriending.registry.BefriendingTypeMapRegisterEvent;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModBefriending {

	@SubscribeEvent
	public static void registerBefriendingMap(BefriendingTypeMapRegisterEvent event)
	{
		
	}
	
	
}
