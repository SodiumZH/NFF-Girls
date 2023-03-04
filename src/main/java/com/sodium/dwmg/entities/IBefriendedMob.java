package com.sodium.dwmg.entities;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sodium.dwmg.entities.ai.BefriendedAIState;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBefriendedMob {

	
	/* Initialization */
	
	/** Initialize a mob.
	 * On reading from NBT, the befriendedFrom mob is null, so implementation must handle null cases.
	 * @param player Player who owns this mob.
	 * @param befriendedFrom The source mob from which this mob was befriended. E.g. a ZombieGirlFriendly was befriended from a ZombieGirlEntity. WARNING: Only on creating mob this value is valid. On reading from data it's null !!!
	 * @return This mob.
	 */
	public IBefriendedMob init(@Nonnull UUID playerUUID, @Nullable LivingEntity befriendedFrom);
	
	/* Ownership */
	
	// Get owner as player entity.
	// Warning: be careful calling this on initialization! If the owner hasn't been initialized it will return null.
	public Player getOwner();
	
	// Get owner as entity.
	// Warning: be careful calling this on initialization! If the owner hasn't been initialized it will return null.
	public UUID getOwnerUUID();
	
	// Set owner from player entity.
	public void setOwner(@Nonnull Player owner);
	
	// Set owner from player UUID.
	public void setOwnerUUID(@Nonnull UUID ownerUUID);
	
	/* AI configs */
	
	public BefriendedAIState getAIState();
	
	// Action when switching AI e.g. on right click/
	public BefriendedAIState switchAIState();
	
	public void setAIState(BefriendedAIState state);
	
	// Get if a target entity can be attacked by this mob.
	public boolean wantsToAttack(LivingEntity pTarget);
	
	// Get the previous target before updating target.
	// This function is only called on setting target. DO NOT CALL ANYWHERE ELSE!
	public LivingEntity getPreviousTarget();
	
	// Get the previous target after updating target.
	// This function is only called on setting target. DO NOT CALL ANYWHERE ELSE!
	public void setPreviousTarget(LivingEntity target);
	
	/* Interaction */
	
	// Actions on player right click the mob
	public boolean onInteraction(Player player, InteractionHand hand);
	
	// Action on player shift + rightmouse click
	public boolean onInteractionShift(Player player, InteractionHand hand);
	
	/* Inventory */
	
	public SimpleContainer getInventory();
	
	// Initialize inventory.
	// Should be called only in constructors.
	public void createInventory();
	
	// Set mob data from inventory.
	// Should be called only on tick.
	public void updateFromInventory();
	
	// Get bauble item stack from inventory.
	// Baubles are extra items in inventory to define some extra functions.
	// If empty, return empty; if out of index, return null.
	// If this feature is not needed, just return empty.
	public ItemStack getBauble(int index);
	
	public void setBauble(ItemStack item, int index);
}
