package net.sodiumstudio.dwmg.registries;

import java.util.HashMap;
import java.util.function.Function;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.containers.MapPair;

public class DwmgHealingItems
{

	// Full-static
	
	public static final HealingItemTable NONE = HealingItemTable.create();
	
	public static final HealingItemTable UNDEAD = HealingItemTable.create()
		.add(ModItems.SOUL_POWDER.get(), 5f)
		.add(ModItems.SOUL_APPLE.get(), 15f);
	
	public static final HealingItemTable CREEPER = HealingItemTable.create()
		.add(Items.GUNPOWDER, 5.0f)
		.add(Items.REDSTONE, 5.0f)
		.add(Items.REDSTONE_BLOCK, 15.0f);
	
	public static final HealingItemTable ENDERMAN = HealingItemTable.create()
		.add(Items.ENDER_EYE, 5.0f)
		.add(ModItems.ANCIENT_STONE.get(), 15.0f);

	public static final HealingItemTable PLANT = HealingItemTable.create()
		.add(Items.WHEAT_SEEDS, 2.0f)
		.add(Items.BONE_MEAL, 5.0f)
		.add(Items.SPORE_BLOSSOM, 15f)
		.add(ModItems.MYSTERIOUS_PETAL.get(), mob -> mob.getMaxHealth());
	
	public static final HealingItemTable ANIMAL = HealingItemTable.create()
		.add(Items.COOKIE, 5f)
		.add(Items.COOKED_CHICKEN, 8f)
		.add(Items.COOKED_RABBIT, 8f)
		.add(Items.COOKED_MUTTON, 8f)
		.add(Items.COOKED_BEEF, 10f)
		.add(Items.COOKED_PORKCHOP, 10f)
		.add(ModItems.COOKED_RAVAGER_MEAT.get(), 30f)
		.add("twilightforest:cooked_meef", 8f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth());
	
	public static final HealingItemTable BEE = HealingItemTable.create()
		.add(Items.HONEY_BOTTLE, 5.0f)
		.add(Items.HONEYCOMB, 10.0f)
		.add(Items.HONEY_BLOCK, 15.0f)
		.add(ModItems.MYSTERIOUS_PETAL.get(), mob -> mob.getMaxHealth());
	
	public static final HealingItemTable GENERAL_HUMANOID_0 = HealingItemTable.create()
		.add(Items.APPLE, 5f)
		.add(Items.COOKIE, 5f)
		.add(Items.PUMPKIN_PIE, 15f)
		.add(ModItems.LEMON.get(), 10f)
		.add(ModItems.LEMON_PIE.get(), 20f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth());
	
	public static final HealingItemTable SNOWMAN = HealingItemTable.create()
		.add(Items.SNOWBALL, 2f)
		.add(Items.SNOW_BLOCK, 5f)
		.add(Items.PUMPKIN_PIE, 15f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth());

	public static final HealingItemTable SLIME = HealingItemTable.create()
		.add(Items.SLIME_BALL, 5f)
		.add(DwmgItems.MAGICAL_GEL_BALL.get(), 15f)
		.add(ModItems.CUBIC_NUCLEUS.get(), mob -> mob.getMaxHealth());
	
	public static final HealingItemTable CRIMSON = HealingItemTable.create()
		.add(Items.CRIMSON_FUNGUS, 5f)
		.add(Items.NETHER_WART, 5f)
		.add(Items.SHROOMLIGHT, 15f)
		.add(Items.GOLDEN_APPLE, mob -> mob.getMaxHealth());
	
	public static final HealingItemTable CURSED_DOLL = HealingItemTable.create()
		.add(Items.STRING, 2f)
		.add(Items.WHITE_WOOL, 5f)
		.add(Items.LIGHT_GRAY_WOOL, 5f)
		.add(Items.GRAY_WOOL, 5f)
		.add(Items.BLACK_WOOL, 5f)
		.add(Items.BROWN_WOOL, 5f)
		.add(Items.LAPIS_LAZULI, 10f)
		.add(Items.EMERALD, 15f)
		.add(ModItems.CUBIC_NUCLEUS.get(), mob -> mob.getMaxHealth());
	
}
