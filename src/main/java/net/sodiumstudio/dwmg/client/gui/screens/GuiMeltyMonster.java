package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.entities.hmag.HmagMeltyMonsterEntity;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.MiscUtil;
import net.sodiumstudio.nautils.math.IntVec2;

public class GuiMeltyMonster extends GuiPreset0 {
	
	public GuiMeltyMonster(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(pPoseStack);
		this.addBaubleSlotBg(pPoseStack, 0, leftRowPos().addY(4));
		this.addBaubleSlotBg(pPoseStack, 1, leftRowPos().slotBelow(1).addY(8));
		this.addBaubleSlotBg(pPoseStack, 2, rightRowPos().addY(4));
		this.addBaubleSlotBg(pPoseStack, 3, rightRowPos().slotBelow().addY(8));
		this.addSlotBg(pPoseStack, 4, leftRowPos().slotBelow(2).addY(12), 4, 2);
		this.addMobRenderBox(pPoseStack, MobRenderBoxStyle.DARK);
		this.addInfoBox(pPoseStack);
		this.addAttributeInfo(pPoseStack, infoPos());
		this.renderMob();
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
	
	public void addStaminaInfo(PoseStack poseStack, IntVec2 position, int color, int textRowWidth)
	{
		Component staminaComp = InfoHelper.builder().putTrans("info.dwmg.gui_melty_monster_stamina")
				.putText(": ").putText(Integer.toString(MiscUtil.cast(mob, HmagMeltyMonsterEntity.class).getStamina())).putText(" / ")
				.putText(Integer.toString(MiscUtil.cast(mob, HmagMeltyMonsterEntity.class).getMaxStamina())).build();
		font.draw(poseStack, staminaComp, position.x, position.y, color);
	}
	
	@Override
	public void addAttributeInfo(PoseStack poseStack, IntVec2 position, int color, int textRowWidth)
	{
		this.addBasicAttributeInfo(poseStack, position, color, textRowWidth);
		position.addY(textRowWidth * 3);
		this.addStaminaInfo(poseStack, position, color, textRowWidth);
		position.addY(textRowWidth);
		this.addFavorabilityAndLevelInfo(poseStack, position, color, textRowWidth);
	}
	
	@Override
	public void addAttributeInfo(PoseStack poseStack, IntVec2 position)
	{
		addAttributeInfo(poseStack, position, 0x404040, 9);
	}
	
}