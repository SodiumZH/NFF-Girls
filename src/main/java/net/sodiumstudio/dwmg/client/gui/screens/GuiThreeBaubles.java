package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiThreeBaubles extends GuiPreset0
{

	public GuiThreeBaubles(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addBaubleSlotBg(pPoseStack, 0, rightRowPos().addY(4));
		this.addBaubleSlotBg(pPoseStack, 1, rightRowPos().slotBelow(1).addY(8));
		this.addBaubleSlotBg(pPoseStack, 2, rightRowPos().slotBelow(2).addY(12));
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.DARK);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	
}
