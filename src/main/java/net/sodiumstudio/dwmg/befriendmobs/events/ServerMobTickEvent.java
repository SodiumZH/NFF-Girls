package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Event;

public class ServerMobTickEvent extends Event {

	protected Mob mob;
	public Mob getMob()
	{
		return mob;
	}
	
	public ServerMobTickEvent(Mob mob)
	{
		this.mob = mob;
	}
	
	public static class PreWorldTick extends ServerMobTickEvent
	{
		public PreWorldTick(Mob mob)
		{
			super(mob);
		}
	}
	
	public static class PostWorldTick extends ServerMobTickEvent
	{
		public PostWorldTick(Mob mob)
		{
			super(mob);
		}
	}
}
