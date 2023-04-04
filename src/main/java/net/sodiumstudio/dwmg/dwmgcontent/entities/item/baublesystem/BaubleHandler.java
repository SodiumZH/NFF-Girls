package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import java.util.HashSet;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public abstract class BaubleHandler {

	public abstract HashSet<Item> getItemsAccepted();
	
	public boolean isAccepted(Item item)
	{
		return getItemsAccepted().contains(item);
	}
	
	public boolean isAccepted(ItemStack itemstack)
	{
		return itemstack.isEmpty() ? false : isAccepted(itemstack.getItem());
	}
	
	public abstract void updateBaubleEffects(IBaubleHolder owner);
	
	public int getHoldingCount(IBaubleHolder owner, Item item)
	{
		int res = 0;
		for (ItemStack stack: owner.getBaubleStacks())
		{
			if (stack.is(item))
				res++;
		}
		return res;
	}
	
}
