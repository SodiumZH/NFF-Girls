package net.sodiumzh.nff.girls.registry;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumzh.nautils.NaUtils;
import net.sodiumzh.nautils.entity.vanillatrade.VanillaTradeRegistry;
import net.sodiumzh.nff.girls.NFFGirls;

public class NFFGirlsTrades
{
	public static final VanillaTradeRegistry TRADES = new VanillaTradeRegistry();
	private static final ResourceLocation COMMON_UNDEAD = new ResourceLocation(NFFGirls.MOD_ID, "common_trade_undead");
	
	private static ItemStack byKey(String key)
	{
		//ResourceLocation loc = new ResourceLocation(key);
		return ForgeRegistries.ITEMS.containsKey(new ResourceLocation(key)) ? 
				ForgeRegistries.ITEMS.getValue(new ResourceLocation(key)).getDefaultInstance() : ItemStack.EMPTY;
	}
	
	private static ItemStack gaiaItem(String key)
	{
		return byKey("grimoireofgaia:" + key);
	}
	
	
	static
	{
		try {
		TRADES.setCurrency(NFFGirlsItems.EVIL_GEM.get())
		.push(COMMON_UNDEAD)
		.setRequiredLevel(1)
		.addBuys(ModItems.SOUL_POWDER.get(), 24, 32, 1, 1, 4)
		.addBuys(Items.NETHER_WART, 56, 64, 1, 1, 4)
		.addSells(1, 1, Items.RED_MUSHROOM, 16, 22, 4)
		.addSells(1, 1, Items.BROWN_MUSHROOM, 16, 22, 4)
		.setRequiredLevel(2)
		.addBuys(Items.ROTTEN_FLESH, 56, 64, 1, 1, 2)
		.addBuys(Items.BONE, 56, 64, 1, 1, 2)
		.addSells(1, 1, Items.QUARTZ, 18, 24, 4)
		.addSells(1, 1, NFFGirlsItems.DEATH_CRYSTAL_POWDER.get(), 2, 2, 4)
		.setRequiredLevel(3)
		.addBuys(ModItems.SOUL_APPLE.get(), 3, 4, 1, 1, 4)
		.addBuys(Items.SOUL_SAND, 56, 64, 1, 1, 2)
		.addBuys(Items.SOUL_SOIL, 56, 64, 1, 1, 2)
		.addBuys(gaiaItem("stone_coal"), 16, 20, 1, 1, 4)
		.addSells(1, 1, NFFGirlsBlocks.ITEM_SOUL_CARPET.get(), 4, 6, 4)
		.addSells(1, 1, gaiaItem("nether_wart_jam"), 4, 6, 4)
		.setRequiredLevel(4)
		.addBuys(Items.GOLD_INGOT, 32, 40, 1, 1, 4)
		.addBuys(Items.ZOMBIE_HEAD, 1, 1, 1, 1, 2)
		.addBuys(Items.SKELETON_SKULL, 1, 1, 1, 1, 2)
		.addBuys(gaiaItem("rotten_heart"), 1, 1, 1, 1, 2)
		.addBuys(byKey("iceandfire:ectoplasm"), 10, 14, 1, 1, 4)
		.addBuys(byKey("alexsmobs:soul_heart"), 1, 1, 1, 1, 4)
		.setRequiredLevel(5)
		.addBuys(Items.CRYING_OBSIDIAN, 12, 16, 1, 1, 4)
		//.addBuys(ModItems.DYSSOMNIA_SKIN.get(), 2, 2, 1, 1, 4)
		.addBuys(Items.TOTEM_OF_UNDYING, 1, 1, 4, 7, 1)
		.addBuys(byKey("iceandfire:dread_shard"), 16, 20, 1, 1, 4)
		
		.pop().push("nffgirls:hmag_zombie_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(5)
		.addSells(128, 128, Items.ZOMBIE_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		
		.pop().push("nffgirls:hmag_husk_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(3)
		.addBuys(byKey("alexsmobs:rattlesnake_rattle"), 5, 8, 1, 1, 4)
		.setRequiredLevel(4)
		.addBuys(byKey("alexsmobs:tarantura_hawk_wing_fragment"), 2, 3, 1, 1, 4)
		.setRequiredLevel(5)
		.addSells(128, 128, Items.ZOMBIE_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		.addSells(1, 1, byKey("alexsmobs:guster_eye"), 1, 1, 2)

		.pop().push("nffgirls:hmag_drowned_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(2)
		.addSells(1, 1, Items.PRISMARINE_CRYSTALS, 12, 16, 4)
		.addSells(1, 1, Items.PRISMARINE_SHARD, 8, 12, 4)
		.setRequiredLevel(3)
		.addBuys(Items.KELP, 56, 64, 1, 1, 4)
		.addBuys(Items.TURTLE_EGG, 8, 10, 1, 1, 4)
		.addSells(1, 1, byKey("iceandfire:shiny_scales"), 3, 4, 2)
		.setRequiredLevel(4)
		.addBuys(Items.NAUTILUS_SHELL, 2, 3, 1, 1, 4)
		.addSells(1, 1, byKey("alexsmobs:rainbow_jelly"), 1, 1, 4)
		.setRequiredLevel(5)
		.addSells(16, 20, Items.TRIDENT, 0, 1, 1)
		.addSells(24, 33, Items.HEART_OF_THE_SEA, 1, 1, 1)
		.addBuys(byKey("iceandfire:seaserpent_skull"), 1, 1, 3, 4, 2)
		.addSells(128, 128, Items.ZOMBIE_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		
		.pop().push("nffgirls:hmag_skeleton_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(5)
		.addSells(128, 128, Items.SKELETON_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		
		.pop().push("nffgirls:hmag_stray_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(4)
		.addBuys(byKey("twilightforest:arctic_fur"), 16, 20, 1, 1, 4)
		.addBuys(byKey("alexsmobs:froststalker_horn"), 3, 4, 1, 1, 4)
		.setRequiredLevel(5)
		.addSells(128, 128, Items.SKELETON_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		
		.pop().push("nffgirls:hmag_wither_skeleton_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(3)
		.addSells(1, 1, Items.OBSIDIAN, 8, 12, 4)
		.addBuys(byKey("iceandfire:witherbone"), 12, 16, 1, 1, 4)
		.setRequiredLevel(4)
		.addBuys(Items.WITHER_SKELETON_SKULL, 1, 1, 1, 1, 2)
		.addSells(1, 1, Items.WITHER_ROSE, 3, 4, 4)
		.addBuys(gaiaItem("withered_brain"), 1, 1, 1, 1, 2)
		.setRequiredLevel(5)
		.addBuys(byKey("iceandfire:cocatrice_eye"), 1, 1, 3, 5, 2)
		.addSells(12, 16, ModItems.NETHER_STAR_FRAGMENT.get(), 1, 1, 2).weight(0.5d)
		
		.pop().push("nffgirls:hmag_creeper_girl")
		.setRequiredLevel(1)
		.addBuys(Items.GUNPOWDER, 28, 36, 1, 1, 4)
		.addBuys(Items.COAL, 28, 36, 1, 1, 4)
		.addBuys(Items.FIRE_CHARGE, 28, 36, 1, 1, 4)
		.addBuys(byKey("byg:anthracite"), 28, 36, 1, 1, 4)
		.setRequiredLevel(2)
		.addBuys(Items.FLINT, 40, 48, 1, 1, 4)
		.addSells(1, 1, Items.TNT, 3, 5, 4)
		.addSells(1, 1, ModItems.BLASTING_BOTTLE.get(), 5, 8, 4)
		.addBuys(byKey("byg:brim_powder"), 56, 64, 1, 1, 4)
		.setRequiredLevel(3)
		.addBuys(Items.REDSTONE, 40, 48, 1, 1, 4)
		.addSells(1, 1, Items.REPEATER, 3, 4, 4)
		.addSells(1, 1, Items.COMPARATOR, 2, 3, 4)
		.setRequiredLevel(4)
		.addSells(1, 1, ModItems.LIGHTNING_PARTICLE.get(), 1, 1, 4)
		.addSells(1, 1, ModItems.EXP_BERRY.get(), 2, 2, 4)
		.addSells(1, 1, ModItems.RANDOMBERRY.get(), 2, 2, 4)
		.addSells(1, 1, ModItems.CUREBERRY.get(), 2, 2, 4)
		.setRequiredLevel(5)
		.addSells(3, 5, byKey("twilightforest:charm_of_keeping_1"), 1, 1, 2)
		.addSellsEnchantmentBook(4, 6, Enchantments.BLAST_PROTECTION, 4, 2)
		
		.pop().push("nffgirls:hmag_necrotic_reaper")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(3)
		.addSells(1, 1, ModItems.NECROFIBER.get(), 2, 2, 2)
		.addSells(1, 1, ModItems.LICH_CLOTH.get(), 2, 2, 2)
		.setRequiredLevel(4)
		.addSells(1, 1, ModItems.DYSSOMNIA_SKIN.get(), 2, 2, 2)
		.setRequiredLevel(5)
		.addSellsEnchantmentBook(4, 6, Enchantments.SHARPNESS, 5, 2)
		
		/*.pop().push("nffgirls:hmag_banshee")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(3)*/
		
		;

		
		if (NaUtils.getServer() != null)
		{
			
		}
		
		} catch(Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
}
