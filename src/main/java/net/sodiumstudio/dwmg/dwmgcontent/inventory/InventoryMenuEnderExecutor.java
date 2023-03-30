package net.sodiumstudio.dwmg.dwmgcontent.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.math.IntVec2;
import net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens.GuiEnderExecutor;
import net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens.GuiZombieGirl;

public class InventoryMenuEnderExecutor extends AbstractInventoryMenuBefriended{

	public InventoryMenuEnderExecutor(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	public AbstractGuiBefriended makeGui() {
		return new GuiEnderExecutor(this, playerInventory, mob);
	}

	@Override
	protected void addMenuSlots()
	{
		// main hand
		IntVec2 v = new IntVec2(8, 18);
		v.addY(4);
		addSlot(new Slot(container, 0, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();
			}
		});
		
		// off hand
		v.slotBelow().addY(4);
		addSlot(new Slot(container, 1, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();
			}
		});
		
		// block
		v.slotBelow().addY(4);
		addSlot(new Slot(container, 2, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
				/*return (stack.getItem() instanceof BlockItem)
						&& !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();*/
			}
			@Override
			public int getMaxStackSize() {
	            return 1;
	        }	
		});

		// baubles
		v.set(80, 18);
		v.addY(10);
		addSlot(new Slot(container, 3, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return false;
				/*return EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();*/
			}
			@Override
			public int getMaxStackSize() {
	            return 1;
	        }	
		});
		
		v.slotBelow().addY(10);
		addSlot(new Slot(container, 4, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return  false;/*!EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem();*/
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
		if (index < 5) {
			if (!this.moveItemStackTo(stack, 5, 41, true)) {
				return ItemStack.EMPTY;
			} else {
				done = true;
			}
		}
		// From inventory to mob
		else {
			int[] order = {2, 0, 1, 3, 4};			
			// Try each mob slot
			for (int i: order) {
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
		mob.updateFromInventory();
		return done ? stack.copy() : ItemStack.EMPTY;
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.of(20, 101);
	}

	@Override
	public void removed(Player pPlayer) {
		super.removed(pPlayer);
		this.container.stopOpen(pPlayer);
	}
}

