package net.sodiumstudio.befriendmobs.bmevents;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.befriendmobs.bmevents.entity.BefriendedDropRespawnerOnDyingEvent;
import net.sodiumstudio.befriendmobs.bmevents.entity.MobBefriendedEvent;
import net.sodiumstudio.befriendmobs.bmevents.level.BMLevelModuleTickEndEvent;
import net.sodiumstudio.befriendmobs.bmevents.level.BMLevelModuleTickStartEvent;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;

public class BMHooks
{

	/**
	 * Hooks related to befriending processes
	 */
	public static class Befriending
	{
		public static void onMobBefriended(Mob mobBefore, IBefriendedMob befriended)
		{
			MinecraftForge.EVENT_BUS.post(new MobBefriendedEvent(mobBefore, befriended));
		}
	}
	
	public static class Befriended
	{
		public static boolean onBefriendedGenerateRespawnerOnDying(IBefriendedMob mob, MobRespawnerInstance respawner)
		{
			return MinecraftForge.EVENT_BUS.post(new BefriendedDropRespawnerOnDyingEvent(mob, respawner));
		}
	}
	
	
	// Hooks during actions of the level
	public static class Level
	{
		public static void onModuleTickStart(ServerLevel level)
		{
			MinecraftForge.EVENT_BUS.post(new BMLevelModuleTickStartEvent(level));
		}
		
		public static void onModuleTickEnd(ServerLevel level)
		{
			MinecraftForge.EVENT_BUS.post(new BMLevelModuleTickEndEvent(level));
		}
	}
	
}
