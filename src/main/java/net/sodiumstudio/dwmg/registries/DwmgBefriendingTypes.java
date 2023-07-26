package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.registry.ModEntityTypes;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerBanshee;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerCreeperGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerEnderExecutor;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerGhastlySeeker;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerHarpy;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerHornet;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerHuskGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerImp;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerKobold;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerNecroticReaper;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerSkeletonGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerSnowCanine;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerWitherSkeletonGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerZombieGirl;

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
		BefriendingTypeRegistry.register(
				ModEntityTypes.NECROTIC_REAPER.get(),
				DwmgEntityTypes.HMAG_NECROTIC_REAPER.get(),
				new HandlerNecroticReaper());
		BefriendingTypeRegistry.register(
				ModEntityTypes.GHASTLY_SEEKER.get(),
				DwmgEntityTypes.HMAG_GHASTLY_SEEKER.get(),
				new HandlerGhastlySeeker());
		BefriendingTypeRegistry.register(
				ModEntityTypes.BANSHEE.get(),
				DwmgEntityTypes.HMAG_BANSHEE.get(),
				new HandlerBanshee());
		BefriendingTypeRegistry.register(
				ModEntityTypes.KOBOLD.get(),
				DwmgEntityTypes.HMAG_KOBOLD.get(),
				new HandlerKobold());
		BefriendingTypeRegistry.register(
				ModEntityTypes.IMP.get(),
				DwmgEntityTypes.HMAG_IMP.get(),
				new HandlerImp());
		/*BefriendingTypeRegistry.register(
				ModEntityTypes.HARPY.get(),
				DwmgEntityTypes.HMAG_HARPY.get(),
				new HandlerHarpy());
		BefriendingTypeRegistry.register(
				ModEntityTypes.SNOW_CANINE.get(),
				DwmgEntityTypes.HMAG_SNOW_CANINE.get(),
				new HandlerSnowCanine());*/
	}
}
