package net.sodiumstudio.dwmg.dwmgcontent.registries;

import com.github.mechalopa.hmag.registry.ModEntityTypes;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerDrownedGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerHuskGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerSkeletonGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerZombieGirl;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBefriendingTypes {

	@SubscribeEvent
	public static void registerBefriendingType(FMLCommonSetupEvent event)
	{
		BefriendingTypeRegistry.register(
				ModEntityTypes.ZOMBIE_GIRL.get(),
				DwmgEntityTypes.BEF_ZOMBIE_GIRL.get(),
				new HandlerZombieGirl());
		BefriendingTypeRegistry.register(
				ModEntityTypes.SKELETON_GIRL.get(),
				DwmgEntityTypes.BEF_SKELETON_GIRL.get(),
				new HandlerSkeletonGirl());		
		BefriendingTypeRegistry.register(
				ModEntityTypes.HUSK_GIRL.get(),
				DwmgEntityTypes.BEF_HUSK_GIRL.get(),
				new HandlerHuskGirl());	
		BefriendingTypeRegistry.register(
				ModEntityTypes.DROWNED_GIRL.get(),
				DwmgEntityTypes.BEF_DROWNED_GIRL.get(),
				new HandlerDrownedGirl());
	}
	
	
}
