package net.sodiumstudio.dwmg.registries;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class DwmgConfigs
{
	protected static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec CONFIG;

	// Sound
	public static ForgeConfigSpec.DoubleValue AMBIENT_SOUND_CHANCE;
	public static ForgeConfigSpec.BooleanValue ZOMBIES_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue SKELETONS_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue GHASTS_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue ZOMBIES_NO_AMBIENT_SOUND;
	public static ForgeConfigSpec.BooleanValue SKELETONS_NO_AMBIENT_SOUND;
	public static ForgeConfigSpec.BooleanValue GHASTS_NO_AMBIENT_SOUND;

	// Combat
	public static ForgeConfigSpec.BooleanValue ENABLE_PROJECTILE_FRIENDLY_DAMAGE;
	public static ForgeConfigSpec.DoubleValue MAX_HEALTH_BOOST_BY_LEVEL;
	public static ForgeConfigSpec.DoubleValue MAX_ATK_BOOST_BY_LEVEL;
	
	// Interaction
	public static ForgeConfigSpec.BooleanValue ALLOW_VANILLA_CONVERSION;
	public static ForgeConfigSpec.BooleanValue ALLOW_REVERSE_CONVERSION;
	public static ForgeConfigSpec.BooleanValue ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS;
	public static ForgeConfigSpec.BooleanValue ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES;
	public static ForgeConfigSpec.BooleanValue ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS;
	
	static
	{
		BUILDER.push("sound");
		AMBIENT_SOUND_CHANCE = BUILDER.comment("Befriended mob ambient sound frequency is multiplied by this number.")
				.defineInRange("ambientSoundChance", 1.0d, 0.0d, 1.0d);
		ZOMBIES_USE_GIRL_SOUND = BUILDER.comment("If true, befriended Zombie Girls and variants will use HMaG default girl sound.")
				.define("zombiesUseGirlSound", false);
		SKELETONS_USE_GIRL_SOUND = BUILDER.comment("If true, befriended Skeleton Girls and variants will use HMaG default girl sound.")
				.define("skeletonsUseGirlSound", false);
		GHASTS_USE_GIRL_SOUND = BUILDER.comment("If true, befriended Ghastly Seekers will use HMaG default girl sound.")
				.define("ghastsUseGirlSound", false);
		ZOMBIES_NO_AMBIENT_SOUND = BUILDER.comment("If true, befriended Zombie Girls and variants will not make ambient sound.")
				.define("zombiesNoAmbientSound", false);
		SKELETONS_NO_AMBIENT_SOUND = BUILDER.comment("If true, befriended Skeleton Girls and variants will not make ambient sound.")
				.define("skeletonsNoAmbientSound", false);
		GHASTS_NO_AMBIENT_SOUND = BUILDER.comment("If true, befriended Ghastly Seekers will not make ambient sound.")
				.define("ghastsNoAmbientSound", false);
		BUILDER.pop();
		
		BUILDER.push("combat");
		ENABLE_PROJECTILE_FRIENDLY_DAMAGE = BUILDER.comment("If true, projectiles from befriended mobs will hurt allies.")
				.define("enableProjectileFriendlyDamage", false);
		MAX_HEALTH_BOOST_BY_LEVEL = BUILDER.comment("Max health value boosted by level-up. Zero = no limit.")
				.defineInRange("maxHealthBoostByLevel", 0, 0, 1e+6);
		MAX_ATK_BOOST_BY_LEVEL = BUILDER.comment("Max ATK value boosted by level-up. Zero = no limit.")
				.defineInRange("maxAtkBoostByLevel", 0, 0, 1e+6);
		BUILDER.pop();
		
		BUILDER.push("interaction");
		ALLOW_VANILLA_CONVERSION = BUILDER.comment("Allow vanilla conversion if true, i.e. Husk -> Zombie -> Drowned; Skeleton -> Stray.")
				.define("allowVanillaConversion", true);
		ALLOW_REVERSE_CONVERSION = BUILDER.comment("Allow reverting vanilla conversion if true, i.e. Drowned -> Zombie -> Husk; Stray -> Skeleton.")
				.define("allowReverseConversion", true);
		ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS = BUILDER.comment("If true, all befriended Zombie Girls can be converted to Husk Girls with Sponge, no matter if they're converted from Husk Girls.")
				.define("allZombieGirlsCanConvertToHusks", false);
		ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES = BUILDER.comment("If true, all befriended Drowned Girls can be converted to Zombie Girls with Sponge, no matter if they're converted from Zombie Girls.")
				.define("allDrownedGirlsCanConvertToZombies", false);
		ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS = BUILDER.comment("If true, all befriended Stray Girls can be converted to Skeleton Girls with Flint and Steel, no matter if they're converted from Skeleton Girls.")
				.define("allStrayGirlsCanConvertToSkeletons", false);
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
		
		public static class Interaction
		{
			public static boolean ALLOW_VANILLA_CONVERSION;
			public static boolean ALLOW_REVERSE_CONVERSION;
			public static boolean ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS;
			public static boolean ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES;
			public static boolean ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS;
		}
		
		public static void refreshCommon()
		{	
			// Sound
			Sound.AMBIENT_SOUND_CHANCE = DwmgConfigs.AMBIENT_SOUND_CHANCE.get();
			Sound.ZOMBIES_USE_GIRL_SOUND = DwmgConfigs.ZOMBIES_USE_GIRL_SOUND.get();
			Sound.SKELETONS_USE_GIRL_SOUND = DwmgConfigs.SKELETONS_USE_GIRL_SOUND.get();
			Sound.GHASTS_USE_GIRL_SOUND = DwmgConfigs.GHASTS_USE_GIRL_SOUND.get();
			Sound.ZOMBIES_NO_AMBIENT_SOUND = DwmgConfigs.ZOMBIES_NO_AMBIENT_SOUND.get();
			Sound.SKELETONS_NO_AMBIENT_SOUND = DwmgConfigs.SKELETONS_NO_AMBIENT_SOUND.get();
			Sound.GHASTS_NO_AMBIENT_SOUND = DwmgConfigs.GHASTS_NO_AMBIENT_SOUND.get();
			// Combat
			Combat.ENABLE_PROJECTILE_FRIENDLY_DAMAGE = DwmgConfigs.ENABLE_PROJECTILE_FRIENDLY_DAMAGE.get();
			Combat.MAX_HEALTH_BOOST_BY_LEVEL = DwmgConfigs.MAX_HEALTH_BOOST_BY_LEVEL.get();
			Combat.MAX_ATK_BOOST_BY_LEVEL = DwmgConfigs.MAX_ATK_BOOST_BY_LEVEL.get();
			// Interaction
			Interaction.ALLOW_VANILLA_CONVERSION = DwmgConfigs.ALLOW_VANILLA_CONVERSION.get();
			Interaction.ALLOW_REVERSE_CONVERSION = DwmgConfigs.ALLOW_REVERSE_CONVERSION.get();
			Interaction.ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS = DwmgConfigs.ALL_ZOMBIE_GIRLS_CAN_CONVERT_TO_HUSKS.get();
			Interaction.ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES = DwmgConfigs.ALL_DROWNED_GIRLS_CAN_CONVERT_TO_ZOMBIES.get();
			Interaction.ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS = DwmgConfigs.ALL_STRAY_GIRLS_CAN_CONVERT_TO_SKELETONS.get();
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
