package com.sodium.dwmg.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.inventory.InventoryMenuVanillaUndeadGirls;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class GUIVanillaUndeadGirls extends AbstractGUIBefriended<InventoryMenuVanillaUndeadGirls> {
	public static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation("dwmg",
			"textures/gui/container/bef_undead_girls.png");

	public GUIVanillaUndeadGirls(InventoryMenuVanillaUndeadGirls pMenu, Inventory pPlayerInventory, Component pTitle,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, pTitle, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void init() {
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
		// state = new Component()
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, INVENTORY_LOCATION);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);

		InventoryScreen.renderEntityInInventory(i + 51, j + 60, 17, (float) (i + 51) - this.xMouse,
				(float) (j + 75 - 50) - this.yMouse, (LivingEntity)mob);
	}

}
