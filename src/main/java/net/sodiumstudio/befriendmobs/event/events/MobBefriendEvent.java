package net.sodiumstudio.befriendmobs.event.events;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entitiy.IBefriendedMob;

// This event only fires on server.
public class MobBefriendEvent extends Event {

	private Player player;
	private LivingEntity fromMob;
	private IBefriendedMob newMob;
	
	public MobBefriendEvent(Player player, LivingEntity from, IBefriendedMob to)
	{
		this.player = player;
		this.fromMob = from;
		this.newMob = to;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	// Warning: this mob has been invalidated!
	public LivingEntity getMobBefore()
	{
		return fromMob;
	}
	
	public IBefriendedMob getModAfter()
	{
		return newMob;
	}
	
}
