package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.nautils.math.IntVec2;

public class GuiGhastlySeeker extends GuiPreset0 {

	public GuiGhastlySeeker(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addBaubleSlotBg(graphics, 0, leftRowPos().addY(10));
		this.addBaubleSlotBg(graphics, 1, leftRowPos().slotBelow(1).addY(20));
		this.addBaubleSlotBg(graphics, 2, rightRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 3, rightRowPos().slotBelow().addY(8));
		this.addSlotBg(graphics, 4, rightRowPos().slotBelow(2).addY(12), 2, 3);
		this.addMobRenderBox(graphics, MobRenderBoxStyle.DARK);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
	@Override
	public IntVec2 getEntityRenderPosition()
	{
		return super.getEntityRenderPosition().addY(-10);
	}
	
	@Override
	public int getMobRenderScale()
	{
		return 15;
	}

}
