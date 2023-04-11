package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.Event;

public class ServerEntityTickEvent extends Event {

	protected Entity entity;
	public Entity getEntity()
	{
		return entity;
	}
	
	public ServerEntityTickEvent(Entity entity)
	{
		this.entity = entity;
	}
	
	public static class PreWorldTick extends ServerEntityTickEvent
	{
		public PreWorldTick(Entity entity)
		{
			super(entity);
		}
	}
	
	public static class PostWorldTick extends ServerEntityTickEvent
	{
		public PostWorldTick(Entity entity)
		{
			super(entity);
		}
	}
}
