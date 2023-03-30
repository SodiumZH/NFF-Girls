package net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.util.math.IntVec2;

public class GuiEnderExecutor extends GuiPreset_0 {

	public GuiEnderExecutor(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		IntVec2 v = IntVec2.of(i, j);
		// Main window

		this.blit(pPoseStack, v.x, v.y, 0, 0, imageWidth, imageHeight);

		v.add(7, 17);
		IntVec2 ab = IntVec2.of(imageWidth, 0); // Asset position base
		IntVec2 vf = ab.coord(2, 2); // for filled

		// Main hand
		v.addY(4);
		this.addSlotBg(pPoseStack, 0, v, ab.coord(1, 3), vf);
		// Off hand
		v.slotBelow().addY(4);
		this.addSlotBg(pPoseStack, 1, v, ab.coord(1, 2), vf);
		// Block
		v.slotBelow().addY(4);
		this.addSlotBg(pPoseStack, 2, v, ab.coord(0, 4), vf);
		
		// Baubles
		v.set(i, j).add(79, 17).addY(10);
		this.addSlotBg(pPoseStack, 3, v, ab.coord(1, 0), vf);	
		v.slotBelow().addY(10);
		this.addSlotBg(pPoseStack, 4, v, ab.coord(1, 0), vf);

		// Entity render background override
		this.blit(pPoseStack, i + 27, j + 17, 96, imageHeight, 50, 72);
		
		// Info box
		this.blit(pPoseStack, i + 99, j + 17, 0, imageHeight, 96, 72);
		addHealthInfo(pPoseStack, IntVec2.of(i + 102, j + 20));
		InventoryScreen.renderEntityInInventory(i + getEntityRenderPosition().x, j + getEntityRenderPosition().y,
				getMobRenderScale(), (float) (i + 52) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, mob.asMob());
	}
	
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
