package net.sodiumstudio.nautils.entity.vanillatrade;

import net.minecraft.util.RandomSource;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.trading.MerchantOffer;

/**
 * A {@code VanillaTradeListingEnchanted} is a {@code VanillaTradeListingEnchanted} of which the result is enchanted. 
 * The enchantment source can be either (Enchantment+level) or {@code RandomEnchantmentSelector}.
 */
public class VanillaTradeListingEnchanted extends VanillaTradeListing
{
	private final Tuple<Enchantment, Integer> enc;
	private RandomEnchantmentSelector sel;
	
	public VanillaTradeListingEnchanted(Enchantment e, int level) 
	{
		this.enc = new Tuple<>(e, level);
		this.sel = null;
	}
	
	public VanillaTradeListingEnchanted(RandomEnchantmentSelector sel)
	{
		this.enc = null;
		this.sel = sel;
	}
	
	@Override
	public MerchantOffer getOffer(Entity trader, RandomSource rnd)
	{
		MerchantOffer offer = super.getOffer(trader, rnd);
		if (this.enc != null)
			offer.getCostB().enchant(enc.getA(), enc.getB());
		else
		{
			Tuple<Enchantment, Integer> e = sel.getValue();
			offer.getCostB().enchant(e.getA(), e.getB());
		}
		return offer;
	}
	
}
