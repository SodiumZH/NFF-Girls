package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiBanshee extends GuiPreset0
{

	public GuiBanshee(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
	}
	
	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, leftRowPos().addY(10), 1, 1);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow().addY(20), 3, 0);
		this.addBaubleSlotBg(graphics, 2, rightRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 3, rightRowPos().slotBelow().addY(8));
		this.addBaubleSlotBg(graphics, 4, rightRowPos().slotBelow(2).addY(12));
		this.addMobRenderBox(graphics, 1);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}

}
