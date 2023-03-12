package net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.dwmg.befriendmobs.client.gui.screens.AbstractGuiBefriended;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.util.math.IntVec2;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;

/** GUI template for all vanilla-undead-mob-like befriended mobs.
*/

@OnlyIn(Dist.CLIENT)
public class GuiVanillaUndead extends AbstractGuiBefriended {
	
	@Override
	public ResourceLocation getTextureLocation() {
		return new ResourceLocation(Dwmg.MOD_ID,
			"textures/gui/container/bef_vanilla_undead.png");
	}
	
	public GuiVanillaUndead(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory, IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob, true);
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
	
	public int getScale()
	{
		return 25;
	}
	
	@Override
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
	   super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
	}

}

