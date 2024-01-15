package net.sodiumstudio.dwmg.registries;

import java.util.HashSet;
import java.util.function.Supplier;

import com.github.mechalopa.hmag.world.item.ModSwordItem;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.item.NaUtilsItem;
import net.sodiumstudio.befriendmobs.item.MobCatcherItem;
import net.sodiumstudio.befriendmobs.item.MobRespawnerItem;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleItem;
import net.sodiumstudio.dwmg.item.BefriendingProgressProbeItem;
import net.sodiumstudio.dwmg.item.DwmgRespawnerItem;
import net.sodiumstudio.dwmg.item.EmptyMagicalGelBottleItem;
import net.sodiumstudio.dwmg.item.ExpModifierItem;
import net.sodiumstudio.dwmg.item.ItemCommandWand;
import net.sodiumstudio.dwmg.item.ItemEvilMagnet;
import net.sodiumstudio.dwmg.item.ItemNecromancerArmor;
import net.sodiumstudio.dwmg.item.ItemNecromancerWand;
import net.sodiumstudio.dwmg.item.MagicalGelBallItem;
import net.sodiumstudio.dwmg.item.MagicalGelBottleItem;
import net.sodiumstudio.dwmg.item.PeachWoodSwordItem;
import net.sodiumstudio.dwmg.item.ReinforcedFishingRodItem;
import net.sodiumstudio.dwmg.item.TaoistTalismanItem;
import net.sodiumstudio.dwmg.item.TransferringTagItem;
import net.sodiumstudio.dwmg.item.UnsweepableSwordItem;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.SoulAmuletBaubleItem;

