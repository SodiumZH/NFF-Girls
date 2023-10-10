package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class InventoryMenuRedcap extends InventoryMenuPreset0
{

	public InventoryMenuRedcap(int containerId, Inventory playerInventory, Container container, IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addMenuSlots() {
		this.addArmorSlot(1, leftRowPos().addY(4), CHEST, null);
		this.addArmorSlot(2, leftRowPos().slotBelow(1).addY(8), LEGS, null);
		this.addArmorSlot(3, leftRowPos().slotBelow(2).addY(12), FEET, null);
		this.addGeneralSlot(4, rightRowPos().slotBelow(2).addY(12), stack -> stack.getItem() != null && stack.getItem() instanceof AxeItem);
		this.addGeneralSlot(5, rightRowPos().slotBelow(1).addY(8), null);
		this.addBaubleSlot(6, rightRowPos().addY(4), "0");
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {6, 1, 2, 3, 4, 5};
		return this.quickMovePreset(order.length, player, index, order);
	}

}
