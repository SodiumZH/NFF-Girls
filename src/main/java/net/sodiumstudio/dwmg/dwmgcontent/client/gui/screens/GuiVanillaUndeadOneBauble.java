package net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.dwmg.befriendmobs.client.gui.screens.templates.GuiVanillaUndead;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.util.math.IntVec2;

public class GuiVanillaUndeadOneBauble extends GuiVanillaUndead {

	public GuiVanillaUndeadOneBauble(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, getTextureLocation());
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		IntVec2 v = IntVec2.of(i, j);
		// Main window
		
		this.blit(pPoseStack, v.x, v.y, 0, 0, imageWidth, imageHeight);	
		// Armor slots
		v.add(7, 17);
		IntVec2 ve = IntVec2.of(imageWidth, 0);	// for empty
		IntVec2 vf = IntVec2.of(imageWidth + 36, 0);	//for filled
		for (int k = 0; k < 4; ++k)
		{
			this.addSlotBg(pPoseStack, k, v, ve, vf);
			v.slotBelow();
			ve.slotBelow();
		}
		
		// Main hand
		v.set(i, j).add(79, 17).slotBelow(3);
		ve.set(imageWidth + 18, 0).slotBelow(3);
		this.addSlotBg(pPoseStack, 4, v, ve, vf);
		// Off hand
		v.slotAbove();
		ve.slotAbove();
		this.addSlotBg(pPoseStack, 5, v, ve, vf);
		// Bauble
		v.slotAbove(2);
		ve.slotAbove(2);
		this.addSlotBg(pPoseStack, 6, v, ve, vf);

		// Info box
		this.blit(pPoseStack, i + 99, j + 17, 0, imageHeight, 96, 72);
		addHealthInfo(pPoseStack, IntVec2.of(i + 102, j + 20));
		InventoryScreen.renderEntityInInventory(i + 52, j + 70, 25, (float) (i + 52) - this.xMouse,
				(float) (j + 75 - 50) - this.yMouse, (LivingEntity)mob);		
	}

}
