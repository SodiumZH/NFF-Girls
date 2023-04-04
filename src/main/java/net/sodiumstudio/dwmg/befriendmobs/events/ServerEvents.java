package net.sodiumstudio.dwmg.befriendmobs.events;

import java.util.HashSet;

import net.minecraft.world.entity.Entity;
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
	public static void onWorldTick(TickEvent.LevelTickEvent event)
	{
		if (event.side == LogicalSide.SERVER)
		{

			HashSet<Entity> tickedEntity = new HashSet<Entity>();

			for (Player player : event.world.players())
			{
				double radius = BefriendMobsConfigs.ENTITY_TICK_RADIUS;
				for (Entity entity : event.world.getEntities(player, new AABB(player.position().subtract(radius, radius, radius), player.position().add(radius, radius, radius))))
				{
					// Avoid entities to tick twice when around >=2 players
					if (tickedEntity.contains(entity))
						continue;
					if (!(entity instanceof Player)) 
					{
						tickedEntity.add(entity);
						MinecraftForge.EVENT_BUS.post(new EntityAroundPlayerTickEvent(entity));
					}
				}
			}
		}
	}

}