package com.sodium.dwmg.befriendmobsapi.event.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.LogicalSide;

/** This event fires on both side, but only when interaction is handled (in BefriendingHandler::handleInteract method)
 * It will NOT be fired on server when the interaction befriended a mob. A MobBefriendEvent will be posted instead.
 */
public class BefriendableMobInteractEvent extends Event {

	public LogicalSide side;
	public Player player;
	public LivingEntity target;
	public InteractionHand hand;
	public boolean isDebugBefriender = false;
	public BefriendableMobInteractEvent isDebugBefriender()
	{
		isDebugBefriender = true;
		return this;
	}
	
	public BefriendableMobInteractEvent(LogicalSide side, Player player, LivingEntity target, InteractionHand hand)
	{
		this.side = side;
		this.player = player;
		this.target = target;
		this.hand = hand;
	}
	
}
