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
import net.minecraft.world.phys.Vec2;

public abstract class InventoryMenuVanillaUndead extends AbstractInventoryMenuBefriended {

	protected InventoryMenuVanillaUndead(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
		container.startOpen(playerInventory.player);
		// Helmet
		addSlot(new Slot(container, 0, 8, 6) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.HEAD && !this.hasItem();
			}
		});
		// Chest
		addSlot(new Slot(container, 1, 8, 6+18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.CHEST && !this.hasItem();
			}
		});
		// Legs
		addSlot(new Slot(container, 2, 8, 6+18*2) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.LEGS && !this.hasItem();
			}
		});
		// Feet
		addSlot(new Slot(container, 3, 8, 6+18*3) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.FEET && !this.hasItem();
			}
		});
		// Main hand
		addSlot(new Slot(container, 4, 80, 6) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem();
			}
		});
		// Off hand
		addSlot(new Slot(container, 5, 80, 6+18) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem();
			}
		});
		// Bauble slots
		for (int i = 0; i < getBaubleSlotAmount(); ++i)
			
			addSlot(new Slot(container, 6 + i, getBaubleSlotPositionX(i), getBaubleSlotPositionY(i)) {
				
				@Override
				public boolean mayPlace(ItemStack stack) {
					return !this.hasItem() && TagHelper.hasTag(stack.getItem(), "dwmg", "baubles");
				}
				
				@Override
				public int getMaxStackSize() {
		            return 1;
		        }			
		});

		addPlayerInventorySlots(playerInventory, 6 + getBaubleSlotAmount(), 8, 84);

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

	public int getBaubleSlotAmount()
	{
		return 2;
	}
	
	public int getBaubleSlotPositionX(int index)
	{
		return 80;
	}
	
	public int getBaubleSlotPositionY(int index)
	{
		return 6 + 18 * (index + 2);
	}
	
}
