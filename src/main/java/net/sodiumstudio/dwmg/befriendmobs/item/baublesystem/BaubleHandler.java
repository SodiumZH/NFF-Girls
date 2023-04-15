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
	public void updateBaubleEffects(IBaubleHolder owner)
	{
		LivingEntity living = owner.getLiving();
		if (living.level != null && !living.level.isClientSide)
		{	
			owner.clearBaubleModifiers();
			preUpdate(owner);
			for (ItemStack stack: owner.getBaubleStacks())
			{
				if (isAccepted(stack.getItem()))
					applyBaubleEffect(stack, owner);
				/*else if (shouldPopOutIfItemNotAccepted)
				{
					owner.getLiving().spawnAtLocation(stack.copy());
					stack = ItemStack.EMPTY;
				}*/
			}
			postUpdate(owner);
		}
	}
	
	// Define bauble effects e.g. add modifier, change owner properties etc.
	// This will be invoked every tick on each bauble slot
	public abstract void applyBaubleEffect(ItemStack bauble, IBaubleHolder owner);
	
	// Invoked before applying bauble effects, for some additional initialization 
	// Tip: before invoking this, the old additional modifiers are already cleared.
	public void preUpdate(IBaubleHolder owner) {}
	
	// Invoked after applying bauble effects
	public void postUpdate(IBaubleHolder owner) {}

	/* Util */
	public static boolean isBaubleFor(ItemStack stack, IBaubleHolder holder)
	{
		return holder.getBaubleHandler().isAccepted(stack);
	}
	
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
