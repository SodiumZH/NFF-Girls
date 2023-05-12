package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashSet;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;

public class BaubleHandlerEnderExecutor extends BaubleHandlerGeneral
{
	
	@Override
	public HashSet<Item> getItemsAccepted(String key) {
		HashSet<Item> set = super.getItemsAccepted(key);
		return set;
	}


}
