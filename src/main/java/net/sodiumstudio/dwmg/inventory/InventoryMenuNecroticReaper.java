package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreen;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.client.gui.screens.GuiNecroticReaper;

public class InventoryMenuNecroticReaper extends InventoryMenuHandItemsFourBaubles
{

	public InventoryMenuNecroticReaper(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	public BefriendedGuiScreen makeGui() {
		return new GuiNecroticReaper(this, playerInventory, mob);
	}
	
}
