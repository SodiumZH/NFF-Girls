package net.sodiumstudio.befriendmobs.item.event;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;

public class RespawnerConstructEvent extends Event
{
	protected Mob mob;
	protected MobRespawnerInstance respawner;
	//protected ItemStack stack;
	
	public Mob getMob() {return mob;};
	public MobRespawnerInstance getRespawner() {return respawner;};
	//public ItemStack getStack() {return stack;}
	
	public RespawnerConstructEvent(Mob mob, MobRespawnerInstance respawner)
	{
		this.mob = mob;
		this.respawner = respawner;
		//this.stack = stack;
	}
	
	public static class Before extends RespawnerConstructEvent
	{
		public Before(Mob mob, MobRespawnerInstance respawner)
		{
			super(mob, respawner);
		}
	}
	
	public static class After extends RespawnerConstructEvent
	{
		
		public After(Mob mob, MobRespawnerInstance respawner)
		{
			super(mob, respawner);
		}
	}
}
