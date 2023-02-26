package com.sodium.dwmg.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.registries.ModCapabilities;
import com.sodium.dwmg.registries.ModEffects;
import com.sodium.dwmg.util.Debug;


@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModServerEventHandler 
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
						living.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> { l.updateTimers(); });
			//
		}
	}

}