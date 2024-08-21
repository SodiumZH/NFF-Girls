package net.sodiumstudio.befriendmobs.bmevents.entity;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class MobBefriendedEvent extends Event
{
	public final Mob mobBefore;
	
	public final IBefriendedMob mobBefriended;
	
	public MobBefriendedEvent(Mob before, IBefriendedMob after)
	{
		this.mobBefore = before;
		this.mobBefriended = after;
	}
}
