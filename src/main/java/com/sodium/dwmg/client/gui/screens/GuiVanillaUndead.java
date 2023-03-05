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
	
	@Override
	public ResourceLocation getTextureLocation() {
		return new ResourceLocation("dwmg",
			"textures/gui/container/bef_vanilla_undead.png");
	}
	
	public GuiVanillaUndead(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory, IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
		imageWidth = 176;
		imageHeight = 171;
		inventoryLabelY = imageHeight - 93;
	}

	@Override
	protected void init() {
		super.init();
		this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, getTextureLocation());
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		// Main window
		this.blit(pPoseStack, i, j, 0, 0, imageWidth, imageHeight);		
		// Armor slots
		this.blit(pPoseStack, i + 7, j + 5, imageWidth, 0, 18, 4*18);
		// Hand and bauble slots
		this.blit(pPoseStack, i + 79, j + 5, imageWidth+18, 0, 18, 4*18);
		// Info box
		this.blit(pPoseStack, i + 99, j + 5, imageWidth, 4*18, 70, 72);
		InventoryScreen.renderEntityInInventory(i + 52, j + 70, 25, (float) (i + 52) - this.xMouse,
				(float) (j + 75 - 50) - this.yMouse, (LivingEntity)mob);		
	}

	@Override
	   public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
	      this.renderBackground(pPoseStack);
	      this.xMouse = (float)pMouseX;
	      this.yMouse = (float)pMouseY;
	      super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	      this.renderTooltip(pPoseStack, pMouseX, pMouseY);
	   }
	
}

