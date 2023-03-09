package com.sodium.dwmg.befriendmobs.example;

import com.sodium.dwmg.befriendmobs.BefriendMobs;
import com.sodium.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EXAMPLE_BefriendingTypeRegister {

	@SubscribeEvent
	public static void registerBefriendingType(FMLCommonSetupEvent event)
	{
		BefriendingTypeRegistry.register(
				EXAMPLE_EntityTypeRegister.EXAMPLE_BEFRIENDABLE_ZOMBIE.get(),
				EXAMPLE_EntityTypeRegister.EXAMPLE_BEFRIENDED_ZOMBIE.get(),
				new EXAMPLE_BefriendingHandlerZombie());
		return;
	}
	
	
}
