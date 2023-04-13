 package net.sodiumstudio.dwmg.befriendmobs.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.item.ItemMobRespawner;

public class BefMobItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, BefriendMobs.modDomain());
	
	public static final RegistryObject<Item> DEBUG_BEFRIENDER = ITEMS.register("debug_befriender", () -> new Item(new Item.Properties().stacksTo(1)));
	//public static final RegistryObject<Item> EXAMPLE_ZOMBIE_BEFRIENDING_ITEM = ITEMS.register("example_zombie_befriending_item", () -> new Item(new Item.Properties()));
	//public static final RegistryObject<Item> DEBUG_ARMOR_GIVER = ITEMS.register("debug_armor_giver", () -> new Item(new Item.Properties().stacksTo(1)));
	//public static final RegistryObject<Item> DEBUG_TARGET_SETTER = ITEMS.register("debug_target_setter", () -> new Item(new Item.Properties().stacksTo(1)));
	//public static final RegistryObject<Item> DEBUG_MOB_CONVERTER = ITEMS.register("debug_mob_converter", () -> new Item(new Item.Properties().stacksTo(1)));
	//public static final RegistryObject<Item> DEBUG_ATTRIBUTE_CHECKER = ITEMS.register("debug_attribute_checker", () -> new Item(new Item.Properties().stacksTo(1)));

	// A fake item for occupying a stack to enable/block something
	public static final RegistryObject<Item> DUMMY_ITEM = ITEMS.register("dummy_item", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MOB_RESPAWNER = ITEMS.register("mob_respawner", () -> new ItemMobRespawner(new Item.Properties().stacksTo(1)));
}
