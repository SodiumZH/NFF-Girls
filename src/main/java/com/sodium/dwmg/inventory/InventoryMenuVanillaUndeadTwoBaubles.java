package com.sodium.dwmg.inventory;

import com.sodium.dwmg.befriendmobsapi.client.gui.screens.AbstractGuiBefriended;
import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.inventory.templates.InventoryMenuVanillaUndead;
import com.sodium.dwmg.client.gui.screens.GuiVanillaUndeadTwoBaubles;

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

	@Override
	public AbstractGuiBefriended makeGui() {
		return new GuiVanillaUndeadTwoBaubles(this, playerInventory, mob);
	}

}
