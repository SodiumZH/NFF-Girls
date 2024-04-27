package net.sodiumstudio.dwmg.inventory;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.nautils.math.GuiPos;

public class InventoryMenuNightwalker extends InventoryMenuPreset0
{
	
	public InventoryMenuNightwalker(int containerId, Inventory playerInventory, Container container, IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addBaubleSlot(0, leftRowPos(), "0");
		this.addBaubleSlot(1, leftRowPos().slotBelow(), "1");
		this.addBaubleSlot(2, leftRowPos().slotBelow(2), "2");
		this.addBaubleSlot(3, leftRowPos().slotBelow(3), "3");
		this.addGeneralSlot(4, rightRowPos().slotBelow(3), itemstack -> itemstack.is(Items.CLAY_BALL) || itemstack.is(ModItems.ANCIENT_STONE.get()));
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {4, 0, 1, 2, 3};
		return this.quickMovePreset(order.length, player, index, order);
	}

}
