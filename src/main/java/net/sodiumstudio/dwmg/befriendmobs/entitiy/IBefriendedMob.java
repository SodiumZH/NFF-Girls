package net.sodiumstudio.dwmg.befriendmobs.entitiy;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.util.Util;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;

public interface IBefriendedMob extends ContainerListener  {

	/* Initialization */
	
	/** Initialize a mob.
	 * On reading from NBT, the befriendedFrom mob is null, so implementation must handle null cases.
	 * @param player Player who owns this mob.
	 * @param from The source mob from which this mob was befriended or converted. 
	 * E.g. a BefriendedZombie was befriended from a Zombie, or spawned from a Husk by water conversion.
	 * WARNING: Only on creating mob this value is valid. On reading from data it's null !!!
	 */
	public default void init(@Nonnull UUID playerUUID, @Nullable Mob from)
	{
		this.setOwnerUUID(playerUUID);
		if (from != null)
		{
			this.asMob().setHealth(from.getHealth());
		}
		this.setInventoryFromMob();
		this.updateAttributes();
	}

	public boolean hasInit();
	
	// Call this to label a mob initialized after reading nbt, copying from other, etc.
	public void setInit();
	
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
	public default BefriendedAIState switchAIState()
	{
		BefriendedAIState nextState = getAIState().defaultSwitch();
		setAIState(nextState);
		Util.printToScreen(this.asMob().getName().getString() + " " + this.getAIState().getDisplayInfo(), getOwner(), this.asMob());
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
	
	public AdditionalInventory getAdditionalInventory();
	
	@Deprecated // Use getAdditionalInventory instead
	public default AdditionalInventory makeContainerFromInventory()
	{
		return getAdditionalInventory();
	}

	// Save container content to this mob
	@Deprecated // Simply delete it
	public default void saveInventory(AdditionalInventory container)
	{
		getAdditionalInventory().setFromContainer(container);
	}
	
	public int getInventorySize();
	
	// Set mob data from additionalInventory.
	public void updateFromInventory();
	
	// Set additionalInventory from mob data
	public void setInventoryFromMob();
	
	// Get item stack from position in inventory tag
	public default ItemStack getInventoryItemStack(int pos)
	{
		if (pos < 0 || pos >= getInventorySize())
			throw new IndexOutOfBoundsException();
		return this.getAdditionalInventory().getItem(pos);
	}
	
	// Get item (type) from position in inventory tag
	public default Item getInventoryItem(int pos)
	{
		return this.getInventoryItemStack(pos).getItem();
	}
	

	@Deprecated
	public default ItemStack getBauble(int index)
	{
		return ItemStack.EMPTY;
	}
	

	@Deprecated
	public default void setBauble(ItemStack item, int index)
	{
		return;
	}

	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container);

	
	public default Mob asMob()
	{
		return (Mob)this;
	}


	/* ContainerListener interface */
	// DO NOT override this. Override onInventoryChanged instead.
	@Override
	public default void containerChanged(Container pContainer) 
	{
		if (!(pContainer instanceof AdditionalInventory))
			throw new UnsupportedOperationException("IBefriendedMob container only receives AdditionalInventory.");
		if (hasInit())
			updateFromInventory();
		updateAttributes();
		onInventoryChanged();
	}

	public default void onInventoryChanged() 
	{
	}

	/* Misc */
	public void updateAttributes();

}
