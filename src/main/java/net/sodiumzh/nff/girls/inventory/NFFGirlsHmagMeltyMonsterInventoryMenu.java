package net.sodiumzh.nff.girls.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;

public class NFFGirlsHmagMeltyMonsterInventoryMenu extends NFFGirlsInventoryMenuPreset0
{

	public NFFGirlsHmagMeltyMonsterInventoryMenu(int containerId, Inventory playerInventory, Container container,
			INFFTamed mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected void addMenuSlots() {
		this.addBaubleSlot(0, leftRowPos().addY(4), "0");
		this.addBaubleSlot(1, leftRowPos().slotBelow().addY(8), "1");
		this.addBaubleSlot(2, rightRowPos().addY(4), "2");
		this.addBaubleSlot(3, rightRowPos().slotBelow().addY(8), "3");
		this.addGeneralSlot(4, leftRowPos().slotBelow(2).addY(12), s -> s.is(Items.LAVA_BUCKET));
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		int[] order = {4, 0, 1, 2, 3};
		return this.quickMovePreset(order.length, pPlayer, pIndex, order);
	}

}
