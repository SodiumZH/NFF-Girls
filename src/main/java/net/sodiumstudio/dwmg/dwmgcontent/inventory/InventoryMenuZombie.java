package net.sodiumstudio.dwmg.dwmgcontent.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.math.IntVec2;
import net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens.GuiZombieGirl;

public class InventoryMenuZombie extends AbstractInventoryMenuBefriended{

	public InventoryMenuZombie(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
	}

	@Override
	public AbstractGuiBefriended makeGui() {
		return new GuiZombieGirl(this, playerInventory, mob);
	}

	@Override
	protected void addMenuSlots()
	{
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
				return !this.hasItem();
			}
		});
		
		// Bauble slots
		v.set(80, 18);
		for (int i = 0; i < 2; ++i)
		{
			addSlot(new Slot(container, 6 + i, v.x, v.y) {
				
				@Override
				public boolean mayPlace(ItemStack stack) {
					return !this.hasItem() && TagHelper.hasTag(stack.getItem(), BefriendMobs.modDomain(), "baubles");
				}
				
				@Override
				public int getMaxStackSize() {
		            return 1;
		        }	
				
			});
			
			v.slotBelow();
		}
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {

		Slot slot = this.slots.get(index);
		boolean done = false;

		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;

		ItemStack stack = slot.getItem();

		// From mob equipment to player additionalInventory
		if (index < 8) {
			if (!this.moveItemStackTo(stack, 8, 44, true)) {
				return ItemStack.EMPTY;
			} else {
				done = true;
			}
		}
		// From additionalInventory to mob
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
		mob.saveInventory((SimpleContainer) container);
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

