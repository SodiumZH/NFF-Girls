package net.sodiumstudio.dwmg.inventory;

import java.util.function.Predicate;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;

public class InventoryMenuKobold extends InventoryMenuHandItemsTwoBaubles
{

	public InventoryMenuKobold(int containerId, Inventory playerInventory, Container container, IBefriendedMob mob)
	{
		super(containerId, playerInventory, container, mob);
	}

	@Override
	protected Predicate<ItemStack> getMainHandCondition()
	{
		return l -> l.getItem() instanceof PickaxeItem;
	}
	
}
