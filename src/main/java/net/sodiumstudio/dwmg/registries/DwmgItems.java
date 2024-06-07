package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.world.item.ModSwordItem;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.befriendmobs.item.MobCatcherItem;
import net.sodiumstudio.befriendmobs.item.MobRespawnerItem;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.DwmgTab;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
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
import net.sodiumstudio.dwmg.item.ReinforcedFishingRodItem;
import net.sodiumstudio.dwmg.item.TaoistTalismanItem;
import net.sodiumstudio.dwmg.item.TradeIntroductionLetterItem;
import net.sodiumstudio.dwmg.item.TransferringTagItem;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.AquaJadeBaubleItem;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.CourageAmuletBaubleItem;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.HealingJadeBaubleItem;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.LifeJadeBaubleItem;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.PoisonousThornBaubleItem;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.ResistanceAmuletBaubleItem;
import net.sodiumstudio.dwmg.subsystem.baublesystem.baubles.SoulAmuletBaubleItem;
import net.sodiumstudio.nautils.InfoHelper;

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
	
	
	// Ingredients
	public static final RegistryObject<Item> DEATH_CRYSTAL = regItem("death_crystal", new Item.Properties().rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> DEATH_CRYSTAL_POWDER = regItemDefault("death_crystal_powder");
	public static final RegistryObject<Item> SOUL_FLOUR = regItemDefault("soul_flour");
	public static final RegistryObject<Item> SOUL_CLOTH = regItemDefault("soul_cloth");
	public static final RegistryObject<Item> ENDER_FRUIT_JAM = regItem("ender_fruit_jam", new Item.Properties().rarity(Rarity.RARE).craftRemainder(Items.GLASS_BOTTLE));
	public static final RegistryObject<Item> EVIL_GEM = ITEMS.register("evil_gem", () -> new Item(new Item.Properties().tab(TAB)));

	// Foods
	public static final RegistryObject<Item> SOUL_CAKE_SLICE = regItem("soul_cake_slice", new Item.Properties().food(DwmgFoodProperties.SOUL_CAKE_SLICE).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<Item> ENDERBERRY = ITEMS.register("enderberry", () -> new ChorusFruitItem(new Item.Properties().tab(TAB).food(DwmgFoodProperties.ENDERBERRY).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> ENDER_PIE = ITEMS.register("ender_pie", () -> new Item(new Item.Properties().tab(TAB).food(DwmgFoodProperties.ENDER_PIE).rarity(Rarity.RARE)));
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
			"dwmg:soul_amulet", 2, new Item.Properties().rarity(Rarity.RARE).tab(TAB)).alwaysFoil()
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.soul_amulet").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+15").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+5").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<CourageAmuletBaubleItem> COURAGE_AMULET = ITEMS.register("courage_amulet", () -> new CourageAmuletBaubleItem(
			"dwmg:courage_amulet", 1, new Item.Properties().rarity(Rarity.UNCOMMON).tab(TAB))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.proactive_attack").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+4").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed", "+20%").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<CourageAmuletBaubleItem> COURAGE_AMULET_II = ITEMS.register("courage_amulet_ii", () -> new CourageAmuletBaubleItem(
			"dwmg:courage_amulet", 2, new Item.Properties().rarity(Rarity.RARE).tab(TAB)).alwaysFoil()
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.proactive_attack").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.atk", "+6").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed", "+30%").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<ResistanceAmuletBaubleItem> RESISTANCE_AMULET = ITEMS.register("resistance_amulet", () -> new ResistanceAmuletBaubleItem(
			"dwmg:resistance_amulet", 1, new Item.Properties().rarity(Rarity.UNCOMMON).tab(TAB))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.armor", "+4").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+15").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed", "-10%").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<ResistanceAmuletBaubleItem> RESISTANCE_AMULET_II = ITEMS.register("resistance_amulet_ii", () -> new ResistanceAmuletBaubleItem(
			"dwmg:resistance_amulet", 2, new Item.Properties().rarity(Rarity.UNCOMMON).tab(TAB)).alwaysFoil()
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.armor", "+6").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+25").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed", "-10%").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.sun_immune").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<HealingJadeBaubleItem> HEALING_JADE = ITEMS.register("healing_jade", () -> new HealingJadeBaubleItem(
			"dwmg:healing_jade", 1, new Item.Properties().rarity(Rarity.UNCOMMON).tab(TAB))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.healing_per_second", "0.1").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<LifeJadeBaubleItem> LIFE_JADE = ITEMS.register("life_jade", () -> new LifeJadeBaubleItem(
			"dwmg:life_jade", 1, new Item.Properties().rarity(Rarity.UNCOMMON).tab(TAB))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.healing_per_second", "0.15").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+5").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<LifeJadeBaubleItem> LIFE_JADE_II = ITEMS.register("life_jade_ii", () -> new LifeJadeBaubleItem(
			"dwmg:life_jade", 2, new Item.Properties().rarity(Rarity.RARE).tab(TAB)).alwaysFoil()
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.healing_per_second", "0.2").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.hpmax", "+10").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<AquaJadeBaubleItem> AQUA_JADE = ITEMS.register("aqua_jade", () -> new AquaJadeBaubleItem(
			"dwmg:aqua_jade", 2, new Item.Properties().rarity(Rarity.RARE).tab(TAB)).alwaysFoil()
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.aqua_jade").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.speed_in_water", "4x").withStyle(ChatFormatting.GRAY)).cast());
	public static final RegistryObject<PoisonousThornBaubleItem> POISONOUS_THORN = ITEMS.register("poisonous_thorn", () -> new PoisonousThornBaubleItem(
			"dwmg:poisonous_thorn", 1, new Item.Properties().rarity(Rarity.UNCOMMON).tab(TAB))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.poisonous_thorn").withStyle(ChatFormatting.GRAY))
			.description(InfoHelper.createTranslatable("info.dwmg.bauble.poisonous_thorn_1").withStyle(ChatFormatting.GRAY)).cast());


	
	// Equipment & tools
	public static final RegistryObject<Item> NECROMANCER_HAT = ITEMS.register("necromancer_hat", () -> new ItemNecromancerArmor(
			DwmgArmorMaterials.NECROMANCER,
			EquipmentSlot.HEAD,
			new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON)));
	public static final RegistryObject<Item> SUNHAT = ITEMS.register("sunhat", () -> new ArmorItem(
			DwmgArmorMaterials.SUNHAT,
			EquipmentSlot.HEAD,
			new Item.Properties().tab(TAB)));
	public static final RegistryObject<Item> NETHERITE_FORK = ITEMS.register("netherite_fork", () -> new ModSwordItem(Tiers.NETHERITE, 2.0F, -2.4F, new Item.Properties().tab(DwmgTab.TAB).fireResistant()));
	public static final RegistryObject<Item> NECROMANCER_WAND = ITEMS.register("necromancer_wand", () -> new ItemNecromancerWand(
			new Item.Properties().tab(TAB).rarity(Rarity.UNCOMMON).stacksTo(1)));
	public static final RegistryObject<Item> COMMANDING_WAND = ITEMS.register("commanding_wand", () -> new ItemCommandWand(new Item.Properties().tab(TAB).stacksTo(1)));
	public static final RegistryObject<Item> EVIL_MAGNET = ITEMS.register("evil_magnet", () -> new ItemEvilMagnet(new Item.Properties().tab(TAB).stacksTo(1)));
	// Utility items
	public static final RegistryObject<TransferringTagItem> TRANSFERRING_TAG = ITEMS.register("transferring_tag", () -> new TransferringTagItem(new Item.Properties().tab(TAB)));
	public static final RegistryObject<EmptyMagicalGelBottleItem> EMPTY_MAGICAL_GEL_BOTTLE = ITEMS.register("empty_magical_gel_bottle", () -> new EmptyMagicalGelBottleItem(new Item.Properties().tab(TAB)));
	public static final RegistryObject<MagicalGelBottleItem> MAGICAL_GEL_BOTTLE = ITEMS.register("magical_gel_bottle", () -> new MagicalGelBottleItem(new Item.Properties()));
	public static final RegistryObject<MagicalGelBallItem> MAGICAL_GEL_BALL = ITEMS.register("magical_gel_ball", () -> new MagicalGelBallItem(new Item.Properties().tab(TAB)));
	public static final RegistryObject<ReinforcedFishingRodItem> REINFORCED_FISHING_ROD = ITEMS.register("reinforced_fishing_rod", () -> new ReinforcedFishingRodItem(new Item.Properties().durability(256).tab(TAB)));
	public static final RegistryObject<TradeIntroductionLetterItem> TRADE_INTRODUCTION_LETTER = ITEMS.register("trade_introduction_letter",
			() -> new TradeIntroductionLetterItem(new Item.Properties().stacksTo(1)));
	
	// Misc
	public static final RegistryObject<MobRespawnerItem> MOB_RESPAWNER = ITEMS.register("mob_respawner", () -> new DwmgRespawnerItem(new Item.Properties()).setRetainBefriendedMobInventory(false));
	public static final RegistryObject<MobRespawnerItem> MOB_STORAGE_POD = ITEMS.register("mob_storage_pod", () -> new DwmgRespawnerItem(new Item.Properties()));
	public static final RegistryObject<MobCatcherItem> EMPTY_MOB_STORAGE_POD = ITEMS.register("empty_mob_storage_pod", () -> new MobCatcherItem(new Item.Properties().tab(TAB), MOB_STORAGE_POD.get()).canCatchCondition(
			((m, p) -> (m instanceof IDwmgBefriendedMob bm && bm.getOwnerUUID().equals(p.getUUID())))));
	
	
	// Technical
	public static final RegistryObject<Item> TAB_ICON = ITEMS.register("tab_icon", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> GIFT_UNKNOWN_ICON = ITEMS.register("gift_unknown_icon", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MOB_PROFILE_ICON = ITEMS.register("mob_profile_icon", () -> new Item(new Item.Properties()));
	
	// Debug
	public static final RegistryObject<BefriendingProgressProbeItem> BEFRIENDING_PROGRESS_PROBE = 
			ITEMS.register("befriending_progress_probe", () -> new BefriendingProgressProbeItem(new Item.Properties().rarity(Rarity.EPIC)));
	public static final RegistryObject<ExpModifierItem> EXP_MODIFIER = 
			ITEMS.register("exp_modifier", () -> new ExpModifierItem(new Item.Properties().rarity(Rarity.EPIC)));
	
	/* Item register end */
	
	public static void register(IEventBus eventBus){
	    ITEMS.register(eventBus);
	}
}
