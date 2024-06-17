package net.sodiumstudio.nautils.entity.vanillatrade;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import net.minecraft.util.Tuple;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.nautils.math.RandomSelection;

/**
 * A {@code RandomEnchantmentSelector} provides a set of enchantments for generating random offers of enchantment books.
 */
public class RandomEnchantmentSelector
{
	private final Map<Enchantment, Map<Integer, Double>> table = new HashMap<>();
	private RandomSelection<Tuple<Enchantment, Integer>> selector = null;
	private Enchantment lastRegistered = null;
	private boolean built = false;
	
	protected RandomEnchantmentSelector() {};
	
	public RandomEnchantmentSelector add(@Nonnull Enchantment enc, int level, double probabilityWeight)
	{
		if (level <= 0)
			throw new IllegalArgumentException("Non-positive level");
		if (probabilityWeight < 0)
			throw new IllegalArgumentException("Negative weight");
		if (!table.containsKey(enc))
			table.put(enc, new HashMap<>());
		table.get(enc).put(level, probabilityWeight);
		lastRegistered = enc;
		return this;
	}
	
	/**
	 * Add with weight == 1.0d.
	 */
	public RandomEnchantmentSelector add(int level, double probabilityWeight)
	{
		if (lastRegistered == null)
			throw new IllegalStateException("enchantment not specified. Use enchantment-specified versions at least once before using omitted versions.");
		return add(lastRegistered, level, probabilityWeight);
	}

	/**
	 * Add with omitted enchantment type - using the latest registered enchantment.
	 * <p>Take care using this in BuildEvent listeners. Do not call this on the first registration operation.
	 */
	public RandomEnchantmentSelector add(int level)
	{
		return add(level, 1.0d);
	}
	
	public RandomEnchantmentSelector remove(Enchantment enc, int level)
	{
		if (this.table.containsKey(enc))
		{
			this.table.get(enc).remove(level);
			if (this.table.get(enc).isEmpty())
				this.table.remove(enc);
		}
		return this;
	}
	
	/**
	 * Finally build the selector. NEVER CALL THIS IN {@code BuildEvent} LISTENERS!!!
	 */
	public RandomEnchantmentSelector build()
	{
		// Stacktrace check to prevent calling from event listener
		StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
		for (StackTraceElement e: stacktrace)
		{
			if (e.getMethodName().contains("getSelector") && e.getClassName().contains("BuildEvent"))
				throw new IllegalStateException("build error - building through event listener is not allowed.");
		}
		if (this.built)
			throw new IllegalStateException("build error - already built.");
		// Post the event, to allow external modification
		MinecraftForge.EVENT_BUS.post(new BuildEvent(this));
		
		if (table.isEmpty())
			throw new IllegalArgumentException("build error - no valid entry available.");
		double weightSum = 0d;
		for (var e: table.entrySet())
		{
			for (var w: e.getValue().entrySet())
			{
				weightSum += w.getValue();
			}
		}
		if (weightSum == 0)
			throw new IllegalArgumentException("build error - no valid entry available.");
		this.selector = RandomSelection.create(null);
		for (var e: table.entrySet())
		{
			for (var w: e.getValue().entrySet())
			{
				selector.add(new Tuple<>(e.getKey(), w.getKey()), w.getValue() / weightSum);
			}
		}
		built = true;
		return this;
	}
	
	public Tuple<Enchantment, Integer> getValue()
	{
		if (!this.built || this.selector == null)
			throw new IllegalStateException("getValue error - not built.");
		for (int i = 0; i < 100; ++i)
		{
			Tuple<Enchantment, Integer> res = selector.getValue();
			if (res != null)
				return res;
		}
		throw new RuntimeException("getValue error - failed to get a valid result after 100 tests. Usually failure shouldn't happen and this "
				+ "indicates an internal error.");
	}
	
	public static class BuildEvent extends Event
	{
		private final RandomEnchantmentSelector selector;
		public BuildEvent(RandomEnchantmentSelector s)
		{
			this.selector = s;
		}
		public RandomEnchantmentSelector getSelector()
		{
			return selector;
		}
	}

}
