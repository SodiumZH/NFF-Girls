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

	protected AbstractInventoryMenuBefriended(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(null, containerId);
		this.mob = mob;
		this.container = container;
	}

	protected void addPlayerInventorySlots(Inventory playerInventory, int startSlotIndex, int startX, int startY) {

		for (int i1 = 0; i1 < 3; ++i1) {
			for (int k1 = 0; k1 < 9; ++k1) {
				this.addSlot(new Slot(playerInventory, startSlotIndex + k1 + i1 * 9 + 9, startX + k1 * 18,
						startY + i1 * 18));
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

}
