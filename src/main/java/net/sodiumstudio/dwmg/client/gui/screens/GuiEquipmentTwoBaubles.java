package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiEquipmentTwoBaubles extends GuiPreset0 {

	public GuiEquipmentTwoBaubles(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, leftRowPos(), 0, 1);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1), 0, 2);
		this.addSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2), 0, 3);
		this.addSlotBg(pPoseStack, 3, leftRowPos().slotBelow(3), 0, 4);
		this.addSlotBg(pPoseStack, 4, rightRowPos().slotBelow(3), 1, 1);
		this.addSlotBg(pPoseStack, 5, rightRowPos().slotBelow(2), 1, 0);
		this.addSlotBg(pPoseStack, 6, rightRowPos(), 1, 2);
		this.addSlotBg(pPoseStack, 7, rightRowPos().slotBelow(1), 1, 2);
		this.addMobRenderBox(pPoseStack, 2);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	

}
