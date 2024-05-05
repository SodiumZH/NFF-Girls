package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.core.Vec3i;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.hmag.HmagSlimeGirlEntity;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.info.ComponentBuilder;
import net.sodiumstudio.nautils.math.HtmlColors;
import net.sodiumstudio.nautils.math.GuiPos;
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
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addBaubleSlotBg(pPoseStack, 0, leftRowPos());
		this.addBaubleSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1));
		this.addBaubleSlotBg(pPoseStack, 2, leftRowPos().slotBelow(2));
		this.addBaubleSlotBg(pPoseStack, 3, leftRowPos().slotBelow(3));
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.DARK);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
	}
	
	// All inventory slots are on the left, and expand the info box
	@Override
	public void addInfoBox(PoseStack poseStack)
	{
		//this.blit(poseStack, absPos(99, 17), GuiPos.valueOf(0, 183), GuiPos.valueOf(120, 72));
		this.drawSprite(poseStack, absPos(81, 17), GuiPos.valueOf(0, 183), GuiPos.valueOf(2, 72));
		this.drawSprite(poseStack, absPos(83, 17), GuiPos.valueOf(2, 183), GuiPos.valueOf(18, 72));
		this.drawSprite(poseStack, absPos(101, 17), GuiPos.valueOf(2, 183), GuiPos.valueOf(118, 72));
	}
	
	public MutableComponent getDefaultColorInfo()
	{
		LinearColor color = sg.getColorLinear();
		String colorKey = "color.dwmg.html." + HtmlColors.getNearestHtmlColor(color);
		return ComponentBuilder.create().appendTranslatable("info.dwmg.gui_slime_color").appendText(": ")
				.appendTranslatable(colorKey).build().withStyle(Style.EMPTY.withColor(color.toCode()));
	}
	
	public MutableComponent getColorRGBInfo()
	{
		LinearColor color = sg.getColorLinear();
		Vec3i rgb = color.toRGB();
		String rgbInfo = " (R" + Integer.toString(rgb.getX()) + ", G" + Integer.toString(rgb.getY()) + ", B" + Integer.toString(rgb.getZ()) +")";
		return InfoHelper.createText(rgbInfo).withStyle(Style.EMPTY.withColor(color.toCode()));
	}
	
	@Override
	public void addAttributeInfo(PoseStack poseStack, GuiPos position, int color, int textRowWidth)
	{
		this.addBasicAttributeInfo(poseStack, position, color, textRowWidth);
		position = position.addY(textRowWidth * 2);
		this.addFavorabilityAndLevelInfo(poseStack, position, color, textRowWidth);
	}
	
	@Override
	public void addBasicAttributeInfo(PoseStack poseStack, GuiPos position, int color, int textRowWidth)
	{
		GuiPos pos = position;
		String hp = Integer.toString(Math.round(mob.asMob().getHealth()));
		String maxHp = Long.toString(Math.round(mob.asMob().getAttributeValue(Attributes.MAX_HEALTH)));
		String atk = Long.toString(Math.round(mob.asMob().getAttributeValue(Attributes.ATTACK_DAMAGE)));
		String def = Long.toString(Math.round(mob.asMob().getAttributeValue(Attributes.ARMOR)));
		Component hpcomp = InfoHelper.createTranslatable("info.befriendmobs.gui_health")
				.append(InfoHelper.createText(": " + hp + " / " + maxHp));
		Component atkcomp = InfoHelper.createTranslatable("info.befriendmobs.gui_atk")
				.append(InfoHelper.createText(": " + atk));
		Component defcomp = InfoHelper.createTranslatable("info.befriendmobs.gui_armor")
				.append(InfoHelper.createText(": " + def));
		Component atkDefComp = ComponentBuilder.create().append(atkcomp).appendText(" | ")
				.append(defcomp).build();
		font.draw(poseStack, hpcomp, pos.x, pos.y, color);
		pos = pos.addY(textRowWidth);
		font.draw(poseStack, atkDefComp, pos.x, pos.y, color);
	}
	
	@Override
	public void addFavorabilityAndLevelInfo(PoseStack poseStack, GuiPos position, int color, int textRowWidth)
	{
		font.draw(poseStack, getDefaultLevelAndExpInfo(), position.x, position.y, color);
		position = position.addY(textRowWidth);
		font.draw(poseStack, getDefaultFavInfo(), position.x, position.y, color);
		position = position.addY(textRowWidth);
		font.draw(poseStack, getDefaultColorInfo(), position.x, position.y, color);
		position = position.addY(textRowWidth);
		font.draw(poseStack, getColorRGBInfo(), position.x, position.y, color);
	}

	@Override
	public GuiPos infoPos()
	{
		return absPos(85, 21);
	}

	
}
