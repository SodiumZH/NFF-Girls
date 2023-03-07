package com.sodium.dwmg.client.gui.screens;

import com.sodium.dwmg.befriendmobsapi.client.gui.screens.templates.GuiVanillaUndead;
import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.inventory.AbstractInventoryMenuBefriended;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiVanillaUndeadTwoBaubles extends GuiVanillaUndead {

	public GuiVanillaUndeadTwoBaubles(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}



}
