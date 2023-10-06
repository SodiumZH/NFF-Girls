package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiSixBaubles extends GuiPreset0
{

	public GuiSixBaubles(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addBaubleSlotBg(graphics, 0, leftRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 1, leftRowPos().slotBelow(1).addY(8));
		this.addBaubleSlotBg(graphics, 2, leftRowPos().slotBelow(2).addY(12));
		this.addBaubleSlotBg(graphics, 3, rightRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 4, rightRowPos().slotBelow(1).addY(8));
		this.addBaubleSlotBg(graphics, 5, rightRowPos().slotBelow(2).addY(12));
		this.addMobRenderBox(graphics);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}

	@Override
	public int getMobRenderScale()
	{
		return 18;
	}
	
}
