package net.sodiumstudio.befriendmobs.bmevents.entity;

import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.befriended.CBefriendedMobData;

/**
 * Posted on {@link CBefriendedMobData} construct (initialize) after adding common data.
 */
public class BefriendedMobDataConstructEvent extends Event
{
	private final CBefriendedMobData data;
	
	public BefriendedMobDataConstructEvent(CBefriendedMobData data)
	{
		this.data = data;
	}

	public CBefriendedMobData getData() {
		return data;
	}
}
