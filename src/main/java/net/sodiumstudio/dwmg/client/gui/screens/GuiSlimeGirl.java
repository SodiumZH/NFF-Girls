package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.hmag.HmagSlimeGirlEntity;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.math.HtmlColors;
import net.sodiumstudio.nautils.math.IntVec2;
import net.sodiumstudio.nautils.math.LinearColor;

public class GuiSlimeGirl extends GuiPreset0
{

	protected HmagSlimeGirlEntity sg;
	
	public GuiSlimeGirl(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
		sg = (HmagSlimeGirlEntity) mob;
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addBaubleSlotBg(graphics, 0, leftRowPos());
		this.addBaubleSlotBg(graphics, 1, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(graphics, 2, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(graphics, 3, leftRowPos().slotBelow(3));
		this.addMobRenderBox(graphics, 2);
		this.addInfoBox(graphics);
		this.addAttributeInfo(graphics, infoPos());
		this.renderMob(graphics);
	}
	
	// All inventory slots are on the left, and expand the info box
	@Override
	public void addInfoBox(GuiGraphics graphics)
	{
		//this.blit(graphics, absPos(99, 17), IntVec2.valueOf(0, 183), IntVec2.valueOf(120, 72));
		this.drawSprite(graphics, absPos(81, 17), IntVec2.valueOf(0, 183), IntVec2.valueOf(2, 72));
		this.drawSprite(graphics, absPos(83, 17), IntVec2.valueOf(2, 183), IntVec2.valueOf(18, 72));
		this.drawSprite(graphics, absPos(101, 17), IntVec2.valueOf(2, 183), IntVec2.valueOf(118, 72));
	}
	
	public MutableComponent getDefaultColorInfo()
	{
		LinearColor color = sg.getColorLinear();
		Vec3i rgb = color.toRGB();
		String colorKey = "color.dwmg.html." + HtmlColors.getNearestHtmlColor(color);
		String rgbInfo = " (R" + Integer.toString(rgb.getX()) + ", G" + Integer.toString(rgb.getY()) + ", B" + Integer.toString(rgb.getZ()) +")";
		return InfoHelper.builder().putTrans("info.dwmg.gui_slime_color").putText(": ")
				.putTrans(colorKey).putText(rgbInfo).build().withStyle(Style.EMPTY.withColor(color.toCode()));
	}
	
	@Override
	public void addFavorabilityAndLevelInfo(GuiGraphics graphics, IntVec2 position, int color, int textRowWidth)
	{
		graphics.drawString(font, getDefaultLevelAndExpInfo(), position.x, position.y, color);
		position.addY(textRowWidth);
		graphics.drawString(font, getDefaultFavInfo(), position.x, position.y, color);
		position.addY(textRowWidth);
		graphics.drawString(font, getDefaultColorInfo(), position.x, position.y, color);
	}

	@Override
	public IntVec2 infoPos()
	{
		return absPos(85, 21);
	}
	
}
