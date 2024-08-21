package net.sodiumstudio.befriendmobs.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

@Cancelable
public class BefriendedDeathEvent extends Event
{
	protected IBefriendedMob mob;
	protected DamageSource dmgSource;
	public BefriendedDeathEvent(IBefriendedMob mob, DamageSource src)
	{
		this.mob = mob;
		dmgSource = src;
	}
	
	public IBefriendedMob getMob()
	{
		return mob;
	}
	
	public DamageSource getDamageSource()
	{
		return dmgSource;
	}
}
