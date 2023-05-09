package net.sodiumstudio.dwmg.registries;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.DwmgTab;
import net.sodiumstudio.dwmg.item.ItemNecromancerArmor;
import net.sodiumstudio.dwmg.item.NecromancerWandItem;

public class DwmgItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dwmg.MOD_ID);
	
	public static final CreativeModeTab TAB = DwmgTab.TAB;
	// General register function for items
	
	// Register basic item with properties, not supporting item subclasses
	public static RegistryObject<Item> regItem(String name, Item.Properties properties)
	{
		return ITEMS.register(name, ()->new Item(properties.tab(TAB))); //Demos only
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
	public static final RegistryObject<Item> DEATH_CRYSTAL = regItem("death_crystal", new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> DEATH_CRYSTAL_POWDER = regItemDefault("death_crystal_powder");
	public static final RegistryObject<Item> SOUL_FLOUR = regItemDefault("soul_flour");
	public static final RegistryObject<Item> SOUL_CLOTH = regItemDefault("soul_cloth");
	public static final RegistryObject<Item> ENDER_FRUIT_JAM = regItem("ender_fruit_jam", new Item.Properties().rarity(Rarity.RARE));
	// Foods
	public static final RegistryObject<Item> SOUL_CAKE_SLICE = regItem("soul_cake_slice", new Item.Properties().food(DwmgFoodProperties.SOUL_CAKE_SLICE).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> ENDERBERRY = ITEMS.register("enderberry", () -> new ChorusFruitItem(new Item.Properties().tab(TAB).food(DwmgFoodProperties.ENDERBERRY).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ENDER_PIE = ITEMS.register("ender_pie", () -> new Item(new Item.Properties().tab(TAB).food(DwmgFoodProperties.ENDER_PIE).rarity(Rarity.RARE)));
	// Baubles
	public static final RegistryObject<Item> SOUL_AMULET = regItem("soul_amulet", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> RESISTANCE_AMULET = regItem("resistance_amulet", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> HEALING_JADE = regItem("healing_jade", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> AQUA_JADE = regItem("aqua_jade", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> POISONOUS_THORN = regItem("poisonous_thorn", new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	// Equipment
	public static final RegistryObject<Item> NECROMANCER_HAT = ITEMS.register("necromancer_hat", () -> new ItemNecromancerArmor(
			DwmgArmorMaterials.NECROMANCER,
			EquipmentSlot.HEAD,
			new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUNHAT = ITEMS.register("sunhat", () -> new ArmorItem(
			DwmgArmorMaterials.SUNHAT,
			EquipmentSlot.HEAD,
			new Item.Properties().tab(TAB)));
	public static final RegistryObject<Item> NECROMANCER_WAND = ITEMS.register("necromancer_wand", () -> new NecromancerWandItem(
			new Item.Properties().tab(TAB).durability(64).rarity(Rarity.UNCOMMON)));
	
	/*
	public static final RegistryObject<Item> INSOMNIA_HELMET = ITEMS.register("insomnia_helmet", () -> new ArmorItem(
			ArmorMaterials.LEATHER,
			EquipmentSlot.HEAD,
			new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> INSOMNIA_CHESTPLATE = ITEMS.register("insomnia_chestplate", () -> new ArmorItem(
			ArmorMaterials.LEATHER,
			EquipmentSlot.CHEST,
			new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> INSOMNIA_LEGGINGS = ITEMS.register("insomnia_leggings", () -> new ArmorItem(
			ArmorMaterials.LEATHER,
			EquipmentSlot.LEGS,
			new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> INSOMNIA_BOOTS = ITEMS.register("insomnia_boots", () -> new ArmorItem(
			ArmorMaterials.LEATHER,
			EquipmentSlot.FEET,
			new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON)));*/
	//
			
	/* Item register end */
	
	public static void register(IEventBus eventBus){
	    ITEMS.register(eventBus);
	}
}
