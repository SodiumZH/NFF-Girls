package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class InventoryMenuDullahan extends InventoryMenuHandItemsFourBaubles
{

	public InventoryMenuDullahan(int containerId, Inventory playerInventory, Container container, IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob); 
	}
	
	// Whether main hand slot accepts the item.
	@Override
	protected boolean shouldMainHandAccept(ItemStack stack)
	{
		return true;
	}
	
	// Whether off hand slot accepts the item.
	@Override
	protected boolean shouldOffHandAccept(ItemStack stack)
	{
		return true;
	}
}
