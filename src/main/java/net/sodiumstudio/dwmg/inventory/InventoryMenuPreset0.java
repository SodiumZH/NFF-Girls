package net.sodiumstudio.dwmg.inventory;

import java.util.function.Predicate;

import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleSystem;
import net.sodiumstudio.nautils.math.IntVec2;

/**
 * The inventory menu adapting GuiPreset0
 */
public abstract class InventoryMenuPreset0 extends BefriendedInventoryMenu
{
	protected static final EquipmentSlot HEAD = EquipmentSlot.HEAD;
	protected static final EquipmentSlot CHEST = EquipmentSlot.CHEST;
	protected static final EquipmentSlot LEGS = EquipmentSlot.LEGS;
	protected static final EquipmentSlot FEET = EquipmentSlot.FEET;
	protected static final EquipmentSlot MAINHAND = EquipmentSlot.MAINHAND;
	protected static final EquipmentSlot OFFHAND = EquipmentSlot.OFFHAND;
	
	
	public InventoryMenuPreset0(int containerId, Inventory playerInventory, Container container, IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition() {
		return IntVec2.valueOf(32, 101);
	}
	
	protected IntVec2 leftRowPos()
	{
		return IntVec2.valueOf(8, 18);
	}
	
	protected IntVec2 rightRowPos()
	{
		return IntVec2.valueOf(80, 18);
	}
	
	protected void addGeneralSlot(int slotIndex, IntVec2 pos, Predicate<ItemStack> additionalCondition)
	{
		addSlot(new Slot(container, slotIndex, pos.x, pos.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem() && (additionalCondition == null || additionalCondition.test(stack));
			}
		});
	}
	
	protected void addGeneralSlot(int slotIndex, IntVec2 pos, Predicate<ItemStack> additionalCondition, int maxStackSize)
	{
		addSlot(new Slot(container, slotIndex, pos.x, pos.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return !this.hasItem() && (additionalCondition == null || additionalCondition.test(stack));
			}
			@Override
			public int getMaxStackSize()
			{
				return maxStackSize;
			}
		});
	}
	
	protected void addArmorSlot(int slotIndex, IntVec2 pos, EquipmentSlot ArmorType, Predicate<ItemStack> additionalCondition)
	{
		addSlot(new Slot(container, slotIndex, pos.x, pos.y) {
			@Override
			public boolean mayPlace(ItemStack stack) {
				return (stack.getItem() instanceof ArmorItem)
						&& ((ArmorItem) stack.getItem()).getSlot() == ArmorType
						&& !EnchantmentHelper.hasBindingCurse(stack)
						&& !this.hasItem()
						&& (additionalCondition == null || additionalCondition.test(stack));
			}
		});
	}
	
	@Override
	public void removed(Player pPlayer) {
		super.removed(pPlayer);
	}
	
	public ItemStack quickMovePreset(int inventorySize, Player player, int index, int[] order)
	{

		Slot slot = this.slots.get(index);
		boolean done = false;

		if (slot == null || !slot.hasItem())
			return ItemStack.EMPTY;

		ItemStack stack = slot.getItem();
		//ItemStack stackCopy = stack.copy();
		// From mob's additional inventory to player inventory
		if (index < inventorySize) {
			if (!this.moveItemStackTo(stack, inventorySize, inventorySize + 36, true)) {
				return ItemStack.EMPTY;
			} else {
				done = true;
			}
		}
		// From inventory to mob's additional inventory
		else {
			
			// Try each mob slot
			for (int i = 0; i < order.length; ++i)
			{
				// If the item is suitable and slot isn't occupied
				if (this.getSlot(order[i]).mayPlace(stack) && !this.getSlot(order[i]).hasItem())
				{
					// Try moving
					if (this.moveItemStackTo(stack, order[i], order[i] + 1, false))
					{
						done = true;
						return ItemStack.EMPTY;
					}
				}
			}
			
		}
		return done ? stack.copy() : ItemStack.EMPTY;
	}
	
	// ===== 0.x.21 new bauble changed
	
	@Override
	protected void addBaubleSlot(int slot, IntVec2 pos, String key)
	{
		addSlot(new Slot(container, slot, pos.x, pos.y) {			
			@Override
			public boolean mayPlace(ItemStack stack) {
				//return BaubleHandler.shouldBaubleSlotAccept(stack, this, mob, key);
				return BaubleSystem.canEquipOn(stack, mob.asMob(), key);
			}			
			@Override
			public int getMaxStackSize() {
	            return 1;
	        }			
		});
	}
	
}
