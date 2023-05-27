package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreen;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.util.math.IntVec2;
import net.sodiumstudio.dwmg.client.gui.screens.GuiGhastlySeeker;
import net.sodiumstudio.dwmg.client.gui.screens.GuiHandItemsTwoBaubles;

public class InventoryMenuGhastlySeeker extends InventoryMenuPreset0
{

	public InventoryMenuGhastlySeeker(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		this.addBaubleSlot(0, leftRowPos().addY(10), "0");
		this.addBaubleSlot(1, leftRowPos().slotBelow().addY(20), "1");
		this.addBaubleSlot(2, rightRowPos().addY(4), "2");
		this.addBaubleSlot(3, rightRowPos().slotBelow().addY(8), "3");
		this.addGeneralSlot(4, rightRowPos().slotBelow(2).addY(12), s -> s.is(Items.FIRE_CHARGE));
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {
		int[] order = {0, 1, 2, 3, 4};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.valueOf(32, 101);
	}

}
