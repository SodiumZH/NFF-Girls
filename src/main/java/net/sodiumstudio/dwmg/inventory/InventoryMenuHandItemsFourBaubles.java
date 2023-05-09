package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreen;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.util.math.IntVec2;
import net.sodiumstudio.dwmg.client.gui.screens.GuiNecroticReaper;

public abstract class InventoryMenuHandItemsFourBaubles extends BefriendedInventoryMenu
{


	protected InventoryMenuHandItemsFourBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		
		// Left column
		IntVec2 v = new IntVec2(8, 18);
		// Right column
		IntVec2 v1 = new IntVec2 (80, 18);
		
		InventoryMenuHandItemsFourBaubles menu = this;
		
		// Right hand
		v1.slotBelow(3);
		addSlot(new Slot(container, 0, v1.x, v1.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return menu.shouldMainHandAccept(stack);
			}
		});
		
		
		// Left hand
		v.slotBelow(3);
		addSlot(new Slot(container, 1, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return menu.shouldOffHandAccept(stack);
			}
		});
		
		// Baubles
		v.slotAbove(3);
		this.addBaubleSlot(2, v);
		
		v.slotBelow();
		this.addBaubleSlot(3, v);
		
		v1.slotAbove(3);
		this.addBaubleSlot(4, v1);
		
		v1.slotBelow();
		this.addBaubleSlot(5, v1);
		

	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {

		Slot slot = this.slots.get(index);
		boolean done = false;

		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;

		ItemStack stack = slot.getItem();

		// From mob equipment to player inventory
		if (index < 6) {
			if (!this.moveItemStackTo(stack, 6, 6+36, true)) {
				return ItemStack.EMPTY;
			} else {
				done = true;
			}
		}
		// From befriendedInventory to mob
		else {
			// Try Baubles first
			for (int i = 2; i < 6; ++i) {
				// If the item is suitable and slot isn't occupied
				if (this.getSlot(i).mayPlace(stack) && !this.getSlot(i).hasItem()) {
					// Try moving
					if (this.moveItemStackTo(stack, i, i + 1, false)) {
						done = true;
						break;
					}
				}
			}
			if (!done)
			{
				// Then hand
				for (int i = 0; i < 2; ++i) {
					// If the item is suitable and slot isn't occupied
					if (this.getSlot(i).mayPlace(stack) && !this.getSlot(i).hasItem()) {
						// Try moving
						if (this.moveItemStackTo(stack, i, i + 1, false)) {
							done = true;
							break;
						}
					}
				}
			}
		}
		return done ? stack.copy() : ItemStack.EMPTY;
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.valueOf(20, 101);
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
