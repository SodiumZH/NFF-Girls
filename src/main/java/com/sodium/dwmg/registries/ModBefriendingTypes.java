package com.sodium.dwmg.registries;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import com.sodium.dwmg.entities.befriending.HandlerZombieGirl;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBefriendingTypes {

	@SubscribeEvent
	public static void registerBefriendingType(FMLCommonSetupEvent event)
	{
		BefriendingTypeRegistry.register(ModEntityTypes.ZOMBIE_GIRL.get(), DwmgEntityTypes.BEF_ZOMBIE_GIRL.get(), new HandlerZombieGirl());
		return;
	}
	
	
}
