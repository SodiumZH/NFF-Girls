package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.nautils.math.IntVec2;

public class InventoryMenuHandItemsTwoBaubles extends InventoryMenuPreset0
{

	public InventoryMenuHandItemsTwoBaubles(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);
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
		int[] order = {2, 3, 0, 1};
		return this.quickMovePreset(order.length, player, index, order);
	}
	
	@Override
	protected IntVec2 getPlayerInventoryPosition()
	{
		return IntVec2.valueOf(32, 101);
	}

}
