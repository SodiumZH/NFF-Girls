package com.sodium.dwmg.inventory;

import com.sodium.dwmg.entities.IBefriendedMob;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class InventoryMenuZombieGirl extends InventoryMenuVanillaUndead{

	public InventoryMenuZombieGirl(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

}
