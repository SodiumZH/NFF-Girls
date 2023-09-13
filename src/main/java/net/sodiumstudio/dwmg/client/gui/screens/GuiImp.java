package net.sodiumstudio.dwmg.client.gui.screens;

import net.minecraft.world.entity.player.Inventory;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.nautils.math.IntVec2;

public class GuiImp extends GuiHandItemsTwoBaubles
{
	
	public GuiImp(BefriendedInventoryMenu pMenu, Inventory pPlayerInventory, IBefriendedMob mob)
	{
		super(pMenu, pPlayerInventory, mob);
	}

	@Override
	public IntVec2 getMainHandIconPos()
	{
		return IntVec2.valueOf(3, 2);
	}
	
	@Override
	public IntVec2 getOffHandIconPos()
	{
		return IntVec2.valueOf(3, 4);
	}
}
