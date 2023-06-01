package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiBanshee extends GuiPreset0
{

	public GuiBanshee(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
	}
	
	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, leftRowPos().addY(10), 1, 1);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow().addY(20), 3, 0);
		this.addBaubleSlotBg(pPoseStack, 2, rightRowPos().addY(4));
		this.addBaubleSlotBg(pPoseStack, 3, rightRowPos().slotBelow().addY(8));
		this.addBaubleSlotBg(pPoseStack, 4, rightRowPos().slotBelow(2).addY(12));
		this.addMobRenderBox(pPoseStack, 1);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();	
	}

}
