package net.sodiumstudio.dwmg.inventory;

import java.util.function.Predicate;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.nautils.math.IntVec2;

public class InventoryMenuHandItemsTwoBaubles extends InventoryMenuPreset0
{
	public InventoryMenuHandItemsTwoBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addGeneralSlot(0, leftRowPos().addY(10), getMainHandCondition());
		this.addGeneralSlot(1, leftRowPos().slotBelow().addY(20), null, getOffHandMaxAmount());
		this.addBaubleSlot(2, rightRowPos().addY(10), "0");
		this.addBaubleSlot(3, rightRowPos().slotBelow().addY(20), "1");
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {2, 3, 0, 1};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.valueOf(32, 101);
	}

	protected Predicate<ItemStack> getMainHandCondition()
	{
		return null;
	}
	
	protected int getOffHandMaxAmount()
	{
		return 64;
	}
}
