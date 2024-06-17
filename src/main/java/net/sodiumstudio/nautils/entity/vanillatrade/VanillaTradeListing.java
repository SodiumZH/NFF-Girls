package net.sodiumstudio.nautils.entity.vanillatrade;

import java.util.ArrayList;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.NbtHelper;

public class VanillaTradeListing implements IVanillaTradeListing
{
	protected ArrayList<ItemStack> baseCostA = new ArrayList<>();	// If there're more than one, it will randomly pick one with the same probability.
	protected TradeCountRange aCount = TradeCountRange.fixed(1);
	protected ArrayList<ItemStack> costB = new ArrayList<>();
	protected TradeCountRange bCount =  TradeCountRange.fixed(1);
	protected ArrayList<ItemStack> result = new ArrayList<>();
	protected TradeCountRange resCount = TradeCountRange.fixed(1);
	protected int xpReward = 0;
	protected int maxUses = 12;
	protected float priceMultiplier = 0f;
	protected int demand = 0;
	protected int requiredLevel = 1;
	protected double selectionWeight = 1.0d;	// Higher meaning higher probability of being picked in a set of listings.
	protected boolean mapBToResult = false;	// If true, a given B will always get the corresponding result with the same array index.
	protected boolean linkBCountToResult = false;	// If true, the count of B is always same to the result.
	protected boolean hasB = false;		// To indicate whether this listing is expected to have B. If it's true but the B is missing, it will be counted as invalid.
	
	private VanillaTradeListing() {}
	
	/**
	 * Create an invalid instance. You must add costA and result manually.
	 */
	public static VanillaTradeListing createInvalid()
	{
		return new VanillaTradeListing();
	}
	
	/**
	 * Create an instance with a costA and a result.
	 */
	public static VanillaTradeListing create(ItemStack costA, ItemStack result)
	{
		VanillaTradeListing res = new VanillaTradeListing();
		res.baseCostA.add(costA.copy());
		res.result.add(result.copy());
		return res;
	}
	
	/**
	 * Create an instance with a costA and a result.
	 */
	public static VanillaTradeListing create(Item costA, Item result)
	{
		return create(getNonnullInstance(costA), getNonnullInstance(result));
	}
	
	/**
	 * Add possible costA(s). If there're multiple elements, it will randomly pick one 
	 * with same probability when generating {@code MerchantOffer}s.
	 */
	public VanillaTradeListing addA(ItemStack... stacks)
	{
		for (ItemStack stack: stacks)
			if (stack != null && !stack.isEmpty()) baseCostA.add(stack.copy());
		return this;
	}
	
	/**
	 * Add possible costA(s). If there're multiple elements, it will randomly pick one 
	 * with same probability when generating {@code MerchantOffer}s.
	 */
	public VanillaTradeListing addA(Item... items)
	{
		for (Item item: items)
			if (item != null && item != Items.AIR) baseCostA.add(item.getDefaultInstance().copy());
		return this;
	}
	
	/**
	 * Add possible costB(s). If there're multiple elements, it will randomly pick one 
	 * with same probability when generating {@code MerchantOffer}s.
	 */
	public VanillaTradeListing addB(ItemStack... stacks)
	{
		for (ItemStack stack: stacks)
			if (stack != null && !stack.isEmpty()) 
			{
				costB.add(stack.copy());
				hasB = true;
			}
		return this;
	}
	
	/**
	 * Add possible costB(s). If there're multiple elements, it will randomly pick one 
	 * with same probability when generating {@code MerchantOffer}s.
	 */
	public VanillaTradeListing addB(Item... items)
	{
		for (Item item: items)
			if (item != null && item != Items.AIR) 
			{
				costB.add(item.getDefaultInstance().copy());
				hasB = true;
			}
		return this;
	}

	/**
	 * Add possible result(s). If there're multiple elements, it will randomly pick one 
	 * with same probability when generating {@code MerchantOffer}s.
	 */
	public VanillaTradeListing addResult(ItemStack... stacks)
	{
		for (ItemStack stack: stacks)
			if (stack != null && !stack.isEmpty()) result.add(stack.copy());
		return this;
	}
	
	/**
	 * Add possible result(s). If there're multiple elements, it will randomly pick one 
	 * with same probability when generating {@code MerchantOffer}s.
	 */
	public VanillaTradeListing addResult(Item... items)
	{
		for (Item item: items)
			if (item != null && item != Items.AIR) result.add(item.getDefaultInstance().copy());
		return this;
	}
	
