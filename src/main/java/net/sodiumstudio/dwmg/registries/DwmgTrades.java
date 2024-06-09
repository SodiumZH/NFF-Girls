package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.nautils.entity.vanillatrade.VanillaTradeRegistry;

public class DwmgTrades
{
	public static final VanillaTradeRegistry TRADES = new VanillaTradeRegistry();
	private static final ResourceLocation COMMON_UNDEAD = new ResourceLocation(Dwmg.MOD_ID, "common_trade_undead");
	
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
		TRADES.setCurrency(DwmgItems.EVIL_GEM.get())
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
		.addSells(1, 1, DwmgItems.DEATH_CRYSTAL_POWDER.get(), 2, 2, 4)
		.setRequiredLevel(3)
		.addBuys(ModItems.SOUL_APPLE.get(), 3, 4, 1, 1, 4)
		.addBuys(Items.SOUL_SAND, 56, 64, 1, 1, 2)
		.addBuys(Items.SOUL_SOIL, 56, 64, 1, 1, 2)
		.addBuys(gaiaItem("stone_coal"), 16, 20, 1, 1, 4)
		.addSells(1, 1, DwmgBlocks.ITEM_SOUL_CARPET.get(), 4, 6, 4)
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
		.addBuys(ModItems.DYSSOMNIA_SKIN.get(), 2, 2, 1, 1, 4)
		.addBuys(Items.TOTEM_OF_UNDYING, 1, 1, 4, 7, 1)
		.addBuys(byKey("iceandfire:dread_shard"), 16, 20, 1, 1, 4)
		
		.pop().push("dwmg:hmag_zombie_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(5)
		.addSells(60, 64, Items.ZOMBIE_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		
		.pop().push("dwmg:hmag_husk_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(3)
		.addBuys(byKey("alexsmobs:rattlesnake_rattle"), 5, 8, 1, 1, 4)
		.setRequiredLevel(4)
		.addBuys(byKey("alexsmobs:tarantura_hawk_wing_fragment"), 2, 3, 1, 1, 4)
		.setRequiredLevel(5)
		.addSells(60, 64, Items.ZOMBIE_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		.addSells(1, 1, byKey("alexsmobs:guster_eye"), 1, 1, 2)

		.pop().push("dwmg:hmag_drowned_girl")
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
		.addSells(60, 64, Items.ZOMBIE_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		
		.pop().push("dwmg:hmag_skeleton_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(5)
		.addSells(60, 64, Items.SKELETON_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		
		.pop().push("dwmg:hmag_stray_girl")
		.linkListings(COMMON_UNDEAD)
		.setRequiredLevel(4)
		.addBuys(byKey("twilightforest:arctic_fur"), 16, 20, 1, 1, 4)
		.addBuys(byKey("alexsmobs:froststalker_horn"), 3, 4, 1, 1, 4)
		
		.setRequiredLevel(5)
		.addSells(60, 64, Items.SKELETON_SPAWN_EGG, 1, 1, 1).weight(0.2d)
		
		.pop().push("dwmg:hmag_wither_skeleton_girl")
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
		.pop();
		
		//String str = TRADES.getListings(COMMON_UNDEAD).toString();
/*
		).pop()*/;
		} catch(Throwable t) {
			t.printStackTrace();
			throw t;
		}
	}
}
