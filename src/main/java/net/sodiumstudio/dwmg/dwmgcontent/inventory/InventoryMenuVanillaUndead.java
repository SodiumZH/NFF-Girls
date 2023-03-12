package net.sodiumstudio.dwmg.dwmgcontent.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.math.IntVec2;

public abstract class InventoryMenuVanillaUndead extends AbstractInventoryMenuBefriended {

	protected InventoryMenuVanillaUndead(int containerId, Inventory playerInventory, Container container,
			IBefriendedMob mob) {
		super(containerId, playerInventory, container, mob);

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
