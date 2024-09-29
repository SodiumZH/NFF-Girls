package net.sodiumzh.nff.girls.registry;

import java.util.HashMap;
import java.util.Map;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.services.entity.capability.HealingItemTable;

public class NFFGirlsHealingItems
{

	private static final Map<ResourceLocation, HealingItemTable> HEALING_ITEMS = new HashMap<>();
	
	public static HealingItemTable register(String key, HealingItemTable value)
	{
		HEALING_ITEMS.put(new ResourceLocation(NFFGirls.MOD_ID, key), value);
		return value;
	}

	public static final HealingItemTable NONE = register("none", HealingItemTable.builder().build());
	
	public static final HealingItemTable UNDEAD = register("undead", HealingItemTable.builder()
		.add(ModItems.SOUL_POWDER.get(), 5f)
		.add(ModItems.SOUL_APPLE.get(), 15f)
		.build());
	
	public static final HealingItemTable CREEPER = register("creeper", HealingItemTable.builder()
		.add(Items.GUNPOWDER, 5.0f)
		.add(Items.REDSTONE, 5.0f)
		.add(Items.REDSTONE_BLOCK, 15.0f)
		.build());
	
	public static final HealingItemTable ENDERMAN = register("enderman", HealingItemTable.builder()
		.add(Items.ENDER_EYE, 5.0f)
		.add(ModItems.ANCIENT_STONE.get(), 15.0f)
		.build());

	public static final HealingItemTable GHAST = register("ghast", HealingItemTable.builder()
		.add(Items.GUNPOWDER, 5.0f)
		.add(ModItems.SOUL_POWDER.get(), 5f)
		.add(ModItems.SOUL_APPLE.get(), 15f)
		.build());
	
	public static final HealingItemTable PLANT = register("plant", HealingItemTable.builder()
		.add(Items.WHEAT_SEEDS, 2.0f)
		.add(Items.BONE_MEAL, 5.0f)
		.add(Items.SPORE_BLOSSOM, 15f)
		.add(ModItems.MYSTERIOUS_PETAL.get(), mob -> mob.getMaxHealth())
		.build());
	
	public static final HealingItemTable ANIMAL = register("animal", HealingItemTable.builder()
		.add(Items.COOKIE, 5f)
		.add(Items.COOKED_CHICKEN, 8f)
		.add(Items.COOKED_RABBIT, 8f)
		.add(Items.COOKED_MUTTON, 8f)
		.add(Items.COOKED_BEEF, 10f)
		.add(Items.COOKED_PORKCHOP, 10f)
		//.add(ModItems.COOKED_RAVAGER_MEAT.get(), 30f)
		.add("twilightforest:cooked_meef", 8f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth())
		.build());
	
	public static final HealingItemTable BEE = register("bee", HealingItemTable.builder()
		.add(Items.HONEY_BOTTLE, 5.0f)
		.add(Items.HONEYCOMB, 10.0f)
		.add(Items.HONEY_BLOCK, 15.0f)
		.add(ModItems.MYSTERIOUS_PETAL.get(), mob -> mob.getMaxHealth())
		.build());
	
	public static final HealingItemTable GENERAL_HUMANOID_0 = register("general_humanoid_0", HealingItemTable.builder()
		.add(Items.APPLE, 5f)
		.add(Items.COOKIE, 5f)
		.add(Items.PUMPKIN_PIE, 15f)
		.add(ModItems.LEMON.get(), 10f)
		.add(ModItems.LEMON_PIE.get(), 20f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth())
		.build());
	
	public static final HealingItemTable SNOWMAN = register("snowman", HealingItemTable.builder()
		.add(Items.SNOWBALL, 2f)
		.add(Items.SNOW_BLOCK, 5f)
		.add(Items.PUMPKIN_PIE, 15f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth())
		.build());

	public static final HealingItemTable SLIME = register("slime", HealingItemTable.builder()
		.add(Items.SLIME_BALL, 5f)
		.add(NFFGirlsItems.MAGICAL_GEL_BALL.get(), 15f)
		.add(ModItems.CUBIC_NUCLEUS.get(), mob -> mob.getMaxHealth())
		.build());
	
	public static final HealingItemTable CRIMSON = register("crimson", HealingItemTable.builder()
		.add(Items.CRIMSON_FUNGUS, 5f)
		.add(Items.NETHER_WART, 5f)
		.add(Items.SHROOMLIGHT, 15f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth())
		.build());
	
	public static final HealingItemTable CLOTH_DOLL = register("cloth_doll", HealingItemTable.builder()
		.add(Items.STRING, 2f)
		.add(Items.WHITE_WOOL, 5f)
		.add(Items.LIGHT_GRAY_WOOL, 5f)
		.add(Items.GRAY_WOOL, 5f)
		.add(Items.BLACK_WOOL, 5f)
		.add(Items.BROWN_WOOL, 5f)
		.add(Items.LAPIS_LAZULI, 10f)
		.add(Items.EMERALD, 15f)
		.add(ModItems.CUBIC_NUCLEUS.get(), mob -> mob.getMaxHealth())
		.build());
	
	public static final HealingItemTable BLAZE = register("blaze", HealingItemTable.builder()
		.add(Items.COAL, 5f)
		.add(Items.FIRE_CHARGE, 10f)
		.add(Items.BLAZE_POWDER, 15f)
		.add(ModItems.BURNING_CORE.get(), mob -> mob.getMaxHealth())
		.build());
	
	public static final HealingItemTable CLAY_DOLL = register("clay_doll", HealingItemTable.builder()
		.add(Items.CLAY_BALL, 2f)
		.add(Items.LAPIS_LAZULI, 5f)
		.add(ModItems.ANCIENT_STONE.get(), 15f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth())
		.build());
	
}
