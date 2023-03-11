package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.dwmg.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.templates.InventoryMenuVanillaUndead;
import net.sodiumstudio.dwmg.client.gui.screens.GuiVanillaUndeadTwoBaubles;

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
