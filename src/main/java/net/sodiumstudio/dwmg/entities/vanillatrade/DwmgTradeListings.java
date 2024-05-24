package net.sodiumstudio.dwmg.entities.vanillatrade;

import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public abstract class DwmgTradeListings
{

	public static VillagerTrades.ItemListing listing(ItemStack baseCostA, int minCostCount, int maxCostCountExclude, ItemStack costB, ItemStack result, int maxUses, int xp, float priceMultiplier)
	{
		return (entity, rnd) -> {
			ItemStack costA = baseCostA.copy();
			costA.setCount(rnd.nextInt(minCostCount, maxCostCountExclude));
			return new MerchantOffer(costA, costB, result, 0, maxUses, xp, priceMultiplier);
		};
	}
	
	public static VillagerTrades.ItemListing buys(Item buys, int minCount, int maxCountEx, int maxUses)
	{
		return listing(buys.getDefaultInstance(), minCount, maxCountEx, ItemStack.EMPTY, new ItemStack(DwmgItems.EVIL_GEM.get()), maxUses, 0, 0.05f);
	}
	
	public static VillagerTrades.ItemListing sells(ItemStack sells, int minCount, int maxCountEx, int maxUses)
	{
		return listing(DwmgItems.EVIL_GEM.get().getDefaultInstance(), minCount, maxCountEx, ItemStack.EMPTY, sells.copy(), maxUses, 0, 0.05f);
	}
	
	public static VillagerTrades.ItemListing sells(Item sells, int minCount, int maxCountEx, int maxUses)
	{
		return sells(sells.getDefaultInstance(), minCount, maxCountEx, maxUses);
	}
	
}
