 package net.sodiumstudio.nautils.entity.vanillatrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.containers.CompoundSet;
import net.sodiumstudio.nautils.math.RandomSelection;

public class VanillaTradeListings<T extends IVanillaTradeListing>
{
	private CompoundSet<T> set = new CompoundSet<>();
	
	public VanillaTradeListings() {}
	
	public VanillaTradeListings<T> add(T t)
	{
		if (t != null && t.isValid())
			set.add(t);
		return this;
	}
	
	public VanillaTradeListings<T> addAll(Collection<T> c)
	{
		Set<T> copy = new HashSet<>();
		copy.addAll(c);
		copy.removeIf(t -> t == null || !t.isValid());
		this.set.addAll(copy);
		return this;
	}
	
	public VanillaTradeListings<T> linkExternal(Set<T> other)
	{
		set.addExternalSet(other);
		return this;
	}
	
	public VanillaTradeListings<T> linkExternal(VanillaTradeListings<T> other)
	{
		set.addExternalSet(other.set);
		return this;
	}
	
	/**
	 * Get a set of all valid listings.
	 */
	public Set<T> getValidSet()
	{
		Set<T> res = this.set.toHashSet();
		res.removeIf(t -> !t.isValid());
		return res;
	}
	
	/**
	 * Get a subset of valid listings with the given merchant level.
	 */
	public Set<T> forLevel(int level)
	{
		Set<T> res = this.set.toHashSet();
		res.removeIf(t -> !t.isValid());
		res.removeIf(t -> t.getMerchantLevel() != level);
		return res;
	}
	
	/**
	 * Get all merchant levels that have valid listings, sorted from smallest to largest.
	 */
	public List<Integer> allLevels()
	{
		List<Integer> res = new ArrayList<>();
		this.set.toHashSet().forEach(t -> {
			if (!res.contains(t.getMerchantLevel()))
				res.add(t.getMerchantLevel());
		});
		return NaContainerUtils.toArrayList(res.stream().sorted().toList());
	}
	
	/**
	 * Randomly pick several listings from the set with given amount and merchant level.
	 * <p>Note: the output set size could possibly be smaller than the input.
	 */ 
	public Set<T> pickListings(int amount, int merchantLevel)
	{
		if (amount < 0)
			throw new IllegalArgumentException();
		if (amount == 0)
			return new HashSet<>();
		HashSet<T> candidates = set.toHashSet();
		candidates.removeIf(t -> t == null || !t.isValid());
		candidates.removeIf(t -> t.getMerchantLevel() != merchantLevel);
		if (amount >= candidates.size())
			return candidates;
		
		HashSet<T> out = new HashSet<>();
		for (int i = 0; i < amount; ++i)
		{
			double totalWeight = 0;
			T fallback = null;	// This shouldn't be called, but due to the double calculation error there could be a very minor probability to hit the fallback
			for (var t: candidates)
			{
				totalWeight += t.getSelectionWeight();
				if (fallback == null || fallback.getSelectionWeight() < t.getSelectionWeight())
					fallback = t;
			}
			RandomSelection<T> sel = RandomSelection.create(fallback);
			for (var t: candidates)
			{
				sel.add(t, t.getSelectionWeight() / totalWeight);
			}
			T selected = sel.getValue();
			if (selected != null)
			{
				candidates.remove(selected);
				out.add(selected);
			}
		}
		return out;
	}
	
	/**
	 * Pick listing instances for all present levels.
	 * @param amountForEachLevel How many Listing instances it should pick for each level.
	 * Null input or absent level value will be picked 1 instance.
	 * @return A list of Listing instances with ascending order in level. 
	 */
	public List<T> pickListingForAllLevels(@Nullable Map<Integer, Integer> amountForEachLevel)
	{
		List<T> res = new ArrayList<>();
		for (int lv: this.allLevels())
		{
			int amount = (amountForEachLevel != null && amountForEachLevel.containsKey(lv)) ? amountForEachLevel.get(lv) : 1;
			res.addAll(this.pickListings(amount, lv));
		}
		res.removeIf(t -> t == null || !t.isValid());
		return res;
	}
	
	/**
	 * Pick listing instances for all present levels.
	 * @param amountForEachLevel How many Listing instances it should pick for each level.
	 * input[i] for level i+1.
	 * @return A list of Listing instances with ascending order in level. 
	 */
	public List<T> pickListingsForAllLevels(int... amountForEachLevel)
	{
		if (amountForEachLevel == null || amountForEachLevel.length == 0) return this.pickListingForAllLevels((Map<Integer, Integer>)null); 
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < amountForEachLevel.length; ++i)
		{
			map.put(i + 1, amountForEachLevel[i]);
		}
		return this.pickListingForAllLevels(map);
	}
	
	/**
	 * Pick listing instances for all specified levels in the input map keys.
	 * @param amountForEachLevel How many Listing instances it should pick for each level.
	 * Null input or absent level value will be picked 1 instance.
	 * @return A list of Listing instances with ascending order in level. 
	 */
	public List<T> pickListingForSpecifiedLevels(@Nonnull Map<Integer, Integer> amountForEachLevel)
	{
		List<Integer> validLevels = this.allLevels();
		validLevels.removeIf(i -> !amountForEachLevel.containsKey(i));
		List<T> res = new ArrayList<>();
		for (int lv: validLevels)
		{
			res.addAll(this.pickListings(amountForEachLevel.get(lv), lv));
		}
		res.removeIf(t -> t == null || !t.isValid());
		return res;
	}
	
	/**
	 * Pick listing instances for levels from 1 to input length.
	 * @param amountForEachLevel How many Listing instances it should pick for each level.
	 * input[i] for level i+1.
	 * @return A list of Listing instances with ascending order in level. 
	 */
	public List<T> pickListingForSpecifiedLevels(int... amountForEachLevel)
	{
		if (amountForEachLevel.length == 0) return new ArrayList<>(); 
		Map<Integer, Integer> map = new HashMap<>();
		for (int i = 0; i < amountForEachLevel.length; ++i)
		{
			map.put(i + 1, amountForEachLevel[i]);
		}
		var res = this.pickListingForSpecifiedLevels(map);
		res.removeIf(t -> t == null || !t.isValid());
		return res;
	}
	
	@Override
	public String toString()
	{
		return "VanillaTradeListings{" + this.set.toHashSet().toString() + "}";
	}
}
