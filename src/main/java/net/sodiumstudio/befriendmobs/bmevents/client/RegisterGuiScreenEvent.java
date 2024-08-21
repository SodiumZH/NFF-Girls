package net.sodiumstudio.befriendmobs.bmevents.client;

import java.util.function.Function;

import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreen;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreenMaker;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;

public class RegisterGuiScreenEvent extends Event implements IModBusEvent
{
	public RegisterGuiScreenEvent() {}
	
	/**
	 * Register using simple mapping from menu to screen. 
	 * Also for GUI classes with constructor with one parameter {@code BefriendedInventoryMenu}.
	 * @param menuClass
	 * @param prvd
	 */
	public void register(Class<? extends BefriendedInventoryMenu> menuClass, Function<BefriendedInventoryMenu, BefriendedGuiScreen> guiFunction)
	{
		BefriendedGuiScreenMaker.put(menuClass, guiFunction);
	}
	
	/**
	 * Register using constructor with parameters {@code BefriendedInventoryMenu, Inventory, IBefriendedMob}.
	 * This is a simplification for GUI classes with such constructor above and you'll be able to use {@code YourClass::new}.
	 * <p> Note: If registered in this style, it will invoke {@code new YourClass(menu, menu.playerInventory, menu.mob)} for construction.
	 * If you don't want this, use {@code register} instead and manually input the parameters.
	 * @param menuClass
	 * @param prvd
	 */
	public void registerDefault(Class<? extends BefriendedInventoryMenu> menuClass, Provider prvd)
	{
		this.register(menuClass, (menu) -> prvd.create(menu, menu.playerInventory, menu.mob));
	}
	
	@FunctionalInterface
	public static interface Provider
	{
		BefriendedGuiScreen create(BefriendedInventoryMenu menu, Inventory inv, IBefriendedMob mob);
	}
	
}
