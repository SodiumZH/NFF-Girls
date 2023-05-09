package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.util.math.IntVec2;

public class GuiNecroticReaper extends GuiPreset_0
{
	public GuiNecroticReaper(BefriendedInventoryMenu menu, Inventory playerInventory, IBefriendedMob mob)
	{
		super(menu, playerInventory, mob);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		IntVec2 v = IntVec2.valueOf(i, j);
		// Main window

		this.blit(pPoseStack, v.x, v.y, 0, 0, imageWidth, imageHeight);

		IntVec2 ab = IntVec2.valueOf(imageWidth, 0); // Asset position base
		IntVec2 vf = ab.coord(2, 2); // for filled
		
		
		// Left column
		IntVec2 v1 = v.copy().add(7, 17);
		// Off hand
		v1.slotBelow(3);
		this.addSlotBg(pPoseStack, 1, v1, ab.coord(1, 4), vf);
		// Baubles
		v1.slotAbove(3);
		this.addSlotBg(pPoseStack, 2, v1, ab.coord(1, 0), vf);
		v1.slotBelow();
		this.addSlotBg(pPoseStack, 3, v1, ab.coord(1, 0), vf);
		
		
		// Right column
		// Baubles
		IntVec2 v2 = v.copy().add(79, 17);
		// Main hand
		v2.slotBelow(3);
		this.addSlotBg(pPoseStack, 0, v2, ab.coord(2, 4), vf);
		// Baubles
		v2.slotAbove(3);
		this.addSlotBg(pPoseStack, 4, v2, ab.coord(1, 0), vf);
		v2.slotBelow();
		this.addSlotBg(pPoseStack, 5, v2, ab.coord(1, 0), vf);

		// Entity render background override
		this.blit(pPoseStack, i + 27, j + 17, 96, imageHeight, 50, 72);
		
		// Info box
		this.blit(pPoseStack, i + 99, j + 17, 0, imageHeight, 96, 72);
		addAttributeInfo(pPoseStack, IntVec2.valueOf(i + 102, j + 20));
		InventoryScreen.renderEntityInInventory(i + getEntityRenderPosition().x, j + getEntityRenderPosition().y,
				getMobRenderScale(), (float) (i + 52) - this.xMouse, (float) (j + 75 - 50) - this.yMouse, mob.asMob());
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
