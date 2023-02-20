package com.sodium.dwmg.entities;

import javax.annotation.Nonnull;

import com.sodium.dwmg.entities.befriending.AIState;

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
	
	@Nonnull
	public Player getOwner();
	
	public IBefriendedMob setOwner(@Nonnull Player owner);

	public boolean setAiState(AIState to, AIState from);
		
	// Actions on player right click the mob
	void onRightClicked(Player player);
	
	// Action on player shift+rightmouse click
	void onShiftRightClicked(Player player);
	
}
