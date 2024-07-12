package net.sodiumstudio.dwmg.events;

import java.util.List;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.TickEvent.WorldTickEvent;
import net.minecraftforge.event.world.SleepFinishedTimeEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgLevelEvents
{
	
	@SubscribeEvent
	public static void onSleepFinished(SleepFinishedTimeEvent event)
	{
		if (event.getWorld() instanceof ServerLevel level)
		{
			for (Player player: level.players())
			{
				AABB bound = new AABB(player.position().add(-8, -8, -8), player.position().add(8, 8, 8));
				List<Entity> entities = level.getEntities(player, bound);
				for (Entity entity: entities)
				{
					if (IDwmgBefriendedMob.isBMAnd(entity, bm -> bm.getOwnerUUID().equals(player.getUUID()))
							&& entity.distanceToSqr(player) < 64f)
					{
						IDwmgBefriendedMob.getBM(entity).getFavorabilityHandler().addFavorability(2f);
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onLevelEndTick(WorldTickEvent event)
	{
	/*	if (event.phase == TickEvent.Phase.END && event.level instanceof ServerLevel sl)
		{
			for (Entity e: sl.getAllEntities())
			{
				if (e instanceof Mob mob && mob.isDeadOrDying())
					e.discard();
			}
		}*/
	}
}
