package net.sodiumstudio.dwmg.befriendmobs.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;

public class RegItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BefriendMobs.MOD_ID);
	
	public static final RegistryObject<Item> DEBUG_BEFRIENDER = ITEMS.register("debug_befriender", () -> new Item(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> EXAMPLE_ZOMBIE_BEFRIENDING_ITEM = ITEMS.register("example_zombie_befriending_item", () -> new Item(new Item.Properties()));

}
