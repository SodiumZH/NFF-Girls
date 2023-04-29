package net.sodiumstudio.dwmg.befriendmobs.item.baublesystem;

import java.util.HashSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;

public abstract class BaubleHandler {

	// NOT IMPLEMENTED
	//public boolean shouldPopOutIfItemNotAccepted = true;
	
	/* Item types */
	public abstract HashSet<Item> getItemsAccepted();
	
	// Check if an item can be added as a bauble
	public boolean isAccepted(Item item)
	{
		return getItemsAccepted().contains(item);
	}
	
	// Check if an item can be added as a bauble (stack version)
	public boolean isAccepted(ItemStack itemstack)
	{
		return itemstack.isEmpty() ? false : isAccepted(itemstack.getItem());
	}
	
	// Executed every tick
	// This is automatically ticked in events.EntityEvents and 
	// do not call it anywhere else unless you know what you're doing.
	public void tick(IBaubleHolder holder)
	{
		LivingEntity living = holder.getLiving();
		if (living.level != null && !living.level.isClientSide)
		{	
			preTick(holder);
			for (String key: holder.getBaubleSlots().keySet())
			{
				// Refresh slots
				if (this.shouldAlwaysRefresh(key, holder) || holder.hasSlotChanged(key))
				{
					// Not empty
					if (isAccepted(holder.getBaubleSlots().get(key).getItem()))
					{
						holder.removeBaubleModifiers(key);
						this.clearBaubleEffect(key, holder);
						refreshBaubleEffect(key, holder.getBaubleSlots().get(key), holder);
					}
					// Empty
					else
					{
						holder.removeBaubleModifiers(key);
						this.clearBaubleEffect(key, holder);
					}					
				}	
			}
			postTick(holder);
			holder.saveDataCache();
		}

	}
	
	
	// Return true if an item slot should be refreshed every tick
	public boolean shouldAlwaysRefresh(String slotKey, IBaubleHolder holder)
	{
		return false;
	}
	
	// Refresh a bauble slot.
	// Define bauble effects e.g. add modifier, change owner properties etc.
	public abstract void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleHolder owner);
	
	// Remove effects of a bauble slot. Executed when the slot is emptied.
	// Attribute modifier removal needn't to be defined here, as they will be automatically removed in tick().
	public void clearBaubleEffect(String slotKey, IBaubleHolder holder) {}
	
	// Invoked before applying bauble effects, for some additional initialization 
	public void preTick(IBaubleHolder owner) {}
	
	// Invoked after ticking bauble effects
	public void postTick(IBaubleHolder owner) {}

	/* Util */
	public static boolean isBaubleFor(ItemStack stack, IBaubleHolder holder)
	{
		return holder.getBaubleHandler().isAccepted(stack);
	}
	
	/* IBefriendedMob inventory menu util */
	
	public static boolean shouldBaubleSlotAccept(ItemStack stack, Slot slot, IBaubleHolder slotOwner)
	{
		if (slotOwner != null)
		{
			return !slot.hasItem() && isBaubleFor(stack, slotOwner);
		}
		else return false;
	}
	
	public static boolean shouldBaubleSlotAccept(ItemStack stack, Slot slot, LivingEntity slotOwner)
	{
		if (slotOwner != null && slotOwner instanceof IBaubleHolder holder)
		{
			return shouldBaubleSlotAccept(stack, slot, holder);
		}
		else return false;
	}
	
	public static boolean shouldBaubleSlotAccept(ItemStack stack, Slot slot, IBefriendedMob slotOwner)
	{
		if (slotOwner != null && slotOwner instanceof IBaubleHolder holder)
		{
			return shouldBaubleSlotAccept(stack, slot, holder);
		}
		else return false;
	}
}
