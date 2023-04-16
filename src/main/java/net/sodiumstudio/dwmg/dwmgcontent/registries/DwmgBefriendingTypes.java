package net.sodiumstudio.dwmg.dwmgcontent.registries;

import com.github.mechalopa.hmag.registry.ModEntityTypes;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerCreeperGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerEnderExecutor;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerHornet;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerHuskGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerSkeletonGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerWitherSkeletonGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag.HandlerZombieGirl;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBefriendingTypes {

	@SubscribeEvent
	public static void registerBefriendingType(FMLCommonSetupEvent event)
	{
		
		BefriendingTypeRegistry.register(
				ModEntityTypes.ZOMBIE_GIRL.get(),
				DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get(),
				new HandlerZombieGirl());
		BefriendingTypeRegistry.register(
				ModEntityTypes.SKELETON_GIRL.get(),
				DwmgEntityTypes.HMAG_SKELETON_GIRL.get(),
				new HandlerSkeletonGirl());		
		BefriendingTypeRegistry.register(
				ModEntityTypes.HUSK_GIRL.get(),
				DwmgEntityTypes.HMAG_HUSK_GIRL.get(),
				new HandlerHuskGirl());			
		BefriendingTypeRegistry.register(
				ModEntityTypes.DROWNED_GIRL.get(),
				DwmgEntityTypes.HMAG_DROWNED_GIRL.get(),
				new HandlerZombieGirl());	
		BefriendingTypeRegistry.register(
				ModEntityTypes.CREEPER_GIRL.get(),
				DwmgEntityTypes.HMAG_CREEPER_GIRL.get(),
				new HandlerCreeperGirl());
		BefriendingTypeRegistry.register(
				ModEntityTypes.ENDER_EXECUTOR.get(),
				DwmgEntityTypes.HMAG_ENDER_EXECUTOR.get(),
				new HandlerEnderExecutor());	
		BefriendingTypeRegistry.register(
				ModEntityTypes.STRAY_GIRL.get(),
				DwmgEntityTypes.HMAG_STRAY_GIRL.get(),
				new HandlerSkeletonGirl());	
		BefriendingTypeRegistry.register(
				ModEntityTypes.WITHER_SKELETON_GIRL.get(),
				DwmgEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(),
				new HandlerWitherSkeletonGirl());
		BefriendingTypeRegistry.register(
				ModEntityTypes.HORNET.get(),
				DwmgEntityTypes.HMAG_HORNET.get(),
				new HandlerHornet());
	}
	
}
