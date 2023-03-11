package net.sodiumstudio.befriendmobs.inventory.templates;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.befriendmobs.util.TagHelper;
import net.sodiumstudio.befriendmobs.util.math.IntVec2;

public abstract class InventoryMenuVanillaUndead extends AbstractInventoryMenuBefriended {

	protected InventoryMenuVanillaUndead(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);

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
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.HEAD && !this.hasItem();
			}
		});
		
		v.slotBelow();
		// Chest
		addSlot(new Slot(container, 1, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.CHEST && !this.hasItem();
			}
		});
		
		v.slotBelow();
		// Legs
		addSlot(new Slot(container, 2, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.LEGS && !this.hasItem();
			}
		});
		
		v.slotBelow();
		// Feet
		addSlot(new Slot(container, 3, v.x, v.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == EquipmentSlot.FEET && !this.hasItem();
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
		v.set(getBaubleSlotPositionX(), getBaubleSlotPositionY());
		for (int i = 0; i < getBaubleSlotAmount(); ++i)
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
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.of(20, 101);
	}
	
	@Override
	public ItemStack quickMoveStack(Player player, int index) {

		Slot slot = this.slots.get(index);
		boolean done = false;

		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;

		ItemStack stack = slot.getItem();

		// From mob equipment to player inventory
		if (index < 6+getBaubleSlotAmount()) {
			if (!this.moveItemStackTo(stack, 6+getBaubleSlotAmount(), 6+getBaubleSlotAmount()+32, true)) {
				return ItemStack.EMPTY;
			} else {
				done = true;
			}
		}
		// From inventory to mob
		else {
			// Try each mob slot
			for (int i = 0; i < 6 + getBaubleSlotAmount(); ++i) {
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
	
	public int getBaubleSlotPositionX()
	{
		return 80;
	}
	
	public int getBaubleSlotPositionY()
	{
		return 18;
	}
	
}
