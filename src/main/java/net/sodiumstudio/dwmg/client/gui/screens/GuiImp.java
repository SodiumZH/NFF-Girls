package net.sodiumstudio.dwmg.client.gui.screens;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.nautils.math.GuiPos;

public class GuiImp extends GuiHandItemsTwoBaubles
{
	
	public GuiImp(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory, IBefriendedMob mob)
	{
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	public GuiPos getMainHandIconPos()
	{
		return GuiPos.valueOf(3, 2);
	}
	
	@Override
	public GuiPos getOffHandIconPos()
	{
		return GuiPos.valueOf(3, 4);
	}
}
