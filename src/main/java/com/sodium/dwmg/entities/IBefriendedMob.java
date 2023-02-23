package com.sodium.dwmg.entities;

import java.util.UUID;

import javax.annotation.Nonnull;

import com.sodium.dwmg.entities.befriending.AIState;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface IBefriendedMob {

	/** Initialize a mob.
	 * 
	 * @param player Player who owns this mob.
	 * @param befriendedFrom The source mob from which this mob was befriended. E.g. a ZombieGirlFriendly was befriended from a ZombieGirlEntity.
	 * @return This mob.
	 */
	public IBefriendedMob init(@Nonnull Player player, LivingEntity befriendedFrom);
	
	public Player getOwner();
	
	public UUID getOwnerUUID();
	
	public IBefriendedMob setOwner(@Nonnull Player owner);
	
	// Actions on player right click the mob
	boolean onInteraction(Player player);
	
	// Action on player shift+rightmouse click
	boolean onInteractionShift(Player player);
	
}
