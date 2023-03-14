package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.world.item.ItemStack;

public class ItemHelper
{
	
	public static void consumeOne(ItemStack stack)
	{
		if (stack.isEmpty())
			return;
		int amount = stack.getCount();
		stack.setCount(amount - 1);
	}
	
}
