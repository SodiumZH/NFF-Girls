package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.*;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.*;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.DwmgTab;
import net.sodiumstudio.dwmg.dwmgcontent.blocks.BlockSoulCarpet;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Dwmg.MOD_ID);
	
	// General register function for blocks (for simplification)
	public static RegistryObject<Block> regBlock(String name, BlockBehaviour.Properties properties)
	{
		return DwmgBlocks.BLOCKS.register(name, () -> new Block(properties));	
	}
	
	// Register block items. Must be called after the corresponding block is registered!!
	public static RegistryObject<Item> regBlockItem(String name, RegistryObject<Block> block, Item.Properties properties)
	{
		return DwmgItems.ITEMS.register(name, () -> new BlockItem(block.get(), properties.tab(DwmgItems.TAB)));
	}
	
	
	/* Blocks */
	
	//public static final RegistryObject<Block> EXAMPLE_BLOCK = regBlock("example_block", BlockBehaviour.Properties.of
	//	(Material.METAL, MaterialColor.COLOR_PURPLE).strength(3.0f).sound(SoundType.METAL).requiresCorrectToolForDrops());		
	public static final RegistryObject<Block> SOUL_CARPET = DwmgBlocks.BLOCKS.register("soul_carpet", () -> new BlockSoulCarpet(BlockBehaviour.Properties.of
		(Material.WOOL, MaterialColor.COLOR_PURPLE).strength(0.1f).sound(SoundType.WOOL)));

	
	
	/* Block Items */
	//public static final RegistryObject<Item> ITEM_EXAMPLE_BLOCK = regBlockItem("example_block", EXAMPLE_BLOCK, new Item.Properties());
	public static final RegistryObject<Item> ITEM_SOUL_CARPET = regBlockItem("soul_carpet", SOUL_CARPET, new Item.Properties());
	
	// Register to event bus
	public static void register(IEventBus eventBus) {
	    BLOCKS.register(eventBus);
	}


}
