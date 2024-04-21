package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.nautils.math.GuiPos;

public class GuiGhastlySeeker extends GuiPreset0 {

	public GuiGhastlySeeker(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addBaubleSlotBg(pPoseStack, 0, leftRowPos().addY(10));
		this.addBaubleSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1).addY(20));
		this.addBaubleSlotBg(pPoseStack, 2, rightRowPos().addY(4));
		this.addBaubleSlotBg(pPoseStack, 3, rightRowPos().slotBelow().addY(8));
		this.addSlotBg(pPoseStack, 4, rightRowPos().slotBelow(2).addY(12), 2, 3);
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.DARK);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	
	@Override
	public GuiPos getEntityRenderPosition()
	{
		return super.getEntityRenderPosition().addY(-10);
	}
	
	@Override
	public int getMobRenderScale()
	{
		return 15;
	}

}
