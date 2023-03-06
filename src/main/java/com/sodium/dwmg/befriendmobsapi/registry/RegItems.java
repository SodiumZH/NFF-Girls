package com.sodium.dwmg.befriendmobsapi.registry;

import com.sodium.dwmg.befriendmobsapi.BefriendMobsAPI;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class RegItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BefriendMobsAPI.MOD_ID);
	
	public static final RegistryObject<Item> DEBUG_BEFRIENDER = ITEMS.register("debug_befriender", () -> new Item(new Item.Properties()));

}
