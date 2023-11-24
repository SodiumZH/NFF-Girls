package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.registry.ModEntityTypes;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.sodiumstudio.befriendmobs.bmevents.setup.RegisterBefriendingTypeEvent;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerAlraune;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerBanshee;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerCreeperGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerCrimsonSlaughterer;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerCursedDoll;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerEnderExecutor;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerGhastlySeeker;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerGlaryad;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerHarpy;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerHornet;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerHuskGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerImp;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerJackFrost;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerJiangshi;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerKobold;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerMeltyMonster;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerNecroticReaper;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerRedcap;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerSkeletonGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerSnowCanine;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerWitherSkeletonGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerZombieGirl;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerSlimeGirl;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBefriendingTypes {

	@SubscribeEvent
	public static void registerBefriendingType(RegisterBefriendingTypeEvent event)
	{
		
		event.register(
				ModEntityTypes.ZOMBIE_GIRL.get(),
				DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get(),
				new HandlerZombieGirl());
		event.register(
				ModEntityTypes.SKELETON_GIRL.get(),
				DwmgEntityTypes.HMAG_SKELETON_GIRL.get(),
				new HandlerSkeletonGirl());		
		event.register(
				ModEntityTypes.HUSK_GIRL.get(),
				DwmgEntityTypes.HMAG_HUSK_GIRL.get(),
				new HandlerHuskGirl());			
		event.register(
				ModEntityTypes.DROWNED_GIRL.get(),
				DwmgEntityTypes.HMAG_DROWNED_GIRL.get(),
				new HandlerZombieGirl());	
		event.register(
				ModEntityTypes.CREEPER_GIRL.get(),
				DwmgEntityTypes.HMAG_CREEPER_GIRL.get(),
				new HandlerCreeperGirl());
		event.register(
				ModEntityTypes.ENDER_EXECUTOR.get(),
				DwmgEntityTypes.HMAG_ENDER_EXECUTOR.get(),
				new HandlerEnderExecutor());	
		event.register(
				ModEntityTypes.STRAY_GIRL.get(),
				DwmgEntityTypes.HMAG_STRAY_GIRL.get(),
				new HandlerSkeletonGirl());	
		event.register(
				ModEntityTypes.WITHER_SKELETON_GIRL.get(),
				DwmgEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(),
				new HandlerWitherSkeletonGirl());
		event.register(
				ModEntityTypes.HORNET.get(),
				DwmgEntityTypes.HMAG_HORNET.get(),
				new HandlerHornet());
		event.register(
				ModEntityTypes.NECROTIC_REAPER.get(),
				DwmgEntityTypes.HMAG_NECROTIC_REAPER.get(),
				new HandlerNecroticReaper());
		event.register(
				ModEntityTypes.GHASTLY_SEEKER.get(),
				DwmgEntityTypes.HMAG_GHASTLY_SEEKER.get(),
				new HandlerGhastlySeeker());
		event.register(
				ModEntityTypes.BANSHEE.get(),
				DwmgEntityTypes.HMAG_BANSHEE.get(),
				new HandlerBanshee());
		event.register(
				ModEntityTypes.KOBOLD.get(),
				DwmgEntityTypes.HMAG_KOBOLD.get(),
				new HandlerKobold());
		event.register(
				ModEntityTypes.IMP.get(),
				DwmgEntityTypes.HMAG_IMP.get(),
				new HandlerImp());
		event.register(
				ModEntityTypes.HARPY.get(),
				DwmgEntityTypes.HMAG_HARPY.get(),
				new HandlerHarpy());
		event.register(
				ModEntityTypes.SNOW_CANINE.get(),
				DwmgEntityTypes.HMAG_SNOW_CANINE.get(),
				new HandlerSnowCanine());
		event.register(
				ModEntityTypes.SLIME_GIRL.get(),
				DwmgEntityTypes.HMAG_SLIME_GIRL.get(),
				new HandlerSlimeGirl());
		event.register(
				ModEntityTypes.JIANGSHI.get(),
				DwmgEntityTypes.HMAG_JIANGSHI.get(),
				new HandlerJiangshi());
		event.register(
				ModEntityTypes.DULLAHAN.get(),
				DwmgEntityTypes.HMAG_DULLAHAN.get(),
				new HandlerSkeletonGirl());
		event.register(
				ModEntityTypes.DODOMEKI.get(),
				DwmgEntityTypes.HMAG_DODOMEKI.get(),
				new HandlerSkeletonGirl());
		event.register(
				ModEntityTypes.ALRAUNE.get(),
				DwmgEntityTypes.HMAG_ALRAUNE.get(),
				new HandlerAlraune());
		event.register(
				ModEntityTypes.GLARYAD.get(),
				DwmgEntityTypes.HMAG_GLARYAD.get(),
				new HandlerAlraune());
		event.register(
				ModEntityTypes.CRIMSON_SLAUGHTERER.get(),
				DwmgEntityTypes.HMAG_CRIMSON_SLAUGHTERER.get(),
				new HandlerCrimsonSlaughterer());
		event.register(
				ModEntityTypes.CURSED_DOLL.get(),
				DwmgEntityTypes.HMAG_CURSED_DOLL.get(),
				new HandlerCursedDoll());
		event.register(
				ModEntityTypes.REDCAP.get(),
				DwmgEntityTypes.HMAG_REDCAP.get(),
				new HandlerRedcap());
		event.register(
				ModEntityTypes.JACK_FROST.get(),
				DwmgEntityTypes.HMAG_JACK_FROST.get(),
				new HandlerJackFrost());
		event.register(
				ModEntityTypes.MELTY_MONSTER.get(),
				DwmgEntityTypes.HMAG_MELTY_MONSTER.get(),
				new HandlerMeltyMonster());
	}
}
