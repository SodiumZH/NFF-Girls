package com.sodium.dwmg.registries;

import com.sodium.dwmg.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
	
	
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, DwMG.MOD_ID);
	
	
	public static final RegistryObject<Item> EXAMPLE_ITEM = ITEMS.register("example_item", ()->new Item
			(new Item.Properties().tab(DwMGTab.TAB)));
	

	
	public static void register(IEventBus eventBus){
	    ITEMS.register(eventBus);
	}
}
