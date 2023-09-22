package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class GuiEquipmentTwoBaubles extends GuiPreset0 {

	public GuiEquipmentTwoBaubles(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, leftRowPos(), 0, 1);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow(1), 0, 2);
		this.addSlotBg(graphics, 2, leftRowPos().slotBelow(2), 0, 3);
		this.addSlotBg(graphics, 3, leftRowPos().slotBelow(3), 0, 4);
		this.addSlotBg(graphics, 4, rightRowPos().slotBelow(3), 1, 1);
		this.addSlotBg(graphics, 5, rightRowPos().slotBelow(2), 1, 0);
		this.addSlotBg(graphics, 6, rightRowPos(), 1, 2);
		this.addSlotBg(graphics, 7, rightRowPos().slotBelow(1), 1, 2);
		this.addMobRenderBox(graphics, 2);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	

}
