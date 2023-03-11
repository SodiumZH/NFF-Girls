package net.sodiumstudio.dwmg.dwmgcontent.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.dwmg.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.templates.InventoryMenuVanillaUndead;
import net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens.GuiVanillaUndeadOneBauble;

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
