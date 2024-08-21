package net.sodiumstudio.befriendmobs.bmevents.entity;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;

/**
 * This event is fired on a befriended mob dies if it's configured to spawn respawner.
 * <p> If canceled, it will not drop respawner in normal style.
 */
@Cancelable
public class BefriendedDropRespawnerOnDyingEvent extends Event
{

	public final IBefriendedMob dyingMob;
	public final MobRespawnerInstance respawner;
	
	public BefriendedDropRespawnerOnDyingEvent(IBefriendedMob mob, MobRespawnerInstance respawner)
	{
		this.dyingMob = mob;
		this.respawner = respawner;
	}
	
}
