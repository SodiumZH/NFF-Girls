package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.dwmgcontent.*;

public class DwmgItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dwmg.MOD_ID);
	
	//public static final CreativeModeTab TAB = DwmgTab.TAB;
	// General register function for items
	
	// Register basic item with properties, not supporting item subclasses
	public static RegistryObject<Item> regItem(String name, Item.Properties properties)
	{
		return ITEMS.register(name, ()->new Item(properties/*.tab(TAB)*/)); //Demos only
	}
	
	// Register basic item using default properties
	public static RegistryObject<Item> regItemDefault(String name)
	{
		return regItem(name, new Item.Properties());
	}

	
	/************************************/
	/* Item Registering, with constants */ 
	/************************************/	
	
	
	// Crafting intermediates
	public static final RegistryObject<Item> DEATH_CRYSTAL = regItemDefault("death_crystal");
	public static final RegistryObject<Item> DEATH_CRYSTAL_POWDER = regItemDefault("death_crystal_powder");
	public static final RegistryObject<Item> SOUL_FLOUR = regItemDefault("soul_flour");

	// Foods
	public static final RegistryObject<Item> SOUL_CAKE= regItemDefault("soul_cake"); // TO-DO: change it into a vanilla-cake-like block
	public static final RegistryObject<Item> SOUL_CAKE_SLICE = regItem("soul_cake_slice", new Item.Properties().food(DwmgFoodProperties.SOUL_CAKE_SLICE));
	
	// Debug items

	
	/* Item register end */
	
	public static void register(IEventBus eventBus){
	    ITEMS.register(eventBus);
	}
}
