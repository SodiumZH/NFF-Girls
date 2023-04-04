package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Event;

public class EntityAroundPlayerTickEvent extends Event {

	protected Entity entity;
	public Entity getEntity()
	{
		return entity;
	}
	
	public EntityAroundPlayerTickEvent(Entity entity)
	{
		this.entity = entity;
	}
	
	
}