	/**
	 * Set the count selection range for cost A. (Uniform-distribution for randomization.)
	 * @param min Minimum available value (included).
	 * @param max Maximum available value (INCLUDED).
	 */
	public VanillaTradeListing setACountRange(int min, int max)
	{
		this.aCount = TradeCountRange.uniform(min, max);
		return this;
	}

	/**
	 * Set the cost A count to be a fixed value without randomization.
	 */
	public VanillaTradeListing setACount(int count)
	{
		this.aCount = TradeCountRange.fixed(count);
		return this;
	}

	/**
	 * Set that the count selection for cost A should use Poisson-distribution for randomization with parameter p.
	 * @param p Poisson parameter (0-1). Higher value means higher probability of getting a larger value. 0.5 by default meaning symmetric.
	 */
	public VanillaTradeListing setAPoisson(double p)
	{
		this.aCount = this.aCount.setPoisson(p);
		return this;
	}
	
	/**
	 * Set that the count selection for cost A should use Poisson-distribution for randomization with parameter 0.5.
	 */
	public VanillaTradeListing setAPoisson()
	{
		return this.setAPoisson(0.5d);
	}
	
	/**
	 * Set that the count selection for cost A should use uniform-distribution for randomization.
	 */
	public VanillaTradeListing setAUniform()
	{
		this.aCount = this.aCount.setUniform();
		return this;
	}
	
	/**
	 * Set the count selection range for cost B. (Uniform-distribution for randomization.)
	 * @param min Minimum available value (included).
	 * @param max Maximum available value (INCLUDED).
	 */
	public VanillaTradeListing setBCountRange(int min, int max)
	{
		this.bCount = TradeCountRange.uniform(min, max);
		return this;
	}

	/**
	 * Set the cost B count to be a fixed value without randomization.
	 */
	public VanillaTradeListing setBCount(int count)
	{
		this.bCount = TradeCountRange.fixed(count);
		return this;
	}
	
	/**
	 * Set that the count selection for cost B should use Poisson-distribution for randomization with parameter p.
	 * @param p Poisson parameter (0-1). Higher value means higher probability of getting a larger value. 0.5 by default meaning symmetric.
	 */
	public VanillaTradeListing setBPoisson(double p)
	{
		this.bCount = this.bCount.setPoisson(p);
		return this;
	}
	
	/**
	 * Set that the count selection for cost B should use Poisson-distribution for randomization with parameter 0.5.
	 */
	public VanillaTradeListing setBPoisson()
	{
		return this.setBPoisson(0.5d);
	}
	
	/**
	 * Set that the count selection for costB should use uniform-distribution for randomization. 
	 */
	public VanillaTradeListing setBUniform()
	{
		this.bCount = this.bCount.setUniform();
		return this;
	}
	
	/**
	 * Set the count selection range for result. (Uniform-distribution for randomization.)
	 * @param min Minimum available value (included).
	 * @param max Maximum available value (INCLUDED).
	 */
	public VanillaTradeListing setResultCountRange(int min, int max)
	{
		this.resCount = TradeCountRange.uniform(min, max);
		return this;
	}

	/**
	 * Set the result count to be a fixed value without randomization.
	 */
	public VanillaTradeListing setResultCount(int count)
	{
		this.resCount = TradeCountRange.fixed(count);
		return this;
	}
	
	/**
	 * Set that the count selection for result should use Poisson-distribution for randomization with parameter p.
	 * @param p Poisson parameter (0-1). Higher value means higher probability of getting a larger value. 0.5 by default meaning symmetric.
	 */
	public VanillaTradeListing setResultPoisson(double p)
	{
		this.resCount = this.resCount.setPoisson(p);
		return this;
	}
	
	/**
	 * Set that the count selection for result should use Poisson-distribution for randomization with parameter 0.5.
	 */
	public VanillaTradeListing setResultPoisson()
	{
		return this.setResultPoisson(0.5d);
	}
	
	/**
	 * Set that the count selection for result should use uniform-distribution for randomization. 
	 */
	public VanillaTradeListing setResultUniform()
	{
		this.resCount = this.resCount.setUniform();
		return this;
	}
	
	/**
	 * Set that the count selection for costA, costB and result should use Poisson distribution for randomization with factor p. 
	 */
	public VanillaTradeListing setAllPoisson(double p)
	{
		this.setAPoisson(p);
		this.setBPoisson(p);
		this.setResultPoisson(p);
		return this;
	}
	
	/**
	 * Set that the count selection for costA, costB and result should use Poisson distribution for randomization with factor 0.5. 
	 */
	public VanillaTradeListing setAllPoisson()
	{
		return this.setAllPoisson(0.5d);
	}
	
