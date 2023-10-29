package net.sodiumstudio.dwmg.registries;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class DwmgConfigs
{
	protected static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec CONFIG;
	
	public static ForgeConfigSpec.BooleanValue ENABLE_PROJECTILE_FRIENDLY_DAMAGE;

	public static ForgeConfigSpec.DoubleValue AMBIENT_SOUND_CHANCE;
	public static ForgeConfigSpec.BooleanValue ZOMBIES_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue SKELETONS_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue GHASTS_USE_GIRL_SOUND;
	public static ForgeConfigSpec.BooleanValue ZOMBIES_NO_AMBIENT_SOUND;
	public static ForgeConfigSpec.BooleanValue SKELETONS_NO_AMBIENT_SOUND;
	public static ForgeConfigSpec.BooleanValue GHASTS_NO_AMBIENT_SOUND;
	
	static
	{
		BUILDER.push("sound");
		
		ENABLE_PROJECTILE_FRIENDLY_DAMAGE = BUILDER.define("enableProjectileFriendlyDamage", false);
		
		AMBIENT_SOUND_CHANCE = BUILDER.defineInRange("ambientSoundChance", 1.0d, 0.0d, 1.0d);
		ZOMBIES_USE_GIRL_SOUND = BUILDER.define("zombiesUseGirlSound", false);
		SKELETONS_USE_GIRL_SOUND = BUILDER.define("skeletonsUseGirlSound", false);
		GHASTS_USE_GIRL_SOUND = BUILDER.define("ghastsUseGirlSound", false);
		ZOMBIES_NO_AMBIENT_SOUND = BUILDER.define("zombiesNoAmbientSound", false);
		SKELETONS_NO_AMBIENT_SOUND = BUILDER.define("skeletonsNoAmbientSound", false);
		GHASTS_NO_AMBIENT_SOUND = BUILDER.define("ghastsNoAmbientSound", false);
		
		
		
		BUILDER.pop();
		
		CONFIG = BUILDER.build();
	}
	
	public static class ValueCache
	{
		
		public static boolean ENABLE_PROJECTILE_FRIENDLY_DAMAGE = false;
		public static double AMBIENT_SOUND_CHANCE = 1.0d;
		public static boolean ZOMBIES_USE_GIRL_SOUND = false;
		public static boolean SKELETONS_USE_GIRL_SOUND = false;
		public static boolean GHASTS_USE_GIRL_SOUND = false;
		public static boolean ZOMBIES_NO_AMBIENT_SOUND = false;
		public static boolean SKELETONS_NO_AMBIENT_SOUND = false;
		public static boolean GHASTS_NO_AMBIENT_SOUND = false;
		
		public static void refresh()
		{
			ENABLE_PROJECTILE_FRIENDLY_DAMAGE = DwmgConfigs.ENABLE_PROJECTILE_FRIENDLY_DAMAGE.get();
			
			AMBIENT_SOUND_CHANCE = DwmgConfigs.AMBIENT_SOUND_CHANCE.get();
			ZOMBIES_USE_GIRL_SOUND = DwmgConfigs.ZOMBIES_USE_GIRL_SOUND.get();
			SKELETONS_USE_GIRL_SOUND = DwmgConfigs.SKELETONS_USE_GIRL_SOUND.get();
			GHASTS_USE_GIRL_SOUND = DwmgConfigs.GHASTS_USE_GIRL_SOUND.get();
			ZOMBIES_NO_AMBIENT_SOUND = DwmgConfigs.ZOMBIES_NO_AMBIENT_SOUND.get();
			SKELETONS_NO_AMBIENT_SOUND = DwmgConfigs.SKELETONS_NO_AMBIENT_SOUND.get();
			GHASTS_NO_AMBIENT_SOUND = DwmgConfigs.GHASTS_NO_AMBIENT_SOUND.get();
		}
	}
	
	@SubscribeEvent
	public static void loadConfig(final ModConfigEvent event)
	{
		if (event.getConfig().getSpec() == CONFIG)
		{
			ValueCache.refresh();
		}
	}
}
