package net.sodiumstudio.dwmg.registries;

import java.util.function.Supplier;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SimpleFoiledItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleItem;
import net.sodiumstudio.befriendmobs.util.InfoHelper;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.DwmgTab;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleItem;
import net.sodiumstudio.dwmg.item.ItemCommandWand;
import net.sodiumstudio.dwmg.item.ItemEvilMagnet;
import net.sodiumstudio.dwmg.item.ItemNecromancerArmor;
import net.sodiumstudio.dwmg.item.ItemNecromancerWand;

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
	
	public static DwmgBaubleItem newBauble(Item.Properties prop)
	{
		return new DwmgBaubleItem(prop.tab(TAB))
				.description(InfoHelper.createTrans("info.dwmg.bauble"));
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
	public static final RegistryObject<DwmgBaubleItem> SOUL_AMULET = ITEMS.register("soul_amulet", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("soul_amulet")
			.description(InfoHelper.createTrans("info.dwmg.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.hpmax", "+10").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.atk", "+3").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> SOUL_AMULET_II = ITEMS.register("soul_amulet_ii", () -> newBauble(new Item.Properties().rarity(Rarity.RARE)).foil()
			.typeName("soul_amulet").level(2)
			.description(InfoHelper.createTrans("info.dwmg.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.hpmax", "+15").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.atk", "+5").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> COURAGE_AMULET = ITEMS.register("courage_amulet", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("courage_amulet")
			.description(InfoHelper.createTrans("info.dwmg.bauble.proactive_attack").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.atk", "+4").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.speed", "+20%").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> COURAGE_AMULET_II = ITEMS.register("courage_amulet_ii", () -> newBauble(new Item.Properties().rarity(Rarity.RARE)).foil()
			.typeName("courage_amulet").level(2)
			.description(InfoHelper.createTrans("info.dwmg.bauble.proactive_attack").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.atk", "+6").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.speed", "+30%").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> RESISTANCE_AMULET = ITEMS.register("resistance_amulet", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("resistance_amulet")
			.description(InfoHelper.createTrans("info.dwmg.bauble.armor", "+4").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.speed", "-10%").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> RESISTANCE_AMULET_II = ITEMS.register("resistance_amulet_ii", () -> newBauble(new Item.Properties().rarity(Rarity.RARE)).foil()
			.typeName("resistance_amulet").level(2)
			.description(InfoHelper.createTrans("info.dwmg.bauble.armor", "+6").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.speed", "-10%").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> HEALING_JADE = ITEMS.register("healing_jade", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("healing_jade")
			.description(InfoHelper.createTrans("info.dwmg.bauble.healing_per_second", "0.1").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> LIFE_JADE = ITEMS.register("life_jade", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("life_jade")
			.description(InfoHelper.createTrans("info.dwmg.bauble.healing_per_second", "0.15").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.hpmax", "+5").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> LIFE_JADE_II = ITEMS.register("life_jade_ii", () -> newBauble(new Item.Properties().rarity(Rarity.RARE)).foil()
			.typeName("life_jade").level(2)
			.description(InfoHelper.createTrans("info.dwmg.bauble.healing_per_second", "0.2").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.hpmax", "+10").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> AQUA_JADE = ITEMS.register("aqua_jade", () -> newBauble(new Item.Properties().rarity(Rarity.RARE))
			.typeName("aqua_jade")
			.description(InfoHelper.createTrans("info.dwmg.bauble.aqua_jade").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.speed_in_water", "4x").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> POISONOUS_THORN = ITEMS.register("poisonous_thorn", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("poisonous_thorn")
			.description(InfoHelper.createTrans("info.dwmg.bauble.poisonous_thorn").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTrans("info.dwmg.bauble.poisonous_thorn_1").withStyle(ChatFormatting.GRAY)));


	
	// Equipment & tools
	public static final RegistryObject<Item> NECROMANCER_HAT = ITEMS.register("necromancer_hat", () -> new ItemNecromancerArmor(
			DwmgArmorMaterials.NECROMANCER,
			EquipmentSlot.HEAD,
			new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUNHAT = ITEMS.register("sunhat", () -> new ArmorItem(
			DwmgArmorMaterials.SUNHAT,
			EquipmentSlot.HEAD,
			new Item.Properties().tab(TAB)));
	public static final RegistryObject<Item> NECROMANCER_WAND = ITEMS.register("necromancer_wand", () -> new ItemNecromancerWand(
			new Item.Properties().tab(TAB).durability(64).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> COMMANDING_WAND = ITEMS.register("commanding_wand", () -> new ItemCommandWand(new Item.Properties().tab(TAB).stacksTo(1)));
	public static final RegistryObject<Item> EVIL_MAGNET = ITEMS.register("evil_magnet", () -> new ItemEvilMagnet(new Item.Properties().tab(TAB).stacksTo(1)));
			
	// Misc
	public static final RegistryObject<Item> TAB_ICON = ITEMS.register("tab_icon", ()->new Item(new Item.Properties()));
	
	/* Item register end */
	
	public static void register(IEventBus eventBus){
	    ITEMS.register(eventBus);
	}
}
