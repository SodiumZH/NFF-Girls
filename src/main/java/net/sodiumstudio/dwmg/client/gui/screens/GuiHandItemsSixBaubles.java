package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
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
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, rightRowPos().slotBelow(3), 1, 1);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow(3), 1, 0);
		this.addBaubleSlotBg(graphics, 2, leftRowPos());
		this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(graphics, 4, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(graphics, 5, rightRowPos());
		this.addBaubleSlotBg(graphics, 6, rightRowPos().slotBelow(1));
		this.addBaubleSlotBg(graphics, 7, rightRowPos().slotBelow(2));
		this.addMobRenderBox(graphics);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
}