	/**
	 * Set that the count selection for costA, costB and result should use uniform distribution for randomization. 
	 */
	public VanillaTradeListing setAllUniform()
	{
		this.setAUniform();
		this.setBUniform();
		this.setResultUniform();
		return this;
	}
	
	/**
	 * Set the max use times before getting out-of-stock.
	 */
	public VanillaTradeListing setMaxUses(int val)
	{
		this.maxUses = val;
		return this;
	}
	
	/**
	 * Set the player XP reward for each time of the trade.
	 */
	public VanillaTradeListing setXpReward(int val)
	{
		this.xpReward = val;
		return this;
	}
	
	/**
	 * Set the required Merchant level to enable the offer.
	 */
	public VanillaTradeListing setRequiredLevel(int val)
	{
		this.requiredLevel = Math.max(1, val);
		return this;
	}
	
	/**
	 * Declares this Listing should have B. After doing this, if this Listing is missing B, it will be regarded as invalid.
	 */
	public VanillaTradeListing setHasB()
	{
		this.hasB = true;
		return this;
	}
	
	/**
	 * Declare that the count of cost B and result should always be the same.
	 * This is mainly for paid transformations (e.g. Emerald + Raw Fish -> Cooked Fish).
	 */
	public VanillaTradeListing linkBCountToResult()
	{
		this.linkBCountToResult = true;
		return this;
	}
	
	/**
	 * Declare that the costB and result items should have the same array index in the lists of costBs and results.
	 * For example, the costB list in this Listing is {A, B, C}, and the result list is {D, E, F}, then when it picks item A as costB, 
	 * it should always pick item D as the result, similarly B with E, C with F.
	 * <p><b>Note:</b> It's generally not recommended to add items originated from different mods to Bs and results as
	 * missing of mods could cause mismatching. It could be OK if you're sure all mapped pairs of Bs and results (i.e, B[i] and result[i]
	 * for all available i) come from the same mod.
	 */
	public VanillaTradeListing mapBToResult()
	{
		this.mapBToResult = true;
		return this;
	}
	
	/**
	 * Set the selection weight of this Listing.
	 * <p>A selection weight is a {@code double} value represents this Listing's probability of being picked in a set of Listings.
	 * That is, the higher value meaning a higher probability of being chosen when generating random {@link MerchantOffers} 
	 * from a set of listings.
	 */
	public VanillaTradeListing setSelectionWeight(double value)
	{
		this.selectionWeight = value;
		return this;
	}
	
	@Override
	public int getMerchantLevel() {
		return Math.max(requiredLevel, 1);
	}
	
	@Override
	public boolean isValid() {
		this.clearInvalidEntries();
		return this.baseCostA != null && !this.baseCostA.isEmpty() && this.result != null && !this.result.isEmpty() && !(this.hasB && (this.costB == null || this.costB.isEmpty()));
	}

	protected void clearInvalidEntries()
	{
		if (this.baseCostA != null) this.baseCostA.removeIf(ItemStack::isEmpty);
		if (this.costB != null) this.costB.removeIf(ItemStack::isEmpty);
		if (this.result != null) this.result.removeIf(ItemStack::isEmpty);
	}
	
	/**
	 * Be cautious that {@code ItemStack}s in the output offer should be decoupled to those stored in 
	 * the Listing instance (i.e. using {@code copy()}), otherwise it may unexpectedly modify the Listing
	 * itself and cause problems that are hard to locate.
	 */
	@Nullable
	@Override
	public MerchantOffer getOffer(Entity trader, Random rnd) {
		if (!this.isValid()) return null;	// Invalid entries have been cleared here
		ItemStack a = this.baseCostA.isEmpty() ? ItemStack.EMPTY : this.baseCostA.get(rnd.nextInt(this.baseCostA.size()));
		a = a.copy();
		int ca = this.aCount.getValue();
		int bIndex = this.costB.isEmpty() ? 0 : rnd.nextInt(this.costB.size());
		ItemStack b = this.costB.isEmpty() ? ItemStack.EMPTY : this.costB.get(bIndex);
		b = b.copy();
		int cb = this.bCount.getValue();
		ItemStack r;
		if (!costB.isEmpty() && this.mapBToResult)
		{
			if(this.result.size() < this.costB.size())
				throw new IllegalStateException("NaUtils#VanillaTradeListing: set mapping B to result, but B size is larger than result size"); 
			else r = this.result.get(bIndex);
		}
		else r = this.result.isEmpty() ? ItemStack.EMPTY : this.result.get(rnd.nextInt(this.result.size()));
		r = r.copy();
		int cr = this.linkBCountToResult ? cb : this.resCount.getValue();
		a.setCount(ca);
		b.setCount(cb);
		r.setCount(cr);
		MerchantOffer offer = new MerchantOffer(a, b, r, 0, this.maxUses, this.xpReward, this.priceMultiplier, this.demand);
		return offer;
	}

