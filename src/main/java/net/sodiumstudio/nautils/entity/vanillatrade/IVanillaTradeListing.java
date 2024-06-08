package net.sodiumstudio.nautils.entity.vanillatrade;

import net.minecraft.world.entity.npc.VillagerTrades;

/**
 * NaUtils' extension of vanilla {@link VillagerTrades.ItemListing}.
 * A Listing is a registry entry that can generate a randomized {@code MerchantOffer} for a vanilla merchant.
 */
public interface IVanillaTradeListing extends VillagerTrades.ItemListing
{
	/**
	 * Get the required merchant level to enable this listing.
	 */
	public int getMerchantLevel();
	
	/**
	 * Get a double value representing the "selection weight". That is, the higher value meaning a higher probability of being chosen
	 * when generating random {@link MerchantOffers} from a set of listings.
	 */
	public double getSelectionWeight();
	
	/**
	 * Check if the listing is valid i.e. can provide a valid {@code MerchantOffer}.
	 */
	public boolean isValid();
}
