package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsSixBaublesInventoryMenu extends NFFGirlsInventoryMenuPreset0
{

	public NFFGirlsSixBaublesInventoryMenu(int containerId, Inventory playerInventory, Container container,
			INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots() {
		this.addBaubleSlot(0, leftRowPos().addY(4), "0");
		this.addBaubleSlot(1, leftRowPos().slotBelow().addY(8), "1");
		this.addBaubleSlot(2, leftRowPos().slotBelow(2).addY(12), "2");	
		this.addBaubleSlot(3, rightRowPos().addY(4), "3");
		this.addBaubleSlot(4, rightRowPos().slotBelow().addY(8), "4");
		this.addBaubleSlot(5, rightRowPos().slotBelow(2).addY(12), "5");	
		
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {0, 1, 2, 3, 4, 5};
		return this.quickMovePreset(order.length, player, index, order);
	}

}
