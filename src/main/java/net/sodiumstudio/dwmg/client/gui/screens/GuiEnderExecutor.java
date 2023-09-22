package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiEnderExecutor extends GuiPreset0 {

	public GuiEnderExecutor(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, leftRowPos().addY(4), 1, 1);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow().addY(8), 1, 0);
		this.addSlotBg(graphics, 2, leftRowPos().slotBelow(2).addY(12), 2, 2);
		this.addBaubleSlotBg(graphics, 3, rightRowPos().addY(10));
		this.addBaubleSlotBg(graphics, 4, rightRowPos().slotBelow().addY(20));
		this.addMobRenderBox(graphics, 1);
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
