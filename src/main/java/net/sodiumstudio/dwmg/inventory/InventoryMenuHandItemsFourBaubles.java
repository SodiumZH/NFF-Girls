package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.nautils.math.IntVec2;

public abstract class InventoryMenuHandItemsFourBaubles extends InventoryMenuPreset0
{


	protected InventoryMenuHandItemsFourBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addGeneralSlot(0, rightRowPos().slotBelow(3), s -> shouldMainHandAccept(s));
		this.addGeneralSlot(1, leftRowPos().slotBelow(3), s -> shouldOffHandAccept(s));
		this.addBaubleSlot(2, leftRowPos(), "0");
		this.addBaubleSlot(3, leftRowPos().slotBelow(), "1");
		this.addBaubleSlot(4, rightRowPos(), "2");
		this.addBaubleSlot(5, rightRowPos().slotBelow(), "3");
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {2, 3, 4, 5, 0, 1};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.valueOf(32, 101);
	}

	// Whether main hand slot accepts the item.
	protected boolean shouldMainHandAccept(ItemStack stack)
	{
		return false;
	}
	
	// Whether off hand slot accepts the item.
	protected boolean shouldOffHandAccept(ItemStack stack)
	{
		return false;
	}

	
}
