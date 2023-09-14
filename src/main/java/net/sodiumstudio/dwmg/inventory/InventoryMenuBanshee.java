package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.block.FlowerBlock;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;

public class InventoryMenuBanshee extends InventoryMenuPreset0
{

	public InventoryMenuBanshee(int containerId, Inventory playerInventory, Container container, IBefriendedMob mob)
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
		boolean done = false;
		Slot slot = this.slots.get(index);
		ItemStack stack = slot.getItem();
		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;
		
		if (index >= inventorySize)
		{		
			// If flower, try offhand first
			if (stack.getItem() instanceof BlockItem blockitem && blockitem.getBlock() instanceof FlowerBlock)
			{
				if (this.moveItemStackTo(stack, 1, 2, false))
				{
					done = true;
				}
			}
			// If weapon, try mainhand first
			else if (stack.getItem() instanceof TieredItem)
			{
				if (this.moveItemStackTo(stack, 0, 1, false))
				{
					done = true;
				}
			}			
		}
		if (done)
			return stack.copy();
		else
		{
			int[] order = {2, 3, 4, 0, 1};
			return this.quickMovePreset(inventorySize, player, index, order);
		}
	}

}
