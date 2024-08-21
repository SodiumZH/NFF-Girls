package net.sodiumstudio.befriendmobs.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entity.capability.CBMPlayerModule;
import net.sodiumstudio.befriendmobs.registry.BMCaps;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMServerEvents 
{

	@SubscribeEvent
	public static void onLevelTick(TickEvent.LevelTickEvent event) {

		
		if (event.side == LogicalSide.SERVER)
		{
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
				serverlevel.getCapability(BMCaps.CAP_BM_LEVEL).ifPresent(cap -> {cap.tick();});
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLevelTickFinalize(TickEvent.LevelTickEvent event)
	{
		if (event.phase == TickEvent.Phase.END && event.side == LogicalSide.SERVER)
		{
			for (Player player: event.level.players())
			{
				CBMPlayerModule.removeOneTickTags(player);
			}
		}
	}
	
}