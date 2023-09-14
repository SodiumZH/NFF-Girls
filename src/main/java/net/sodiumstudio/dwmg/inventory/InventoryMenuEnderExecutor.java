package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.nautils.math.IntVec2;

public class InventoryMenuEnderExecutor extends InventoryMenuPreset0{

	public InventoryMenuEnderExecutor(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addGeneralSlot(0, leftRowPos().addY(4), null);
		this.addGeneralSlot(1, leftRowPos().slotBelow().addY(8), null);
		this.addGeneralSlot(2, leftRowPos().slotBelow(2).addY(12), s -> false);		// Block slot isn't implemented
		this.addBaubleSlot(3, rightRowPos().addY(10), "0");
		this.addBaubleSlot(4, rightRowPos().slotBelow().addY(20), "1");
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {3, 4, 2, 0, 1};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.valueOf(32, 101);
	}

	@Override
	public void removed(Player pPlayer) {
		super.removed(pPlayer);
		this.container.stopOpen(pPlayer);
	}
}

