package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.handlers;

import java.util.HashSet;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.IBaubleHolder;

public class BaubleHandlerEnderExecutor extends BaubleHandlerGeneral
{
	
	@Override
	public HashSet<Item> getItemsAccepted() {
		HashSet<Item> set = super.getItemsAccepted();
		return set;
	}

	@Override
	public void applyBaubleEffect(ItemStack bauble, IBaubleHolder owner) {
		super.applyBaubleEffect(bauble, owner);
	}
	
	
	

}
