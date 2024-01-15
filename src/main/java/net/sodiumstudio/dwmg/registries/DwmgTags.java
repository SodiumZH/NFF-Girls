package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.HMaG;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.nautils.TagHelper;

public class DwmgTags
{
	public static final TagKey<Block> AFFECTS_CRIMSON_SLAUGHTERER = TagHelper.createBlockTag(
			Dwmg.MOD_ID, "affects_crimson_slaughterer");
	public static final TagKey<Block> NIGHTWALKER_MAGIC_BALL_AFFECTS = TagHelper.createBlockTag
			(Dwmg.MOD_ID, "nightwalker_magic_ball_affects");
	public static final TagKey<Block> CAN_BEFRIEND_NIGHTWALKERS_ON = TagHelper.createBlockTag(
			Dwmg.MOD_ID, "can_befriend_nightwalkers_on");
	
	public static final TagKey<Item> DEATH_CRYSTAL_INGREDIENTS = TagHelper.createItemTag(
			Dwmg.MOD_ID, "death_crystal_ingredients");
	public static final TagKey<Item> ENDER_FRUIT_JAM_OPTIONAL_FRUITS = TagHelper.createItemTag(
			Dwmg.MOD_ID, "ender_fruit_jam_optional_fruits");
	public static final TagKey<Item> ENDERBERRY_CRAFTING_INGREDIENTS_A = TagHelper.createItemTag(
			Dwmg.MOD_ID, "enderberry_crafting_ingredients_a");
	public static final TagKey<Item> ENDERBERRY_CRAFTING_INGREDIENTS_B = TagHelper.createItemTag(
			Dwmg.MOD_ID, "enderberry_crafting_ingredients_b");
	public static final TagKey<Item> HMAG_BERRIES = TagHelper.createItemTag(Dwmg.MOD_ID, "hmag_berries");
	public static final TagKey<Item> SOUL_CLOTH_INGREDIENTS = TagHelper.createItemTag(
			Dwmg.MOD_ID, "soul_cloth_ingredients");

	public static final TagKey<EntityType<?>> IGNORES_UNDEAD_AFFINITY = TagHelper.createEntityTypeTag(
			Dwmg.MOD_ID, "ignores_undead_affinity");
	public static final TagKey<EntityType<?>> IGNORES_MAGICAL_GEL_SLOWNESS = TagHelper.createEntityTypeTag(
			Dwmg.MOD_ID, "ignores_magical_gel_slowness");
	public static final TagKey<EntityType<?>> USES_FORTUNE_AS_LOOTING = TagHelper.createEntityTypeTag(
			Dwmg.MOD_ID, "uses_fortune_as_looting");
	public static final TagKey<EntityType<?>> AFFECTED_BY_UNDEAD_AFFINITY = TagHelper.createEntityTypeTag(
			Dwmg.MOD_ID, "affected_by_undead_affinity");
	public static final TagKey<EntityType<?>> CAN_EQUIP_SOUL_AMULET = TagHelper.createEntityTypeTag(
			Dwmg.MOD_ID, "can_equip_soul_amulet");
	
	/*
	protected static TagKey<Block> blockTag(String name)
	{
		return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Dwmg.MOD_ID, name));
	}
	
	protected static TagKey<Item> itemTag(String name)
	{
		return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Dwmg.MOD_ID, name));
	}
	
	protected static TagKey<EntityType<?>> entityTag(String name)
	{
		return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Dwmg.MOD_ID, name));
	}*/
	
}
