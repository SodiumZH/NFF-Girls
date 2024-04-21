package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.nautils.math.GuiPos;

public class GuiHandItemsTwoBaubles extends GuiPreset0 {

	public GuiHandItemsTwoBaubles(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, leftRowPos().addY(10), getMainHandIconPos().x, getMainHandIconPos().y);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow(1).addY(20), getOffHandIconPos().x, getOffHandIconPos().y);
		this.addBaubleSlotBg(graphics, 2, rightRowPos().addY(10));
		this.addBaubleSlotBg(graphics, 3, rightRowPos().slotBelow().addY(20));
		this.addMobRenderBox(graphics, MobRenderBoxStyle.DARK);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
	@Override
	public GuiPos getEntityRenderPosition()
	{
		return super.getEntityRenderPosition().addY(0);
	}
	
	@Override
	public int getMobRenderScale()
	{
		return 18;
	}

	public GuiPos getMainHandIconPos()
	{
		return GuiPos.valueOf(1, 1);
	}
	
	public GuiPos getOffHandIconPos()
	{
		return GuiPos.valueOf(1, 0);
	}
	
}
