package net.sodiumstudio.dwmg.befriendmobs.events;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.capability.CBefriendableMob;

public class BefriendableTimerUpEvent extends Event
{
	protected Mob mob;
	protected CBefriendableMob cap;
	protected String key;
	protected Player player;
	
	public BefriendableTimerUpEvent(CBefriendableMob mobCap, String key, @Nullable Player player)
	{
		cap = mobCap;
		mob = mobCap.getOwner();
		this.key = key;
		this.player = player;
	}
	
	public Mob getMob()
	{
		return mob;
	}
	
	public CBefriendableMob getCapability()
	{
		return cap;
	}
	
	public String getKey()
	{
		return key;
	}
	
	@Nullable
	public Player getPlayer()
	{
		return player;
	}
}
