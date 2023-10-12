package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiDodomeki extends GuiPreset0
{

	public GuiDodomeki(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addBaubleSlotBg(pPoseStack, 0, leftRowPos());
		this.addBaubleSlotBg(pPoseStack, 1, leftRowPos().slotBelow());
		this.addBaubleSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(pPoseStack, 3, leftRowPos().slotBelow(3));
		/*this.addSlotBg(pPoseStack, 4, rightRowPos(), 4, 0);
		this.addSlotBg(pPoseStack, 5, rightRowPos().slotBelow(), 4, 0);
		this.addSlotBg(pPoseStack, 6, rightRowPos().slotBelow(2), 4, 0);
		this.addSlotBg(pPoseStack, 7, rightRowPos().slotBelow(3), 4, 0);*/
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.LIGHT);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	
}
