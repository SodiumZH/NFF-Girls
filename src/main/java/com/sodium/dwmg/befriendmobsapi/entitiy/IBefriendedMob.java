package com.sodium.dwmg.befriendmobsapi.entitiy;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sodium.dwmg.befriendmobsapi.client.gui.screens.AbstractGuiBefriended;
import com.sodium.dwmg.befriendmobsapi.entitiy.ai.BefriendedAIState;
import com.sodium.dwmg.befriendmobsapi.inventory.AbstractInventoryMenuBefriended;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public interface IBefriendedMob extends ContainerListener  {

	
	/* Initialization */
	
	/** Initialize a mob.
	 * On reading from NBT, the befriendedFrom mob is null, so implementation must handle null cases.
	 * @param player Player who owns this mob.
	 * @param befriendedFrom The source mob from which this mob was befriended. E.g. a ZombieGirlFriendly was befriended from a ZombieGirlEntity. WARNING: Only on creating mob this value is valid. On reading from data it's null !!!
	 */
	public default void init(@Nonnull UUID playerUUID, @Nullable LivingEntity befriendedFrom)
	{
		BefriendedHelper.initDefault(this, playerUUID, befriendedFrom);
	}
	
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
	
	/* Tick */ 
	// This will be automatically called on world tick. DO NOT CALL ANYWHERE ELSE!!!!
	// DO NOT call this in aiStep() or any other functions executed on tick!!!
	public default void onTick() {
		updateFromInventory();
	};
	
	/* AI configs */
	
	public BefriendedAIState getAIState();
	
	// Action when switching AI e.g. on right click/
	public default BefriendedAIState switchAIState()
	{
		BefriendedAIState nextState = getAIState().defaultSwitch();
		setAIState(nextState);
		return nextState;
	}
	
	public void setAIState(BefriendedAIState state);
	
	// Get if a target entity can be attacked by this mob.
	public default boolean wantsToAttack(LivingEntity pTarget)
	{
		return BefriendedHelper.wantsToAttackDefault(this, pTarget);
	}
	
	// Get the previous target before updating target.
	// This function is only called on setting target. DO NOT CALL ANYWHERE ELSE!
	public LivingEntity getPreviousTarget();
	
	// Get the previous target after updating target.
	// This function is only called on setting target. DO NOT CALL ANYWHERE ELSE!
	public void setPreviousTarget(LivingEntity target);
	
	/* Interaction */
	
	// Actions on player right click the mob
	// Automatically called on entity interaction. DO NOT call this in mobInteract()!!
	public boolean onInteraction(Player player, InteractionHand hand);
	
	// Actions on player shift + rightmouse click
	// Automatically called on entity interaction. DO NOT call this in mobInteract()!!
	public boolean onInteractionShift(Player player, InteractionHand hand);
	
	/* Inventory */
	
	public SimpleContainer getInventory();
	
	public void setInventory(SimpleContainer container);
	
	public int getInventorySize();
	
	// Initialize inventory.
	// Should be called only in the constructor.
	public default void createInventory()
	{
		SimpleContainer simplecontainer = this.getInventory();
		this.setInventory(new SimpleContainer(getInventorySize()));
		if (simplecontainer != null) {
			simplecontainer.removeListener(this);
			int i = Math.min(simplecontainer.getContainerSize(), this.getInventory().getContainerSize());

			for (int j = 0; j < i; ++j) {
				ItemStack itemstack = simplecontainer.getItem(j);
				if (!itemstack.isEmpty()) {
					this.getInventory().setItem(j, itemstack.copy());
				}
			}
		}
		this.getInventory().addListener(this);
		this.updateFromInventory();
	}
	
	// Set mob data from inventory.
	// Should be called only on tick.
	public void updateFromInventory();
	
	// Get bauble item stack from inventory.
	// Baubles are extra items in inventory to define some extra functions.
	// If empty, return empty; if out of index, return null.
	// If bauble feature is not needed, just don't override.
	public default ItemStack getBauble(int index)
	{
		return ItemStack.EMPTY;
	}
	
	// Set bauble item stack to inventory.
	// If bauble feature is not needed, just don't override.
	public default void setBauble(ItemStack item, int index)
	{
		return;
	}

	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container);
	
	/* GUI related */
	public AbstractGuiBefriended makeGui(AbstractInventoryMenuBefriended menu, Inventory playerInventory, Component title);
	
	@Override
	public default void containerChanged(Container pContainer) 
	{
		updateFromInventory();
	}
}
