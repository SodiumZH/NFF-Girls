package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.util.math.IntVec2;

public class GuiEnderExecutor extends GuiPreset0 {

	public GuiEnderExecutor(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, leftRowPos().addY(4), 1, 1);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow().addY(8), 1, 0);
		this.addSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2).addY(12), 2, 2);
		this.addBaubleSlotBg(pPoseStack, 3, rightRowPos().addY(10));
		this.addBaubleSlotBg(pPoseStack, 4, rightRowPos().slotBelow().addY(20));
		this.addMobRenderBox(pPoseStack, 1);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();	
	}
	
	@Override
	public int getMobRenderScale()
	{
		return 18;
	}

}
