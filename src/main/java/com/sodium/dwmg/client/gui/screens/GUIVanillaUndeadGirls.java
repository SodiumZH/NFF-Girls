package com.sodium.dwmg.client.gui.screens;

import com.sodium.dwmg.entities.IBefriendedMob;

import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GUIVanillaUndeadGirls extends Screen
{
	
	protected static final ResourceLocation LOCATION = new ResourceLocation("dwmg", "textures/gui/container/bef_undead_girls.png");
	
	
	
	IBefriendedMob boundMob;
	public EditBox name;
	public EditBox state;
	
	protected GUIVanillaUndeadGirls(Inventory playerInventory, IBefriendedMob mob) {
		
		//super(pTitle);
	}

	@Override
	protected void init() 
	{
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		//state = new Component()
	}
	
	
	
}
