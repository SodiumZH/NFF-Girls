package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class InventoryMenuHandItemsSixBaubles extends InventoryMenuPreset0
{

	public InventoryMenuHandItemsSixBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots() {
		this.addGeneralSlot(0, rightRowPos().slotBelow(3), null);
		this.addGeneralSlot(1, leftRowPos().slotBelow(3), null);
		this.addBaubleSlot(2, leftRowPos(), "0");
		this.addBaubleSlot(3, leftRowPos().slotBelow(1), "1");	
		this.addBaubleSlot(4, leftRowPos().slotBelow(2), "2");
		this.addBaubleSlot(5, rightRowPos(), "3");
		this.addBaubleSlot(6, rightRowPos().slotBelow(1), "4");	
		this.addBaubleSlot(7, rightRowPos().slotBelow(2), "5");	
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {2, 3, 4, 5, 6, 7, 0, 1};
		return this.quickMovePreset(order.length, player, index, order);
	}

}
