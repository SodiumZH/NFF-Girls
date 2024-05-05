package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.dwmg.inventory.DwmgGiftingMenu;

public class DwmgGiftingGuiScreen extends AbstractContainerScreen<DwmgGiftingMenu>
{

	public DwmgGiftingGuiScreen(DwmgGiftingMenu pMenu, Inventory pPlayerInventory, Component pTitle)
	{
		super(pMenu, pPlayerInventory, pTitle);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		// TODO Auto-generated method stub
		
	}

}
