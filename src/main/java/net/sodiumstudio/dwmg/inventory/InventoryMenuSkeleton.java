package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.util.TagHelper;
import net.sodiumstudio.befriendmobs.util.math.IntVec2;
import net.sodiumstudio.dwmg.client.gui.screens.GuiBowSecWeaponOneBauble;

public class InventoryMenuSkeleton extends BefriendedInventoryMenu
{

	public InventoryMenuSkeleton(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots() {
		// Helmet
		IntVec2 v = new IntVec2(8, 18);
		addSlot(new Slot(container, 0, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.HEAD 
						&& !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();
			}
		});

		v.slotBelow();
		// Chest
		addSlot(new Slot(container, 1, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.CHEST 
						&& !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();
			}
		});

		v.slotBelow();
		// Legs
		addSlot(new Slot(container, 2, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.LEGS 
						&& !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();
			}
		});

		v.slotBelow();
		// Feet
		addSlot(new Slot(container, 3, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.FEET 
						&& !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();
			}
		});

		v.set(80, 18).slotBelow(3);
		// Main hand
		addSlot(new Slot(container, 4, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem();
			}
		});

		v.slotAbove();
		// Off hand
		addSlot(new Slot(container, 5, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem() && !stack.is(Items.BOW);	// Don't allow to hold bow on offhand
			}
		});

		// Bauble
		v.slotAbove(2);
		addSlot(new Slot(container, 6, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return BaubleHandler.shouldBaubleSlotAccept(stack, this, mob);
			}

			@Override
			public int getMaxStackSize() {
				return 1;
			}
		});

		// backup weapon
		v.slotBelow();
		addSlot(new Slot(container, 7, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem();
			}
		});

		// arrows
		v.slotBelow(2).slotRight().addX(2);
		addSlot(new Slot(container, 8, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem() && stack.getItem().equals(Items.ARROW);
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

		// From mob equipment to player befriendedInventory
		if (index < 9) {
			if (!this.moveItemStackTo(stack, 9, 45, true)) {
				return ItemStack.EMPTY;
			} else {
				done = true;
			}
		}
		// From befriendedInventory to mob
		else {
			// Try put arrow
			if (stack.is(Items.ARROW))
				if (this.moveItemStackTo(stack, 8, 9, false))
					done = true;
			// Try put bow
			if (!done && stack.is(Items.BOW))
				if (this.moveItemStackTo(stack, 4, 5, false) || this.moveItemStackTo(stack, 7, 8, false))
					done = true;
			if (!done)
			{
				// Try each mob slot
				int[] order = {0,1,2,3,6,4,5,7,8};
				for (int i = 0; i < 9; ++i) {
					// If the item is suitable and slot isn't occupied
					if (this.getSlot(order[i]).mayPlace(stack) && !this.getSlot(order[i]).hasItem()) {
						// Try moving
						if (this.moveItemStackTo(stack, order[i], order[i] + 1, false)) {
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
	public AbstractGuiBefriended makeGui() {
		return new GuiBowSecWeaponOneBauble(this, playerInventory, mob);
	}

	@Override
	protected IntVec2 getPlayerInventoryPosition() {
		return IntVec2.valueOf(20, 101);
	}

	@Override
	public void removed(Player pPlayer) {
		super.removed(pPlayer);
		this.container.stopOpen(pPlayer);
	}
}
