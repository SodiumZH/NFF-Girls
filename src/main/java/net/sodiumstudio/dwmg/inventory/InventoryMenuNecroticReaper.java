package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
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
	protected boolean shouldMainHandAccept(ItemStack stack)
	{
		return !(stack.getItem() instanceof TieredItem) || (stack.getItem() instanceof HoeItem);
	}
	
	
}
