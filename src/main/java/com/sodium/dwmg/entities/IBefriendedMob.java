package com.sodium.dwmg.entities;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sodium.dwmg.entities.ai.BefriendedAIState;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public interface IBefriendedMob {

	/** Initialize a mob.
	 * On reading from NBT, the befriendedFrom mob is null, so implementation must handle null cases.
	 * 
	 * @param player Player who owns this mob.
	 * @param befriendedFrom The source mob from which this mob was befriended. E.g. a ZombieGirlFriendly was befriended from a ZombieGirlEntity.
	 * @return This mob.
	 */
	public IBefriendedMob init(@Nonnull Player player, @Nullable LivingEntity befriendedFrom);
	
	public Player getOwner();
	
	public UUID getOwnerUUID();
	
	public void setOwner(@Nonnull Player owner);
	
	public void setOwnerUUID(@Nonnull UUID ownerUUID);
	
	public BefriendedAIState getAIState();
	
	public BefriendedAIState switchAIState();
	
	public void setAIState(BefriendedAIState state);
	
	public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner);
	
	// Actions on player right click the mob
	boolean onInteraction(Player player);
	
	// Action on player shift + rightmouse click
	boolean onInteractionShift(Player player);
	
	
	
}
