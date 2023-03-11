package net.sodiumstudio.dwmg.befriendmobs.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegCapabilities;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.registries.ModCapabilities;

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
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		if (event.side == LogicalSide.SERVER)
		{
			// Update all befriendable mobs near players
			for (Player player : event.world.players())
				for (Entity entity : event.world.getEntities(player, new AABB(player.position().subtract(64.0, 64.0, 64.0), player.position().add(64.0, 64.0, 64.0))))
					if (entity instanceof LivingEntity living)
					{	// TODO: make this an event
						living.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> { l.updateTimers(); });
						if (entity instanceof IBefriendedMob bef)
						{
							bef.onTick();
						}
					}
			//
		}
	}

}