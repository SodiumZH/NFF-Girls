package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgCapabilities;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEvents 
{

	@SubscribeEvent
	public static void onServerStart(ServerStartedEvent event)
	{
		@SuppressWarnings("unused")
		int test = 1;
		
	}
	

	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void onWorldTick(TickEvent.LevelTickEvent event)
	{
		if (event.side == LogicalSide.SERVER)
		{
			// Update all befriendable mobs near players
			for (Player player : event.level.players())
			{
				for (Entity entity : event.level.getEntities(player, new AABB(player.position().subtract(64.0, 64.0, 64.0), player.position().add(64.0, 64.0, 64.0))))
				{
					if (entity instanceof Mob mob)
					{	// TODO: make this an event
						if (!(mob instanceof IBefriendedMob))
						{
							mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
							{
								l.updateTimers(); 
								BefriendingTypeRegistry.getHandler((EntityType<Mob>)(mob.getType())).serverTick(mob);
							});
						}
					}
					if (entity instanceof LivingEntity living)
					{
						living.getCapability(BefMobCapabilities.CAP_HEALING_HANDLER).ifPresent((l) -> 
						{
							l.updateCooldown();
						});
					}
				}
			}
		}
	}

}