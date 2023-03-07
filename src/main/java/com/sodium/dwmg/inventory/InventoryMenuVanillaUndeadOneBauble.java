package com.sodium.dwmg.inventory;

import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class InventoryMenuVanillaUndeadOneBauble extends InventoryMenuVanillaUndead{

	public InventoryMenuVanillaUndeadOneBauble(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots() {
		super.addMenuSlots();
	}

	@Override
	public int getBaubleSlotAmount()
	{
		return 1;
	}
}
