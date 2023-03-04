package com.sodium.dwmg.client.gui.screens;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.inventory.AbstractInventoryMenuBefriended;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractGuiBefriended extends AbstractContainerScreen<AbstractInventoryMenuBefriended>{

	public IBefriendedMob mob;
	protected float xMouse = 0;
	protected float yMouse = 0;
	
	public AbstractGuiBefriended(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory, Component pTitle, IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, pTitle);
		this.mob = mob;
	}
	
}
