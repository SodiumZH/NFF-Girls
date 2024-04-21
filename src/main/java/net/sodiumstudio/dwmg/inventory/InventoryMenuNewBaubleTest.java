package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleSystem;
import net.sodiumstudio.nautils.math.GuiPos;

public abstract class InventoryMenuNewBaubleTest extends InventoryMenuPreset0
{

	public InventoryMenuNewBaubleTest(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void addBaubleSlot(int slot, GuiPos pos, String key)
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
