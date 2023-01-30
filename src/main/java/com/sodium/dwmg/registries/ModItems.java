package com.sodium.dwmg.registries;

import com.github.mechalopa.hmag.world.item.ModFoodItem;
import com.sodium.dwmg.*;
import java.util.HashMap;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import com.sodium.dwmg.registries.*;

public class ModItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dwmg.MODID);
	
	public static final CreativeModeTab TAB = DwmgTab.TAB;
	// General register function for items
	
	// Register basic item with properties, not supporting item subclasses
	public static RegistryObject<Item> regItem(String name, Item.Properties properties)
	{
		return ITEMS.register(name, ()->new Item(properties.tab(TAB)));
	}
	
	// Register from item object, supporting item subclasses
	public static RegistryObject<Item> regItem(String name, Item item)
	{
		return ITEMS.register(name, ()->item);
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
   
	
	
	// Foods
	// public static final RegistryObject<Item> SOUL_CAKE = regItem("soul_cake", new Item(new Item.Properties().tab(TAB))); 
	
	
	
	
	/* Item register end */
	
	public static void register(IEventBus eventBus){
	    ITEMS.register(eventBus);
	}
}
