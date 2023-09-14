package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class InventoryMenuThreeBaubles extends InventoryMenuPreset0
{

	public InventoryMenuThreeBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addMenuSlots() {
		this.addBaubleSlot(0, rightRowPos().addY(4), "0");
		this.addBaubleSlot(1, rightRowPos().slotBelow().addY(8), "1");
		this.addBaubleSlot(2, rightRowPos().slotBelow().addY(12), "2");
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {0, 1, 2};
		return this.quickMovePreset(order.length, player, index, order);
	}

}
