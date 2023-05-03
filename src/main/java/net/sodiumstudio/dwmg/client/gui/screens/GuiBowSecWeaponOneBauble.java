package net.sodiumstudio.dwmg.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.util.math.IntVec2;

// Gui for mobs with bow shooting attack + secondary weapon + one bouble
public class GuiBowSecWeaponOneBauble extends GuiPreset_0 {

	public GuiBowSecWeaponOneBauble(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
		super.renderBg(pPoseStack, pPartialTick, pMouseX, pMouseY);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		IntVec2 v = IntVec2.valueOf(i, j);
		// Main window
		
		this.blit(pPoseStack, v.x, v.y, 0, 0, imageWidth, imageHeight);	
		// Armor slots
		v.add(7, 17);
		IntVec2 ab = IntVec2.valueOf(imageWidth, 0);	// asset array base
		IntVec2 vf = IntVec2.valueOf(imageWidth, 0).coord(2, 2);	//for filled
		for (int k = 0; k < 4; ++k)
		{
			this.addSlotBg(pPoseStack, k, v, ab.coord(0, k), vf);
			v.slotBelow();
		}
		
		// Main hand
		v.set(i, j).add(79, 17).slotBelow(3);
		this.addSlotBg(pPoseStack, 4, v, ab.coord(1, 1), vf);
		// Off hand
		v.slotAbove();
		this.addSlotBg(pPoseStack, 5, v, ab.coord(1, 2), vf);
		// Bauble
		v.slotAbove(2);
		this.addSlotBg(pPoseStack, 6, v, ab.coord(1, 0), vf);
		// Backup weapon
		v.slotBelow();
		this.addSlotBg(pPoseStack, 7, v, ab.coord(2, 1), vf);
		// Info box
		this.blit(pPoseStack, i + 99, j + 17, 0, imageHeight, 96, 72);
		
		// Arrows slot (above info box)
		v.slotBelow(2).slotRight().addX(2);
		this.addSlotBg(pPoseStack, 8, v, ab.coord(2, 0), vf);

		addAttributeInfo(pPoseStack, IntVec2.valueOf(i + 102, j + 20));
		InventoryScreen.renderEntityInInventory(i + 52, j + 80, 25, (float) (i + 52) - this.xMouse,
				(float) (j + 75 - 50) - this.yMouse, (LivingEntity)mob);		

	}

}