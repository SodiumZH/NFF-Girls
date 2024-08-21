package net.sodiumstudio.befriendmobs.item.event;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;

/**
 * Posted after a Mob Catcher successfully caught a mob and created a corresponding Mob Respawner.
 * <p>Note: At this time the mob has been removed.
 */
public class MobCatchFinalizeCatchingMobEvent extends Event
{
	public final Player player;
	public final MobRespawnerInstance respawner;
	public MobCatchFinalizeCatchingMobEvent(Player player, MobRespawnerInstance respawner)
	{
		this.player = player;
		this.respawner = respawner;
	}
}
