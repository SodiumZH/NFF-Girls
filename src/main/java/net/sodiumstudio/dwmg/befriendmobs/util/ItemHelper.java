package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemHelper
{
	
	public static void consumeOne(ItemStack stack)
	{
		if (stack.isEmpty())
			return;
		int amount = stack.getCount();
		stack.setCount(amount - 1);
	}
	
	public static boolean hasItemInHand(Player player, Item item)
	{
		return player.getMainHandItem().is(item) || player.getOffhandItem().is(item);
	}
	
	public static boolean hasItemInHand(Player player, Item[] items)
	{
		for (Item item : items)
		{
			if (hasItemInHand(player, item))
			{
				return true;
			}
		}
		return false;
	}


	
}
