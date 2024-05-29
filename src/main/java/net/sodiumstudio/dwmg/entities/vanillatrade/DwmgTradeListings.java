package net.sodiumstudio.dwmg.entities.vanillatrade;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public abstract class DwmgTradeListings
{

	/** Full listing */
	public static VillagerTrades.ItemListing listing(
			ItemStack baseCostA, int minCostA, int maxCostAEx, 
			ItemStack costB, int minCostB, int maxCostBEx,
			ItemStack result, int minResult, int maxResultEx,
			int maxUses, int xp, float priceMultiplier)
	{
		return (entity, rnd) -> {
			ItemStack a = baseCostA.copy();
			a.setCount(rnd.nextInt(minCostA, maxCostAEx));
			ItemStack b = costB.copy();
			if (!b.isEmpty())
				b.setCount(rnd.nextInt(minCostB, maxCostBEx));
			ItemStack r = result.copy();
			r.setCount(rnd.nextInt(minResult, maxResultEx));
			return new MerchantOffer(a, b, r, 0, maxUses, xp, priceMultiplier);
		};
	}
	
	/** Listing without B */
	public static VillagerTrades.ItemListing listing(
			ItemStack baseCostA, int minCostA, int maxCostAEx, 
			ItemStack result, int minResult, int maxResultEx,
			int maxUses, int xp, float priceMultiplier)
	{
		return listing(baseCostA, minCostA, maxCostAEx, ItemStack.EMPTY, 0, 1, result, minResult, maxResultEx, maxUses, xp, priceMultiplier);
	}
	
	/** Buys player's item with Evil Gem */
	public static VillagerTrades.ItemListing buys(ItemStack buys, int minCount, int maxCountEx, int minPrice, int maxPriceEx, int maxUses)
	{
		return listing(buys, minCount, maxCountEx, new ItemStack(DwmgItems.EVIL_GEM.get()), minPrice, maxPriceEx, maxUses, 0, 0.05f);
	}
	
	/** Buys player's item with Evil Gem */
	public static VillagerTrades.ItemListing buys(Item buys, int minCount, int maxCountEx, int minPrice, int maxPriceEx, int maxUses)
	{
		return buys(buys.getDefaultInstance(), minCount, maxCountEx, minPrice, maxPriceEx, maxUses);
	}
	
	/** Sell item to player with Evil Gem */
	public static VillagerTrades.ItemListing sells(ItemStack sells, int minCount, int maxCountEx, int minPrice, int maxPriceEx, int maxUses)
	{
		return listing(DwmgItems.EVIL_GEM.get().getDefaultInstance(), minPrice, maxPriceEx, sells, minCount, maxCountEx, maxUses, 0, 0.05f);
	}
	
	public static VillagerTrades.ItemListing sells(Item sells, int minCount, int maxCountEx, int minPrice, int maxPriceEx, int maxUses)
	{
		return sells(sells.getDefaultInstance(), minCount, maxCountEx, minPrice, maxPriceEx, maxUses);
	}
	
}
