package net.sodiumstudio.dwmg.befriendmobs.item.baublesystem;

import java.util.HashSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public abstract class BaubleHandler {

	
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
	public void updateBaubleEffects(IBaubleHolder owner)
	{
		LivingEntity living = owner.getliving();
		if (living.level != null && !living.level.isClientSide)
		{	
			owner.clearBaubleModifiers();
			preUpdate();
			for (ItemStack stack: owner.getBaubleStacks())
			{
				applyBaubleEffect(stack, owner);
			}
		}
	}
	
	// Define bauble effects e.g. add modifier, change owner properties etc.
	// This will be invoked every tick on each bauble slot
	public abstract void applyBaubleEffect(ItemStack bauble, IBaubleHolder owner);
	
	// Invoked before applying bauble effects to do some additional initialization 
	// Tip: before invoking this, the old additional modifiers are already cleared.
	public void preUpdate() {}

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
