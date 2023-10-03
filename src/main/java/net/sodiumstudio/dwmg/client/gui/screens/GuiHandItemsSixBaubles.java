package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiHandItemsSixBaubles extends GuiPreset0
{

	public GuiHandItemsSixBaubles(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, rightRowPos().slotBelow(3), 1, 1);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow(3), 1, 0);
		this.addBaubleSlotBg(pPoseStack, 2, leftRowPos());
		this.addBaubleSlotBg(pPoseStack, 3, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(pPoseStack, 4, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(pPoseStack, 5, rightRowPos());
		this.addBaubleSlotBg(pPoseStack, 6, rightRowPos().slotBelow(1));
		this.addBaubleSlotBg(pPoseStack, 7, rightRowPos().slotBelow(2));
		this.addMobRenderBox(pPoseStack);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	
}
