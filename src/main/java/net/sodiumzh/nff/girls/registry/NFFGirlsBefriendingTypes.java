package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.registry.ModEntityTypes;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagAlrauneTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagBansheeTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagCreeperGirlTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagCrimsonSlaughtererTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagCursedDollTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagEnderExecutorTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagGhastlySeekerTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagHarpyTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagHornetTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagHuskGirlTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagImpTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagJackFrostTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagJiangshiTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagKoboldTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagMeltyMonsterTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagNecroticReaperTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagNightwalkerTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagRedcapTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagSkeletonGirlTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagSlimeGirlTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagSnowCanineTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagWitherSkeletonGirlTamingProcess;
import net.sodiumzh.nff.girls.entity.tamingprocess.hmag.HmagZombieGirlTamingProcess;
import net.sodiumzh.nff.services.event.setup.NFFTamingMappingRegisterEvent;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsBefriendingTypes {

	@SubscribeEvent
	public static void registerBefriendingType(NFFTamingMappingRegisterEvent event)
	{
		
		event.register(
				ModEntityTypes.ZOMBIE_GIRL.get(),
				NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.get(),
				new HmagZombieGirlTamingProcess());
		event.register(
				ModEntityTypes.SKELETON_GIRL.get(),
				NFFGirlsEntityTypes.HMAG_SKELETON_GIRL.get(),
				new HmagSkeletonGirlTamingProcess());		
		event.register(
				ModEntityTypes.HUSK_GIRL.get(),
				NFFGirlsEntityTypes.HMAG_HUSK_GIRL.get(),
				new HmagHuskGirlTamingProcess());			
		event.register(
				ModEntityTypes.DROWNED_GIRL.get(),
				NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.get(),
				new HmagZombieGirlTamingProcess());	
		event.register(
				ModEntityTypes.CREEPER_GIRL.get(),
				NFFGirlsEntityTypes.HMAG_CREEPER_GIRL.get(),
				new HmagCreeperGirlTamingProcess());
		event.register(
				ModEntityTypes.ENDER_EXECUTOR.get(),
				NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR.get(),
				new HmagEnderExecutorTamingProcess());	
		event.register(
				ModEntityTypes.STRAY_GIRL.get(),
				NFFGirlsEntityTypes.HMAG_STRAY_GIRL.get(),
				new HmagSkeletonGirlTamingProcess());	
		event.register(
				ModEntityTypes.WITHER_SKELETON_GIRL.get(),
				NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(),
				new HmagWitherSkeletonGirlTamingProcess());
		event.register(
				ModEntityTypes.HORNET.get(),
				NFFGirlsEntityTypes.HMAG_HORNET.get(),
				new HmagHornetTamingProcess());
		event.register(
				ModEntityTypes.NECROTIC_REAPER.get(),
				NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER.get(),
				new HmagNecroticReaperTamingProcess());
		event.register(
				ModEntityTypes.GHASTLY_SEEKER.get(),
				NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER.get(),
				new HmagGhastlySeekerTamingProcess());
		event.register(
				ModEntityTypes.BANSHEE.get(),
				NFFGirlsEntityTypes.HMAG_BANSHEE.get(),
				new HmagBansheeTamingProcess());
		event.register(
				ModEntityTypes.KOBOLD.get(),
				NFFGirlsEntityTypes.HMAG_KOBOLD.get(),
				new HmagKoboldTamingProcess());
		event.register(
				ModEntityTypes.IMP.get(),
				NFFGirlsEntityTypes.HMAG_IMP.get(),
				new HmagImpTamingProcess());
		event.register(
				ModEntityTypes.HARPY.get(),
				NFFGirlsEntityTypes.HMAG_HARPY.get(),
				new HmagHarpyTamingProcess());
		event.register(
				ModEntityTypes.SNOW_CANINE.get(),
				NFFGirlsEntityTypes.HMAG_SNOW_CANINE.get(),
				new HmagSnowCanineTamingProcess());
		event.register(
				ModEntityTypes.SLIME_GIRL.get(),
				NFFGirlsEntityTypes.HMAG_SLIME_GIRL.get(),
				new HmagSlimeGirlTamingProcess());
		event.register(
				ModEntityTypes.JIANGSHI.get(),
				NFFGirlsEntityTypes.HMAG_JIANGSHI.get(),
				new HmagJiangshiTamingProcess());
		event.register(
				ModEntityTypes.DULLAHAN.get(),
				NFFGirlsEntityTypes.HMAG_DULLAHAN.get(),
				new HmagSkeletonGirlTamingProcess());
		event.register(
				ModEntityTypes.DODOMEKI.get(),
				NFFGirlsEntityTypes.HMAG_DODOMEKI.get(),
				new HmagSkeletonGirlTamingProcess());
		event.register(
				ModEntityTypes.ALRAUNE.get(),
				NFFGirlsEntityTypes.HMAG_ALRAUNE.get(),
				new HmagAlrauneTamingProcess());
		event.register(
				ModEntityTypes.GLARYAD.get(),
				NFFGirlsEntityTypes.HMAG_GLARYAD.get(),
				new HmagAlrauneTamingProcess());
		event.register(
				ModEntityTypes.CRIMSON_SLAUGHTERER.get(),
				NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER.get(),
				new HmagCrimsonSlaughtererTamingProcess());
		event.register(
				ModEntityTypes.CURSED_DOLL.get(),
				NFFGirlsEntityTypes.HMAG_CURSED_DOLL.get(),
				new HmagCursedDollTamingProcess());
		event.register(
				ModEntityTypes.REDCAP.get(),
				NFFGirlsEntityTypes.HMAG_REDCAP.get(),
				new HmagRedcapTamingProcess());
		event.register(
				ModEntityTypes.JACK_FROST.get(),
				NFFGirlsEntityTypes.HMAG_JACK_FROST.get(),
				new HmagJackFrostTamingProcess());
		event.register(
				ModEntityTypes.MELTY_MONSTER.get(),
				NFFGirlsEntityTypes.HMAG_MELTY_MONSTER.get(),
				new HmagMeltyMonsterTamingProcess());
		event.register(
				ModEntityTypes.NIGHTWALKER.get(),
				NFFGirlsEntityTypes.HMAG_NIGHTWALKER.get(),
				new HmagNightwalkerTamingProcess());
	}
}
