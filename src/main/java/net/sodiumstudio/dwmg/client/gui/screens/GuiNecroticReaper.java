package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.nautils.math.GuiPos;

public class GuiNecroticReaper extends GuiPreset0
{
	public GuiNecroticReaper(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addSlotBg(graphics, 0, rightRowPos().slotBelow(3), 1, 4);
		this.addSlotBg(graphics, 1, leftRowPos().slotBelow(3), 2, 4);
		this.addBaubleSlotBg(graphics, 2, leftRowPos());
		this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow());
		this.addBaubleSlotBg(graphics, 4, rightRowPos());
		this.addBaubleSlotBg(graphics, 5, rightRowPos().slotBelow());
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
		return 26;
	}
}
