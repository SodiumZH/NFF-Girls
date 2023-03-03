package com.sodium.dwmg.inventory;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.util.TagHelper;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

public class InventoryMenuVanillaUndeadGirls extends AbstractInventoryMenuBefriended {

	protected InventoryMenuVanillaUndeadGirls(int containerId, Inventory playerInventory, Container container,
			final IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
		container.startOpen(playerInventory.player);
		// Helmet
		addSlot(new Slot(container, 0, 8, 18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.HEAD && !this.hasItem();
			}
		});
		// Chest
		addSlot(new Slot(container, 1, 8, 36) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.CHEST && !this.hasItem();
			}
		});
		// Legs
		addSlot(new Slot(container, 2, 8, 54) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.LEGS && !this.hasItem();
			}
		});
		// Feet
		addSlot(new Slot(container, 3, 8, 54) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.FEET && !this.hasItem();
			}
		});
		// Main hand
		addSlot(new Slot(container, 4, 8, 72) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem();
			}
		});
		// Off hand
		addSlot(new Slot(container, 5, 8, 90) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem();
			}
		});
		// Two bauble slots
		addSlot(new Slot(container, 6, 8, 90) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem() && TagHelper.hasTag(stack.getItem(), "dwmg", "baubles");
			}
		});
		addSlot(new Slot(container, 7, 8, 90) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem() && TagHelper.hasTag(stack.getItem(), "dwmg", "baubles");
			}
		});
		// Player from 8 to 44(exclude)
		addPlayerInventorySlots(playerInventory, 8, 8, 84);

	}

	@Override
	public ItemStack quickMoveStack(Player player, int index) {

		Slot slot = this.slots.get(index);
		boolean done = false;

		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;

		ItemStack stack = slot.getItem();

		// From mob equipment to player inventory
		if (index < 8) {
			if (!this.moveItemStackTo(stack, 8, 44, true)) {
				return ItemStack.EMPTY;
			} else {
				done = true;
			}
		}
		// From inventory to mob
		else {
			// Try each mob slot
			for (int i = 0; i < 8; ++i) {
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
		return done ? stack.copy() : ItemStack.EMPTY;
	}

	@Override
	public void removed(Player pPlayer) {
		super.removed(pPlayer);
		this.container.stopOpen(pPlayer);
	}

}
