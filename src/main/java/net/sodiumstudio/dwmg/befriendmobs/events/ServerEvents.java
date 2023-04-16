package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents 
{

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event) {
		
		if (event.side == LogicalSide.SERVER)
		{
			
			ServerLevel serverlevel = (ServerLevel)(event.world);
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