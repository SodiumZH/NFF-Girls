package com.sodium.dwmg.inventory;

import com.sodium.dwmg.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import com.sodium.dwmg.befriendmobs.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobs.inventory.templates.InventoryMenuVanillaUndead;

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

	@Override
	public AbstractGuiBefriended makeGui() {
		// TODO Auto-generated method stub
		return null;
	}
}
