package net.sodiumstudio.befriendmobs.client.gui.screens;

import java.util.HashMap;
import java.util.function.Function;

import net.minecraft.client.Minecraft;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class BefriendedGuiScreenMaker
{
	private static final HashMap<Class<? extends BefriendedInventoryMenu>, Function<BefriendedInventoryMenu, BefriendedGuiScreen>> TABLE
		= new HashMap<Class<? extends BefriendedInventoryMenu>, Function<BefriendedInventoryMenu, BefriendedGuiScreen>>();
	
	/**
	 * Put a method for making GUI from inventory menu for a given menu class.
	 */
	public static void put(Class<? extends BefriendedInventoryMenu> menuClass, Function<BefriendedInventoryMenu, BefriendedGuiScreen> makingMethod)
	{
		TABLE.put(menuClass, makingMethod);
	}
	
	/**
	 * Make GUI from class.
	 * <p> WARNING: Ensure this function is called only on client!
	 */
	public static  BefriendedGuiScreen make(BefriendedInventoryMenu menu)
	{
		if (TABLE.containsKey(menu.getClass()))
		{
			return TABLE.get(menu.getClass()).apply(menu);
		}
		else throw new IllegalArgumentException("BefriendedGuiScreenMaker::make missing method. Use BefriendedGuiScreenMaker.put() to register GUI making method before calling.");
	}
	
}
