package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.FlowerBlock;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class HmagBansheeInventoryMenu extends NFFGirlsInventoryMenuPreset0
{

	public HmagBansheeInventoryMenu(int containerId, Inventory playerInventory, Container container, INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots() {
		this.addGeneralSlot(0, leftRowPos().addY(10), null);
		this.addGeneralSlot(1, leftRowPos().slotBelow().addY(20), null, 1);
		this.addBaubleSlot(2, rightRowPos().addY(4), "0");
		this.addBaubleSlot(3, rightRowPos().slotBelow().addY(8), "1");
		this.addBaubleSlot(4, rightRowPos().slotBelow(2).addY(12), "2");
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int inventorySize = 5;
		//boolean done = false;
		Slot slot = this.slots.get(index);
		ItemStack stack = slot.getItem();
		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;
		
		boolean flag = stack.getItem() instanceof BlockItem b && b.getBlock() instanceof FlowerBlock;
		int[] order = {2, 3, 4, 0, 1};
		int[] order1 = {2, 3, 4, 1, 0};
		return this.quickMovePreset(inventorySize, player, index, flag ? order1 : order);
	}

}
