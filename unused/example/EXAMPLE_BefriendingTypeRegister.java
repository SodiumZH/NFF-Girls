package net.sodiumstudio.befriendmobs.example;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;

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
