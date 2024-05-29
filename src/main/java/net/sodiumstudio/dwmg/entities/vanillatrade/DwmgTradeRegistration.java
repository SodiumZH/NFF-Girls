package net.sodiumstudio.dwmg.entities.vanillatrade;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.nautils.NaUtils;
import net.sodiumstudio.nautils.entity.vanillatrade.VanillaMerchantRegisterListingEvent;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.registries.*;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgTradeRegistration
{
	
	/** Buys player's item with Evil Gem */
	public static VillagerTrades.ItemListing buys(ItemStack buys, int minCount, int maxCountEx, int minPrice, int maxPriceEx, int maxUses)
	{
		return DwmgTradeListings.listing(buys, minCount, maxCountEx, new ItemStack(DwmgItems.EVIL_GEM.get()), minPrice, maxPriceEx, maxUses, 0, 0.05f);
	}
	
	/** Buys player's item with Evil Gem */
	public static VillagerTrades.ItemListing buys(Item buys, int minCount, int maxCountEx, int minPrice, int maxPriceEx, int maxUses)
	{
		return buys(buys.getDefaultInstance(), minCount, maxCountEx, minPrice, maxPriceEx, maxUses);
	}
	
	/** Sell item to player with Evil Gem */
	public static VillagerTrades.ItemListing sells(ItemStack sells, int minCount, int maxCountEx, int minPrice, int maxPriceEx, int maxUses)
	{
		return DwmgTradeListings.listing(DwmgItems.EVIL_GEM.get().getDefaultInstance(), minPrice, maxPriceEx, sells, minCount, maxCountEx, maxUses, 0, 0.05f);
	}
	
	public static VillagerTrades.ItemListing sells(Item sells, int minCount, int maxCountEx, int minPrice, int maxPriceEx, int maxUses)
	{
		return sells(sells.getDefaultInstance(), minCount, maxCountEx, minPrice, maxPriceEx, maxUses);
	}
	
	@SubscribeEvent
	public static void registerTradeListing(VanillaMerchantRegisterListingEvent event)
	{
		event.push(DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get()).addListings(
			buys(ModItems.SOUL_POWDER.get(), 10, 14, 1, 2, 3),
			sells(DwmgItems.DEATH_CRYSTAL_POWDER.get().getDefaultInstance(), 3, 5, 1, 2, 3))
		.addListings(3, 
			buys(ModItems.SOUL_APPLE.get(), 1, 2, 1, 2, 3))
			.pop()
			.link(DwmgEntityTypes.HMAG_SKELETON_GIRL.get(), DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get());
	}
	
	
}
