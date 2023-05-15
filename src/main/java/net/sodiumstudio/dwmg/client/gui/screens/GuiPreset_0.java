package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreen;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.util.InfoHelper;
import net.sodiumstudio.befriendmobs.util.math.IntVec2;
import net.sodiumstudio.dwmg.Dwmg;


/** GUI template for all vanilla-undead-mob-like befriended mobs.
*/

@OnlyIn(Dist.CLIENT)
public class GuiPreset_0 extends BefriendedGuiScreen {
	
	@Override
	public ResourceLocation getTextureLocation() {
		return new ResourceLocation(Dwmg.MOD_ID,
			"textures/gui/container/gui_preset_0.png");
	}
	
	public GuiPreset_0(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob) {
		super(menu, playerInventory, mob, true);
		imageWidth = 200;
		imageHeight = 183;
		inventoryLabelY = imageHeight - 93;
	}

	@Override
	protected void init() {
		super.init();
		//this.minecraft.keyboardHandler.setSendRepeatsToGui(true);
	}

	public IntVec2 getEntityRenderPosition()
	{
		return new IntVec2(52, 80);
	}
	
	public int getMobRenderScale()
	{
		return 25;
	}
	
	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTick) {
		super.render(poseStack, mouseX, mouseY, partialTick);
	}

	public void renderItemSlots()
	{
		
	}
	
	@Override
	public void addAttributeInfo(PoseStack poseStack, IntVec2 position, int color, int textRowWidth)
	{
		super.addAttributeInfo(poseStack, position, color, textRowWidth);
		IntVec2 pos = position.copy().addY(textRowWidth * 3);

		IDwmgBefriendedMob bm = (IDwmgBefriendedMob)mob;		
		String lv = Integer.toString(bm.getLevelHandler().getExpectedLevel());
		String exp = Long.toString(bm.getLevelHandler().getExpInThisLevel());
		String expup = Long.toString(bm.getLevelHandler().getRequiredExpInThisLevel());
		String fav = Integer.toString(Math.round(bm.getFavorability().getFavorability()));
		String favmax = Integer.toString(Math.round(bm.getFavorability().getMaxFavorability()));
		
		MutableComponent lvcomp = InfoHelper.createTrans("info.dwmg.gui_level")
				.append(InfoHelper.createText(": " + lv));	
		MutableComponent expcomp = InfoHelper.createTrans("info.dwmg.gui_exp")
				.append(InfoHelper.createText(": " + exp + " / " + expup));
		MutableComponent favcomp = InfoHelper.createTrans("info.dwmg.gui_favorability")
				.append(InfoHelper.createText(": " + fav + " / " + favmax));
		font.draw(poseStack, lvcomp, pos.x, pos.y, color);
		pos.addY(textRowWidth);
		font.draw(poseStack, expcomp, pos.x, pos.y, color);
		pos.addY(textRowWidth);
		font.draw(poseStack, favcomp, pos.x, pos.y, color);
	}
	
	@Override
	public void addAttributeInfo(PoseStack poseStack, IntVec2 position)
	{
		addAttributeInfo(poseStack, position, 0x404040, 11);
	}
}

