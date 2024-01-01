package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.client.gui.screens.GuiPreset0.MobRenderBoxStyle;
import net.sodiumstudio.nautils.math.IntVec2;

public class GuiNightwalker extends GuiPreset0
{
	public GuiNightwalker(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addBaubleSlotBg(graphics, 0, leftRowPos());
		this.addBaubleSlotBg(graphics, 1, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(graphics, 2, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow(3));
		this.addSlotBg(graphics, 4, rightRowPos().slotBelow(3), 4, 3);
		this.addMobRenderBox(graphics, MobRenderBoxStyle.LIGHT);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
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
}
