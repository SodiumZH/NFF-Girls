package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsHandItemsFourBaublesDefaultInventoryMenu extends NFFGirlsHandItemsFourBaublesInventoryMenu
{

	public NFFGirlsHandItemsFourBaublesDefaultInventoryMenu(int containerId, Inventory playerInventory, Container container, INFFTamed mob)
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
