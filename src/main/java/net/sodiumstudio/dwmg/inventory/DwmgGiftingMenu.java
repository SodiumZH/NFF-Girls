package net.sodiumstudio.dwmg.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class DwmgGiftingMenu extends AbstractContainerMenu
{

	protected DwmgGiftingMenu(MenuType<?> pMenuType, int pContainerId)
	{
		super(pMenuType, pContainerId);
		// TODO Auto-generated constructor stub
	}

	@Override
	public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
