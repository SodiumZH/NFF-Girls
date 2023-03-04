package com.sodium.dwmg.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.inventory.AbstractInventoryMenuBefriended;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/** GUI template for all vanilla-undead-mob-like befriended mobs.
*/

@OnlyIn(Dist.CLIENT)
public class GuiVanillaUndead extends AbstractGuiBefriended {
	public static final ResourceLocation INVENTORY_LOCATION = new ResourceLocation("dwmg",
			"textures/gui/container/bef_undead_girls.png");

	public GuiVanillaUndead(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory, Component pTitle,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, pTitle, mob);
		imageWidth = 176;
		imageHeight = 166;
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
		// Main window
		this.blit(pPoseStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
		// Armor slots
		this.blit(pPoseStack, i + 7, j + 5, imageWidth, 0, this.imageWidth + 18, 4 * 18);
		// Hand and bauble slots
		this.blit(pPoseStack, i + 79, j + 5, imageWidth + 18, 0, this.imageWidth + 18 * 2, 4 * 18);
		// Info box
		this.blit(pPoseStack, i + 99, j + 5, imageWidth, 18 * 4, this.imageWidth + 70, 4 * 18 + 72);
		InventoryScreen.renderEntityInInventory(i + 52, j + 70, 17, (float) (i + 52) - this.xMouse,
				(float) (j + 75 - 50) - this.yMouse, (LivingEntity)mob);
	}

}
