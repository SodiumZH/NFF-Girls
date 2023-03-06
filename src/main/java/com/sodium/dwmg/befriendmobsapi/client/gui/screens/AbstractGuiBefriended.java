package com.sodium.dwmg.befriendmobsapi.client.gui.screens;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.inventory.AbstractInventoryMenuBefriended;

import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractGuiBefriended extends AbstractContainerScreen<AbstractInventoryMenuBefriended> {

	public IBefriendedMob mob;
	protected float xMouse = 0;
	protected float yMouse = 0;


	public abstract ResourceLocation getTextureLocation();
	
	public AbstractGuiBefriended(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob) {
		this(pMenu, pPlayerInventory, mob, false);
	}

	public AbstractGuiBefriended(AbstractInventoryMenuBefriended pMenu, Inventory pPlayerInventory,
			IBefriendedMob mob, boolean renderName)
	{
		super(pMenu, pPlayerInventory, renderName ? ((LivingEntity)mob).getDisplayName() : new TextComponent(""));
		this.mob = mob;
		this.passEvents = false;
	}
	
	
	
	
	// Background, mouse XY and tooltip are rendered here. Do not render them again in subclasses.
	public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
		this.renderBackground(pPoseStack);
		this.xMouse = (float) pMouseX;
		this.yMouse = (float) pMouseY;
		super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
		this.renderTooltip(pPoseStack, pMouseX, pMouseY);
	}
	
	
	
	public void renderName(int posX, int posY)
	{
		
	}
}
