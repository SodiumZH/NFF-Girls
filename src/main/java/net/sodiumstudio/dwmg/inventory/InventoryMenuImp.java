package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;

public class InventoryMenuImp extends InventoryMenuHandItemsTwoBaubles
{

	public InventoryMenuImp(int containerId, Inventory playerInventory, Container container, IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

}
