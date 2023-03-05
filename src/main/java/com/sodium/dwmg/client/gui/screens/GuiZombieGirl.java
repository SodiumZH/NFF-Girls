package com.sodium.dwmg.client.gui.screens;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.inventory.AbstractInventoryMenuBefriended;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiZombieGirl extends GuiVanillaUndead {

	public GuiZombieGirl(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory, Component pTitle,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}



}
