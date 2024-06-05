package net.sodiumstudio.nautils.entity.vanillatrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
		return res.stream().sorted().toList();
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
}
