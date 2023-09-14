package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.nautils.math.IntVec2;

public class GuiHandItemsTwoBaubles extends GuiPreset0 {

	public GuiHandItemsTwoBaubles(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, leftRowPos().addY(10), getMainHandIconPos().x, getMainHandIconPos().y);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1).addY(20), getOffHandIconPos().x, getOffHandIconPos().y);
		this.addBaubleSlotBg(pPoseStack, 2, rightRowPos().addY(10));
		this.addBaubleSlotBg(pPoseStack, 3, rightRowPos().slotBelow().addY(20));
		this.addMobRenderBox(pPoseStack, 2);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	
	@Override
	public IntVec2 getEntityRenderPosition()
	{
		return super.getEntityRenderPosition().addY(0);
	}
	
	@Override
	public int getMobRenderScale()
	{
		return 18;
	}

	public IntVec2 getMainHandIconPos()
	{
		return IntVec2.valueOf(1, 1);
	}
	
	public IntVec2 getOffHandIconPos()
	{
		return IntVec2.valueOf(1, 0);
	}
	
}
