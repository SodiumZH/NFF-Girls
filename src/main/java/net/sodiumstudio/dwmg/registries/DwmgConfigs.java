package net.sodiumstudio.dwmg.registries;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class DwmgConfigs
{
	protected static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec CONFIG;

	public static ForgeConfigSpec.DoubleValue AMBIENT_SOUND_CHANCE;
	public static ForgeConfigSpec.BooleanValue ZOMBIES_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue SKELETONS_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue GHASTS_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue ZOMBIES_NO_AMBIENT_SOUND;
	public static ForgeConfigSpec.BooleanValue SKELETONS_NO_AMBIENT_SOUND;
	public static ForgeConfigSpec.BooleanValue GHASTS_NO_AMBIENT_SOUND;

	public static ForgeConfigSpec.BooleanValue ENABLE_PROJECTILE_FRIENDLY_DAMAGE;
	public static ForgeConfigSpec.DoubleValue MAX_HEALTH_BOOST_BY_LEVEL;
	public static ForgeConfigSpec.DoubleValue MAX_ATK_BOOST_BY_LEVEL;
	
	static
	{
		BUILDER.push("sound");
		AMBIENT_SOUND_CHANCE = BUILDER.defineInRange("ambientSoundChance", 1.0d, 0.0d, 1.0d);
		ZOMBIES_USE_GIRL_SOUND = BUILDER.define("zombiesUseGirlSound", false);
		SKELETONS_USE_GIRL_SOUND = BUILDER.define("skeletonsUseGirlSound", false);
		GHASTS_USE_GIRL_SOUND = BUILDER.define("ghastsUseGirlSound", false);
		ZOMBIES_NO_AMBIENT_SOUND = BUILDER.define("zombiesNoAmbientSound", false);
		SKELETONS_NO_AMBIENT_SOUND = BUILDER.define("skeletonsNoAmbientSound", false);
		GHASTS_NO_AMBIENT_SOUND = BUILDER.define("ghastsNoAmbientSound", false);
		BUILDER.pop();
		BUILDER.push("combat");
		ENABLE_PROJECTILE_FRIENDLY_DAMAGE = BUILDER.define("enableProjectileFriendlyDamage", false);
		MAX_HEALTH_BOOST_BY_LEVEL = BUILDER.comment("Max health value boosted by level-up. Zero = no limit.").defineInRange("maxHealthBoostByLevel", 0, 0, 1e+6);
		MAX_ATK_BOOST_BY_LEVEL = BUILDER.comment("Max ATK value boosted by level-up. Zero = no limit.").defineInRange("maxAtkBoostByLevel", 0, 0, 1e+6);
		BUILDER.pop();
		
		CONFIG = BUILDER.build();
	}
	
	public static class ValueCache
	{
		public static class Sound
		{
			public static double AMBIENT_SOUND_CHANCE = 1.0d;
			public static boolean ZOMBIES_USE_GIRL_SOUND = false;
			public static boolean SKELETONS_USE_GIRL_SOUND = false;
			public static boolean GHASTS_USE_GIRL_SOUND = false;
			public static boolean ZOMBIES_NO_AMBIENT_SOUND = false;
			public static boolean SKELETONS_NO_AMBIENT_SOUND = false;
			public static boolean GHASTS_NO_AMBIENT_SOUND = false;
		}
		
		public static class Combat
		{
			public static boolean ENABLE_PROJECTILE_FRIENDLY_DAMAGE = false;
			public static double MAX_HEALTH_BOOST_BY_LEVEL = 0d;
			public static double MAX_ATK_BOOST_BY_LEVEL = 0d;
		}
		
		public static void refreshCommon()
		{	
			Sound.AMBIENT_SOUND_CHANCE = DwmgConfigs.AMBIENT_SOUND_CHANCE.get();
			Sound.ZOMBIES_USE_GIRL_SOUND = DwmgConfigs.ZOMBIES_USE_GIRL_SOUND.get();
			Sound.SKELETONS_USE_GIRL_SOUND = DwmgConfigs.SKELETONS_USE_GIRL_SOUND.get();
			Sound.GHASTS_USE_GIRL_SOUND = DwmgConfigs.GHASTS_USE_GIRL_SOUND.get();
			Sound.ZOMBIES_NO_AMBIENT_SOUND = DwmgConfigs.ZOMBIES_NO_AMBIENT_SOUND.get();
			Sound.SKELETONS_NO_AMBIENT_SOUND = DwmgConfigs.SKELETONS_NO_AMBIENT_SOUND.get();
			Sound.GHASTS_NO_AMBIENT_SOUND = DwmgConfigs.GHASTS_NO_AMBIENT_SOUND.get();
			Combat.ENABLE_PROJECTILE_FRIENDLY_DAMAGE = DwmgConfigs.ENABLE_PROJECTILE_FRIENDLY_DAMAGE.get();
			Combat.MAX_HEALTH_BOOST_BY_LEVEL = DwmgConfigs.MAX_HEALTH_BOOST_BY_LEVEL.get();
			Combat.MAX_ATK_BOOST_BY_LEVEL = DwmgConfigs.MAX_ATK_BOOST_BY_LEVEL.get();
		}
		
		/*public static void refreshServer()
		{
			Combat.ENABLE_PROJECTILE_FRIENDLY_DAMAGE = DwmgConfigs.ENABLE_PROJECTILE_FRIENDLY_DAMAGE.get();
			Combat.MAX_HEALTH_BOOST_BY_LEVEL = DwmgConfigs.MAX_HEALTH_BOOST_BY_LEVEL.get();
			Combat.MAX_ATK_BOOST_BY_LEVEL = DwmgConfigs.MAX_ATK_BOOST_BY_LEVEL.get();
		}*/
	}
	
	@SubscribeEvent
	public static void loadConfig(final ModConfigEvent event)
	{
		if (event.getConfig().getSpec() == CONFIG)
		{
			ValueCache.refreshCommon();
		}
		/*else if (event.getConfig().getSpec() == CONFIG_SERVER)
		{
			ValueCache.refreshServer();
		}*/
		
	}
}
