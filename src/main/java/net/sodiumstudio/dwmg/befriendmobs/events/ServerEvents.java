package net.sodiumstudio.dwmg.befriendmobs.events;

import java.util.HashSet;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobsConfigs;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents 
{

	@SubscribeEvent
	public static void onServerStart(ServerStartedEvent event)
	{
		@SuppressWarnings("unused")
		int test = 1;		
	}
	

	@SubscribeEvent
	public static void onWorldTick(TickEvent.LevelTickEvent event) {
		
		if (event.side == LogicalSide.SERVER)
		{
			/**/
			ServerLevel serverlevel = (ServerLevel)(event.level);
			if (event.phase.equals(TickEvent.Phase.START))
			{
				for (Entity entity: serverlevel.getAllEntities())
				{
					MinecraftForge.EVENT_BUS.post(new ServerEntityTickEvent.PreWorldTick(entity));		
				}
			}
			else if (event.phase.equals(TickEvent.Phase.END))
			{
				for (Entity entity: serverlevel.getAllEntities())
				{
					MinecraftForge.EVENT_BUS.post(new ServerEntityTickEvent.PostWorldTick(entity));		
				}
			}
		}
	}

}