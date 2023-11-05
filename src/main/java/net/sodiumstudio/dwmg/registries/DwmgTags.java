package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.HMaG;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.sodiumstudio.dwmg.Dwmg;

public class DwmgTags
{
	public static final TagKey<Block> AFFECTS_CRIMSON_SLAUGHTERER = blockTag("affects_crimson_slaughterer");
	
	public static final TagKey<Item> DEATH_CRYSTAL_INGREDIENTS = itemTag("death_crystal_ingredients");
	public static final TagKey<Item> ENDER_FRUIT_JAM_OPTIONAL_FRUITS = itemTag("ender_fruit_jam_optional_fruits");
	public static final TagKey<Item> ENDERBERRY_CRAFTING_INGREDIENTS_A = itemTag("enderberry_crafting_ingredients_a");
	public static final TagKey<Item> ENDERBERRY_CRAFTING_INGREDIENTS_B = itemTag("enderberry_crafting_ingredients_b");
	public static final TagKey<Item> HMAG_BERRIES = itemTag("hmag_berries");
	public static final TagKey<Item> SOUL_CLOTH_INGREDIENTS = itemTag("soul_cloth_ingredients");

	public static final TagKey<EntityType<?>> IGNORES_UNDEAD_AFFINITY = entityTag("ignores_undead_affinity");
	public static final TagKey<EntityType<?>> IGNORES_MAGICAL_GEL_SLOWNESS = entityTag("ignores_magical_gel_slowness");
	public static final TagKey<EntityType<?>> USES_FORTUNE_AS_LOOTING = entityTag("uses_fortune_as_looting");
	public static final TagKey<EntityType<?>> AFFECTED_BY_UNDEAD_AFFINITY = entityTag("affected_by_undead_affinity");
	
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
	}
	
}