	@Override
	public double getSelectionWeight() {
		return selectionWeight;
	}
	
	// Utilities
	
	/**
	 * Create a listing for simple exchanging (no B, e.g. simply buying or selling sth)
	 */
	public static VanillaTradeListing exchanges(ItemStack cost, int costMin, int costMax, ItemStack result, int resultMin, int resultMax)
	{
		return VanillaTradeListing.create(cost, result).setACountRange(costMin, costMax).setResultCountRange(resultMin, resultMax);
	}

	/**
	 * Create a listing for simple exchanging (no B, e.g. simply buying or selling sth)
	 */
	public static VanillaTradeListing exchanges(Item cost, int costMin, int costMax, Item result, int resultMin, int resultMax)
	{
		return VanillaTradeListing.exchanges(getNonnullInstance(cost), costMin, costMax, 
				getNonnullInstance(result), resultMin, resultMax);
	}
	
	/**
	 * Create a listing for a paid conversion (e.g. Emerald + Raw Fish -> Cooked Fish)
	 */
	public static VanillaTradeListing converts(ItemStack cost, int costMin, int costMax, ItemStack transformsFrom, ItemStack transformsTo,
			int amountMin, int amountMax)
	{
		return VanillaTradeListing.create(cost, transformsTo).addB(transformsFrom).setACountRange(costMin, costMax)
				.setBCountRange(amountMin, amountMax).linkBCountToResult().setHasB();
	}
	
	/**
	 * Create a listing for a paid conversion (e.g. Emerald + Raw Fish -> Cooked Fish)
	 */
	public static VanillaTradeListing converts(Item cost, int costMin, int costMax, Item transformsFrom, Item transformsTo,
			int amountMin, int amountMax)
	{
		return VanillaTradeListing.converts(getNonnullInstance(cost), costMin, costMax, getNonnullInstance(transformsFrom), 
				getNonnullInstance(transformsTo), amountMin, amountMax);
	}
	
	/**
	 * Create an <b>invalid</b> listing without any items but with A and result amounts preset. 
	 */
	public static VanillaTradeListing invalidWithAmounts(int costAMin, int costAMax, int resultMin, int resultMax)
	{
		return VanillaTradeListing.createInvalid().setACountRange(costAMin, costAMax).setResultCountRange(resultMin, resultMax);
	}
	
	@Override
	public String toString()
	{
		String res = String.format("VanillaTradeListing%s{costA = ", this.isValid() ? "" : "(Invalid)");
		
		if (this.baseCostA.size() == 1)
			res = res + this.baseCostA.get(0).getItem().toString();
		else res = res + NaContainerUtils.castList(this.baseCostA, stack -> stack.getItem()).toString();
		res = res + ", countA = " + this.aCount.toString() + ", ";
		if (this.hasB)
			res = res + "hasB, ";
		
		if (this.costB.size() > 0)
		{
			res = res + "costB = ";
			if (this.costB.size() == 1)
				res = res + this.costB.get(0).getItem().toString();
			else res = res +  NaContainerUtils.castList(this.costB, stack -> stack.getItem()).toString();
			res = res + ", countB = " + this.bCount.toString() + ", ";
		}
		
		res = res + " result = ";
		if (this.result.size() == 1)
			res = res + this.result.get(0).getItem().toString();
		else res = res +  NaContainerUtils.castList(this.result, stack -> stack.getItem()).toString();
		res = res + ", countResult = " + this.resCount.toString();
		
		res = res + String.format(", requiredLevel = %d, maxUses = %d", this.requiredLevel, this.maxUses);
		if (this.mapBToResult) res = res + ", mapBToResult";
		if (this.linkBCountToResult) res = res + ", linkBCountToResult";
		if (this.xpReward != 0) res = res + String.format(", xpReward = %d", this.xpReward);
		if (this.selectionWeight != 1d) res = res + String.format(", selectionWeight = %f", this.selectionWeight);
		if (this.priceMultiplier != 0d) res = res + String.format(", priceMultiplier = %f", this.priceMultiplier);
		res = res + "}\n";
		return res;
	}
	
	// Utilities
	protected static ItemStack getNonnullInstance(@Nullable Item item)
	{
		return item != null ? item.getDefaultInstance() : ItemStack.EMPTY;
	}

	/*public static VanillaTradeListing readJson(ResourceLocation location)
	{
		MinecraftServer.
	}*/
	
	
}
