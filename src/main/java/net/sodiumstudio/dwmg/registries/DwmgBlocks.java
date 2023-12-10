package net.sodiumstudio.dwmg.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.blocks.BlockSoulCake;
import net.sodiumstudio.dwmg.blocks.BlockSoulCarpet;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Dwmg.MOD_ID);
	
	// General register function for blocks (for simplification)
	public static RegistryObject<Block> regBlock(String name, BlockBehaviour.Properties properties)
	{
		return DwmgBlocks.BLOCKS.register(name, () -> new Block(properties));	
	}
	
	// Register block items. Must be called after the corresponding block is registered!!
	public static RegistryObject<BlockItem> regBlockItem(String name, RegistryObject<Block> block, Item.Properties properties)
	{
		return DwmgItems.ITEMS.register(name, () -> new BlockItem(block.get(), properties.tab(DwmgItems.TAB)));
	}
	
	
	/* Blocks */
	
	public static final RegistryObject<Block> SOUL_CARPET = DwmgBlocks.BLOCKS.register("soul_carpet", () -> 
		new BlockSoulCarpet(BlockBehaviour.Properties.of
				(Material.WOOL, MaterialColor.COLOR_PURPLE)
				.strength(0.1f)
				.sound(SoundType.WOOL)));
	public static final RegistryObject<Block> SOUL_CAKE = DwmgBlocks.BLOCKS.register("soul_cake", () -> 
		new BlockSoulCake(BlockBehaviour.Properties.of
				(Material.CAKE)
				.strength(0.5F)
				.sound(SoundType.WOOL)));
	public static final RegistryObject<Block> LUMINOUS_TERRACOTTA = DwmgBlocks.BLOCKS.register("luminous_terracotta", () -> 
		new Block(BlockBehaviour.Properties.of
				(Material.STONE, 
				MaterialColor.TERRACOTTA_WHITE)
				.requiresCorrectToolForDrops()
				.strength(1.25F, 4.2F)
				.lightLevel(bs -> 7)));
	
	public static final RegistryObject<Block> ENHANCED_LUMINOUS_TERRACOTTA = DwmgBlocks.BLOCKS.register("enhanced_luminous_terracotta", () -> 
		new Block(BlockBehaviour.Properties.of
				(Material.STONE, 
				MaterialColor.TERRACOTTA_WHITE)
				.requiresCorrectToolForDrops()
				.strength(1.25F, 4.2F)
				.lightLevel(bs -> 15)));
	
	
	/* Block Items */
	//public static final RegistryObject<Item> ITEM_EXAMPLE_BLOCK = regBlockItem("example_block", EXAMPLE_BLOCK, new Item.Properties());
	public static final RegistryObject<BlockItem> ITEM_SOUL_CARPET = regBlockItem("soul_carpet", SOUL_CARPET, new Item.Properties());
	public static final RegistryObject<BlockItem> ITEM_SOUL_CAKE = regBlockItem("soul_cake", SOUL_CAKE, new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
	public static final RegistryObject<BlockItem> ITEM_LUMINOUS_TERRACOTTA = regBlockItem("luminous_terracotta", LUMINOUS_TERRACOTTA, new Item.Properties());
	public static final RegistryObject<BlockItem> ITEM_ENHANCED_LUMINOUS_TERRACOTTA = regBlockItem("enhanced_luminous_terracotta", ENHANCED_LUMINOUS_TERRACOTTA, new Item.Properties());
	
	// Register to event bus
	public static void register(IEventBus eventBus) {
	    BLOCKS.register(eventBus);
	}


}
