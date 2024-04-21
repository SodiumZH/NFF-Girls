package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.nautils.math.GuiPos;

public class InventoryMenuJiangshi extends InventoryMenuPreset0
{

	public InventoryMenuJiangshi(int containerId, Inventory playerInventory, Container container, IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addBaubleSlot(3, leftRowPos().addY(10), "0");
		this.addBaubleSlot(4, leftRowPos().slotBelow().addY(20), "1");
		this.addBaubleSlot(5, rightRowPos().addY(10), "2");
		this.addBaubleSlot(6, rightRowPos().slotBelow().addY(20), "3");
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {0, 1, 2, 3};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected GuiPos getPlayerInventoryPosition()
	{
		return GuiPos.valueOf(32, 101);
	}
}
