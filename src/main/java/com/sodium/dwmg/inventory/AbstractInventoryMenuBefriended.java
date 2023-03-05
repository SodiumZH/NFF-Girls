package com.sodium.dwmg.inventory;

import com.sodium.dwmg.entities.IBefriendedMob;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public abstract class AbstractInventoryMenuBefriended extends AbstractContainerMenu {

	protected final Container container;
	protected final IBefriendedMob mob;
	int x = 0;
	int y = 0;

	protected AbstractInventoryMenuBefriended(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob, int x, int y) {
		super(null, containerId);
		this.mob = mob;
		this.container = container;
		this.x = x;
		this.y = y;
	}

	// Start X and Y doesn't include GUI offset. The GUI offset is automatically added.
	protected void addPlayerInventorySlots(Inventory playerInventory, int startSlotIndex, int startX, int startY) {

		for (int i1 = 0; i1 < 3; ++i1) {
			for (int k1 = 0; k1 < 9; ++k1) {
				this.addSlot(new Slot(playerInventory, startSlotIndex + k1 + i1 * 9 + 9, x + startX + k1 * 18,
						y + startY + i1 * 18));
			}
		}

		for (int j1 = 0; j1 < 9; ++j1) {
			this.addSlot(new Slot(playerInventory, startSlotIndex + j1, startX + j1 * 18, startY + 58));
		}
	}



	@Override
	public boolean stillValid(Player player) {
		return mob.getOwner() == player && ((LivingEntity) mob).distanceTo(player) < 16.0;
	}

	/* Utils */

	LivingEntity getLiving() {
		return (LivingEntity) mob;
	}

	public int getBaubleSlotAmount()
	{
		return 2;
	}
	
	public int getBaubleSlotPositionX(int index)
	{
		return x;
	}
	
	public int getBaubleSlotPositionY(int index)
	{
		return y;
	}
	
}
