package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.entities.hmag.HmagMeltyMonsterEntity;
import net.sodiumstudio.nautils.NaMiscUtils;
import net.sodiumstudio.nautils.info.ComponentBuilder;
import net.sodiumstudio.nautils.math.IntVec2;

public class GuiMeltyMonster extends GuiPreset0 {
	
	public GuiMeltyMonster(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(GuiGraphics graphics, float pPartialTick, int pMouseX, int pMouseY) {			
		super.renderBg(graphics, pPartialTick, pMouseX, pMouseY);
		this.addMainScreen(graphics);
		this.addBaubleSlotBg(graphics, 0, leftRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 1, leftRowPos().slotBelow(1).addY(8));
		this.addBaubleSlotBg(graphics, 2, rightRowPos().addY(4));
		this.addBaubleSlotBg(graphics, 3, rightRowPos().slotBelow().addY(8));
		this.addSlotBg(graphics, 4, leftRowPos().slotBelow(2).addY(12), 4, 2);
		this.addMobRenderBox(graphics, MobRenderBoxStyle.DARK);
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
	
	public void addStaminaInfo(GuiGraphics graphics, IntVec2 position, int color, int textRowWidth)
	{
		Component staminaComp = ComponentBuilder.create().appendTranslatable("info.dwmg.gui_melty_monster_stamina")
				.appendText(": ").appendText(Integer.toString(NaMiscUtils.cast(mob, HmagMeltyMonsterEntity.class).getStamina())).appendText(" / ")
				.appendText(Integer.toString(NaMiscUtils.cast(mob, HmagMeltyMonsterEntity.class).getMaxStamina())).build();
		graphics.drawString(font, staminaComp, position.x, position.y, color);

	}
	
	@Override
	public void addAttributeInfo(GuiGraphics graphics, IntVec2 position, int color, int textRowWidth)
	{
		this.addBasicAttributeInfo(graphics, position, color, textRowWidth);
		position.addY(textRowWidth * 3);
		this.addStaminaInfo(graphics, position, color, textRowWidth);
		position.addY(textRowWidth);
		this.addFavorabilityAndLevelInfo(graphics, position, color, textRowWidth);
	}
	
	@Override
	public void addAttributeInfo(GuiGraphics graphics, IntVec2 position)
	{
		addAttributeInfo(graphics, position, 0x404040, 9);
	}
	
}
