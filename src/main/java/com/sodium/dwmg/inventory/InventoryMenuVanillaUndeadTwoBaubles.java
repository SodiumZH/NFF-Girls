package com.sodium.dwmg.inventory;

import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class InventoryMenuVanillaUndeadTwoBaubles extends InventoryMenuVanillaUndead{

	public InventoryMenuVanillaUndeadTwoBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots() {
		super.addMenuSlots();
	}

}
