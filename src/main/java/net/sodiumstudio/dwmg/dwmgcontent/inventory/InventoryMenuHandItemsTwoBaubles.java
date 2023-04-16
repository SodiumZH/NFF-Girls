package net.sodiumstudio.dwmg.dwmgcontent.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.dwmg.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.befriendmobs.util.math.IntVec2;
import net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens.GuiHandItemsTwoBaubles;

public class InventoryMenuHandItemsTwoBaubles extends AbstractInventoryMenuBefriended
{

	public InventoryMenuHandItemsTwoBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	public AbstractGuiBefriended makeGui() {
		return new GuiHandItemsTwoBaubles(this, playerInventory, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		// main hand
		IntVec2 v = new IntVec2(8, 18);
		v.addY(10);
		addSlot(new Slot(container, 0, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();
			}
		});
		
		// off hand
		v.slotBelow().addY(10);
		addSlot(new Slot(container, 1, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();
			}
		});

		// baubles
		v.set(80, 18);
		v.addY(10);
		addSlot(new Slot(container, 2, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return BaubleHandler.shouldBaubleSlotAccept(stack, this, mob);
			}
			@Override
			public int getMaxStackSize() {
	            return 1;
	        }	
		});
		
		v.slotBelow().addY(10);
		addSlot(new Slot(container, 3, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return BaubleHandler.shouldBaubleSlotAccept(stack, this, mob);
			}
			@Override
			public int getMaxStackSize() {
	            return 1;
	        }	
		});
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {

		Slot slot = this.slots.get(index);
		boolean done = false;

		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;

		ItemStack stack = slot.getItem();

		// From mob equipment to player inventory
		if (index < 4) {
			if (!this.moveItemStackTo(stack, 4, 40, true)) {
				return ItemStack.EMPTY;
			} else {
				done = true;
			}
		}
		// From inventory to mob
		else {
			int[] order = {2, 3, 0, 1};			
			// Try each mob slot
			for (int i: order) {
				// If the item is suitable and slot isn't occupied
				if (!done && this.getSlot(i).mayPlace(stack) && !this.getSlot(i).hasItem()) {
					// Try moving
					if (this.moveItemStackTo(stack, i, i + 1, false)) {
						done = true;
						break;
					}
				}
			}
		}
		mob.updateFromInventory();
		return done ? stack.copy() : ItemStack.EMPTY;
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.valueOf(20, 101);
	}

}
