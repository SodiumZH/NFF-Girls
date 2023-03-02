package com.sodium.dwmg.client.gui.screens.inventory;

import com.sodium.dwmg.entities.IBefriendedMob;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class InventoryMenuVanillaUndeadGirls extends AbstractContainerMenu 
{

	protected final Container container;
	protected final IBefriendedMob mob;
	
	
	
	protected InventoryMenuVanillaUndeadGirls(int pContainerId, Inventory pPlayerInventory, Container pContainer, final IBefriendedMob pMob) 
	{
		super(null, pContainerId);
		this.container = pContainer;
		this.mob = pMob;
	}

	@Override
	public boolean stillValid(Player pPlayer) {
		// TODO Auto-generated method stub
		return false;
	}

}
