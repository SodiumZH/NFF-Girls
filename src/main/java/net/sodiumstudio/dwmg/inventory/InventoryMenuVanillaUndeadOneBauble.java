package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import net.sodiumstudio.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.templates.InventoryMenuVanillaUndead;
import net.sodiumstudio.dwmg.client.gui.screens.GuiVanillaUndeadOneBauble;

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
		return new GuiVanillaUndeadOneBauble(this, playerInventory, mob);
	}
}
