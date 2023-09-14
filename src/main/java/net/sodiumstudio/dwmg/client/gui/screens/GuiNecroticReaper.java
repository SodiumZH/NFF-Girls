package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.nautils.math.IntVec2;

public class GuiNecroticReaper extends GuiPreset0
{
	public GuiNecroticReaper(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addSlotBg(pPoseStack, 0, rightRowPos().slotBelow(3), 1, 4);
		this.addSlotBg(pPoseStack, 1, leftRowPos().slotBelow(3), 2, 4);
		this.addBaubleSlotBg(pPoseStack, 2, leftRowPos());
		this.addBaubleSlotBg(pPoseStack, 3, leftRowPos().slotBelow());
		this.addBaubleSlotBg(pPoseStack, 4, rightRowPos());
		this.addBaubleSlotBg(pPoseStack, 5, rightRowPos().slotBelow());
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
		return 26;
	}
}
