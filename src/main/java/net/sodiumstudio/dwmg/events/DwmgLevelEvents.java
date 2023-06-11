package net.sodiumstudio.dwmg.events;

import java.util.List;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.level.SleepFinishedTimeEvent;
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
		if (event.getLevel() instanceof ServerLevel level)
		{
			for (Player player: level.players())
			{
				AABB bound = new AABB(player.position().add(-8, -8, -8), player.position().add(8, 8, 8));
				List<Entity> entities = level.getEntities(player, bound);
				for (Entity entity: entities)
				{
					if (entity instanceof IDwmgBefriendedMob bm
							&& bm.getModId().equals(Dwmg.MOD_ID)
							&& bm.getOwnerUUID().equals(player.getUUID())
							&& entity.distanceToSqr(player) < 64f)
					{
						bm.getFavorability().addFavorability(2f);
					}
				}
			}
		}
	}
	

}
