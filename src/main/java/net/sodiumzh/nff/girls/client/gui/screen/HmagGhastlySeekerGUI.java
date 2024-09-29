package net.sodiumzh.nff.girls.client.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumzh.nautils.math.GuiPos;
import net.sodiumzh.nff.services.entity.taming.INFFTamed;
import net.sodiumzh.nff.services.inventory.NFFTamedInventoryMenu;

public class HmagGhastlySeekerGUI extends NFFGirlsGUIPreset0 {

	public HmagGhastlySeekerGUI(NFFTamedInventoryMenu pMenu, Inventory pPlayerInventory,
			INFFTamed mob) {
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
