package com.sodium.dwmg.befriendmobsapi.example;

import com.sodium.dwmg.befriendmobsapi.client.gui.screens.AbstractGuiBefriended;
import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.inventory.templates.InventoryMenuVanillaUndead;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;

public class Example_InventoryMenuVanillaUndeadTwoBaubles extends InventoryMenuVanillaUndead{

	public Example_InventoryMenuVanillaUndeadTwoBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots() {
		super.addMenuSlots();
	}

	@Override
	public AbstractGuiBefriended makeGui() {
		return new Example_GuiVanillaUndeadTwoBaubles(this, playerInventory, mob);
	}

}