public class DwmgItems {
	
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Dwmg.MOD_ID);
	public static final HashSet<RegistryObject<? extends Item>> NO_TAB = new HashSet<>();
	
	// General register function for items
	
	/** 
	 * Register basic item with properties, not supporting item subclasses
	 * @deprecated use {@code registerItem} instead
	 */
	@Deprecated
	public static RegistryObject<Item> regItem(String name, Item.Properties properties)
	{
		return ITEMS.register(name, ()->new Item(properties)); //Demos only
	}

	
	public static <T extends Item> RegistryObject<T> registerItem(String name, Class<T> clazz, boolean tab, Supplier<T> supplier)
	{
		var res = ITEMS.register(name, supplier);
		if (tab)
			NO_TAB.add(res);
		return res;
	}
	
	public static <T extends Item> RegistryObject<T> registerItem(String name, Class<T> clazz, Supplier<T> supplier)
	{
		return registerItem(name, clazz, true, supplier);
	}
	
	/**
	 * Register an item using default constructor (with Item.Properties).
	 * @param name Registry name.
	 * @param properties Init properties.
	 * @param clazz Item class.
	 * @param tab If true, it will be added to the creative tab.
	 * @return Registry object.
	 */
	public static <T extends Item> RegistryObject<T> registerItem(String name, Item.Properties properties, Class<T> clazz, boolean tab)
	{
		var res = ITEMS.register(name, () ->
		{
			try
			{
				return clazz.getConstructor(Item.Properties.class).newInstance(properties);

			} catch (Exception e)
			{
				e.printStackTrace();
				return null;
			}
		});
		if (!tab)
			NO_TAB.add(res);
		return res;

	}
	
	/**
	 * Register an item and add to the creative tab.
	 * @param name Registry name.
	 * @param properties Init properties.
	 * @param clazz Item class.
	 * @return Registry object.
	 */
	public static <T extends Item> RegistryObject<T> registerItem(String name, Item.Properties properties, Class<T> clazz)
	{
		return registerItem(name, properties, clazz, true);
	}
	
	/**
	 * Register a default item and add to the creative tab.
	 * @param name Registry name.
	 * @param properties Init properties.
	 * @return Registry object.
	 */
	public static RegistryObject<Item> registerItem(String name, Item.Properties properties)
	{
		return registerItem(name, properties, Item.class);
	}
	
	/**
	 * Register a default item with default properties and add to the creative tab.
	 * @param name Registry name.
	 * @return Registry object.
	 */
	public static RegistryObject<Item> registerItem(String name)
	{
		return registerItem(name, new Item.Properties());
	}
	
	// Register basic item using default properties
	@Deprecated
	public static RegistryObject<Item> regItemDefault(String name)
	{
		return regItem(name, new Item.Properties());
	}
	
	@SuppressWarnings("unchecked")
	public static <T extends DwmgBaubleItem> T newBauble(Class<T> clazz, Item.Properties prop)
	{
		try
		{
			return (T)(clazz.getConstructor(Item.Properties.class).newInstance(prop)
					.description(InfoHelper.createTrans("info.dwmg.bauble")));
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@Deprecated
	public static DwmgBaubleItem newBauble(Item.Properties prop)
	{
		return new DwmgBaubleItem(prop.tab(TAB))
				.description(InfoHelper.createTranslatable("info.dwmg.bauble"));
	}
	
	public static <T extends DwmgBaubleItem> RegistryObject<T> registerBauble(String registryName, Supplier<T> supplier)
	{
		return ITEMS.register(registryName, supplier);
	}
	
	/************************************/
	/* Item Registering, with constants */ 
	/************************************/	
	
	
	// Crafting intermediates
	public static final RegistryObject<Item> DEATH_CRYSTAL = registerItem("death_crystal", new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> DEATH_CRYSTAL_POWDER = registerItem("death_crystal_powder");
	public static final RegistryObject<Item> SOUL_FLOUR = registerItem("soul_flour");
	public static final RegistryObject<Item> SOUL_CLOTH = registerItem("soul_cloth");
	public static final RegistryObject<Item> ENDER_FRUIT_JAM = registerItem("ender_fruit_jam", new Item.Properties().rarity(Rarity.RARE));

	// Foods
	public static final RegistryObject<Item> SOUL_CAKE_SLICE = registerItem("soul_cake_slice", new Item.Properties().food(DwmgFoodProperties.SOUL_CAKE_SLICE).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> ENDERBERRY = ITEMS.register("enderberry", () -> new ChorusFruitItem(new Item.Properties().food(DwmgFoodProperties.ENDERBERRY).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ENDER_PIE = ITEMS.register("ender_pie", () -> new Item(new Item.Properties().food(DwmgFoodProperties.ENDER_PIE).rarity(Rarity.RARE)));
	// Baubles
	/*public static final RegistryObject<DwmgBaubleItem> SOUL_AMULET = ITEMS.register("soul_amulet", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("soul_amulet")
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+10").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+3").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> SOUL_AMULET_II = ITEMS.register("soul_amulet_ii", () -> newBauble(new Item.Properties().rarity(Rarity.RARE)).foil()
			.typeName("soul_amulet").level(2)
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+15").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+5").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)));*/
	public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET = ITEMS.register("soul_amulet", () -> new SoulAmuletBaubleItem(
			"dwmg:soul_amulet", 1, new Item.Properties().rarity(Rarity.UNCOMMON).tab(TAB))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+10").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+3").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<SoulAmuletBaubleItem> SOUL_AMULET_II = ITEMS.register("soul_amulet_ii", () -> new SoulAmuletBaubleItem(
			"dwmg:soul_amulet", 2, new Item.Properties().rarity(Rarity.RARE).tab(TAB))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+15").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+5").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<DwmgBaubleItem> COURAGE_AMULET = ITEMS.register("courage_amulet", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("courage_amulet")
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.proactive_attack").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+4").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed", "+20%").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> COURAGE_AMULET_II = ITEMS.register("courage_amulet_ii", () -> newBauble(new Item.Properties().rarity(Rarity.RARE)).foil()
			.typeName("courage_amulet").level(2)
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.proactive_attack").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+6").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed", "+30%").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> RESISTANCE_AMULET = ITEMS.register("resistance_amulet", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("resistance_amulet")
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.armor", "+4").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed", "-10%").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> RESISTANCE_AMULET_II = ITEMS.register("resistance_amulet_ii", () -> newBauble(new Item.Properties().rarity(Rarity.RARE)).foil()
			.typeName("resistance_amulet").level(2)
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.armor", "+6").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed", "-10%").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> HEALING_JADE = ITEMS.register("healing_jade", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("healing_jade")
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.healing_per_second", "0.1").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> LIFE_JADE = ITEMS.register("life_jade", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("life_jade")
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.healing_per_second", "0.15").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+5").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> LIFE_JADE_II = ITEMS.register("life_jade_ii", () -> newBauble(new Item.Properties().rarity(Rarity.RARE)).foil()
			.typeName("life_jade").level(2)
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.healing_per_second", "0.2").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+10").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> AQUA_JADE = ITEMS.register("aqua_jade", () -> newBauble(new Item.Properties().rarity(Rarity.RARE))
			.typeName("aqua_jade")
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.aqua_jade").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed_in_water", "4x").withStyle(ChatFormatting.GRAY)));
	public static final RegistryObject<DwmgBaubleItem> POISONOUS_THORN = ITEMS.register("poisonous_thorn", () -> newBauble(new Item.Properties().rarity(Rarity.UNCOMMON))
			.typeName("poisonous_thorn")
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.poisonous_thorn").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.poisonous_thorn_1").withStyle(ChatFormatting.GRAY)));


	
	// Equipment & tools
	public static final RegistryObject<Item> NECROMANCER_HAT = ITEMS.register("necromancer_hat", () -> new ItemNecromancerArmor(
			DwmgArmorMaterials.NECROMANCER,
			EquipmentSlot.HEAD,
			new Item.Properties().rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUNHAT = ITEMS.register("sunhat", () -> new ArmorItem(
			DwmgArmorMaterials.SUNHAT,
			ArmorItem.Type.HELMET, 
			new Item.Properties()));
	public static final RegistryObject<Item> NETHERITE_FORK = ITEMS.register("netherite_fork", () -> new ModSwordItem(Tiers.NETHERITE, 2.0F, -2.4F, new Item.Properties().fireResistant()));
	public static final RegistryObject<Item> NECROMANCER_WAND = ITEMS.register("necromancer_wand", () -> new ItemNecromancerWand(
			new Item.Properties().rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final RegistryObject<Item> COMMANDING_WAND = ITEMS.register("commanding_wand", () -> new ItemCommandWand(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<Item> EVIL_MAGNET = ITEMS.register("evil_magnet", () -> new ItemEvilMagnet(new Item.Properties().stacksTo(1)));
	public static final RegistryObject<SwordItem> PEACH_WOOD_SWORD = ITEMS.register("peach_wood_sword", () -> new PeachWoodSwordItem(Tiers.WOOD, 3, -2.4F, (new Item.Properties()).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<ReinforcedFishingRodItem> REINFORCED_FISHING_ROD = ITEMS.register("reinforced_fishing_rod", () -> new ReinforcedFishingRodItem(new Item.Properties().durability(256)));
	
	// Utility items
	public static final RegistryObject<TransferringTagItem> TRANSFERRING_TAG = ITEMS.register("transferring_tag", () -> new TransferringTagItem(new Item.Properties()));
	public static final RegistryObject<EmptyMagicalGelBottleItem> EMPTY_MAGICAL_GEL_BOTTLE = ITEMS.register("empty_magical_gel_bottle", () -> new EmptyMagicalGelBottleItem(new Item.Properties()));
	public static final RegistryObject<MagicalGelBallItem> MAGICAL_GEL_BALL = ITEMS.register("magical_gel_ball", () -> new MagicalGelBallItem(new Item.Properties()));
	public static final RegistryObject<MagicalGelBottleItem> MAGICAL_GEL_BOTTLE = ITEMS.register("magical_gel_bottle", () -> new MagicalGelBottleItem(new Item.Properties()));
	public static final RegistryObject<Item> TAOIST_TALISMAN = ITEMS.register("taoist_talisman", () -> new TaoistTalismanItem(new Item.Properties()));
	
	// Misc
	public static final RegistryObject<MobRespawnerItem> MOB_RESPAWNER = ITEMS.register("mob_respawner", () -> new DwmgRespawnerItem(new Item.Properties()).setRetainBefriendedMobInventory(false));
	public static final RegistryObject<MobRespawnerItem> MOB_STORAGE_POD = ITEMS.register("mob_storage_pod", () -> new DwmgRespawnerItem(new Item.Properties()));
	public static final RegistryObject<MobCatcherItem> EMPTY_MOB_STORAGE_POD = ITEMS.register("empty_mob_storage_pod", () -> new MobCatcherItem(new Item.Properties(), MOB_STORAGE_POD.get()).canCatchCondition(
			((m, p) -> (m instanceof IDwmgBefriendedMob bm && bm.getOwnerUUID().equals(p.getUUID())))));

	public static final RegistryObject<Item> TAB_ICON = ITEMS.register("tab_icon", () -> new Item(new Item.Properties()));
	
	// Debug
	public static final RegistryObject<BefriendingProgressProbeItem> BEFRIENDING_PROGRESS_PROBE = 
			ITEMS.register("befriending_progress_probe", () -> new BefriendingProgressProbeItem(new Item.Properties()));
	public static final RegistryObject<ExpModifierItem> EXP_MODIFIER = 
			ITEMS.register("exp_modifier", () -> new ExpModifierItem(new Item.Properties()));
	
	static
	{
		NO_TAB.add(MOB_RESPAWNER);
		NO_TAB.add(MOB_STORAGE_POD);
		NO_TAB.add(TAB_ICON);
		NO_TAB.add(BEFRIENDING_PROGRESS_PROBE);
    }

	/* Item register end */
	
	public static void register(IEventBus eventBus){
	    ITEMS.register(eventBus);
	}

}
