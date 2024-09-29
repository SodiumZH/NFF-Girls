package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class HmagDodomekiInventoryMenu extends NFFGirlsInventoryMenuPreset0
{

	public HmagDodomekiInventoryMenu(int containerId, Inventory playerInventory, Container container, INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addBaubleSlot(2, leftRowPos(), "0");
		this.addBaubleSlot(3, leftRowPos().slotBelow(), "1");
		this.addBaubleSlot(4, leftRowPos().slotBelow(2), "2");
		this.addBaubleSlot(5, leftRowPos().slotBelow(3), "3");
		/*this.addGeneralSlot(6, rightRowPos(), s -> false);
		this.addGeneralSlot(7, rightRowPos().slotBelow(), s -> false);
		this.addGeneralSlot(8, rightRowPos().slotBelow(2), s -> false);
		this.addGeneralSlot(9, rightRowPos().slotBelow(3), s -> false);*/
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {0, 1, 2, 3};
		return this.quickMovePreset(order.length, player, index, order);
	}

}
