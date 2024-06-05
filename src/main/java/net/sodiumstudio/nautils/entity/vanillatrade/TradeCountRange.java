package net.sodiumstudio.nautils.entity.vanillatrade;

import java.util.Random;

import net.minecraft.util.RandomSource;

/**
 * A {@code TradeCountRange} is an integer range for generating
 * {@code MerchantOffer} from {@code VanillaTradeListing}. It can randomly
 * output one integer value within the range each time {@code getValue} is
 * called.
 * <p>
 * Now it supports 3 types of outputting: <i>fixed</i> (always outputting the
 * same value), <i>uniform</i> (all values within the range having the same
 * probability to be picked) and <i>Poisson</i> (the probabilities following the
 * Poisson distribution with specified parameter p).
 */
public class TradeCountRange
{
	private static final RandomSource RND = RandomSource.create();
	private final int minValue;	// Included
	private final int maxValue;	// Included
	private final double p;
	private final RandomizationType rndType;
	private int lastValue;
	private boolean lastValueValid = false;
	
	private TradeCountRange(int min, int max, double p, RandomizationType type)
	{
		this.minValue = min;
		this.maxValue = max;
		this.p = p;
		this.rndType = type;
		this.lastValue = min;
	}
	
	/**
	 * Get a {@code TradeCountRange} representing a fixed value.
	 */
	public static TradeCountRange fixed(int value)
	{
		var res = new TradeCountRange(value, value, 0.5d, RandomizationType.FIXED_VALUE);
		res.lastValueValid = true;
		return res;
	}
	
	/**
	 * Get a {@code TradeCountRange} with probabilities of Poisson distribution.
	 */
	public static TradeCountRange poisson(int min, int max, double p)
	{
		if (min > max) throw new IllegalArgumentException();
		return new TradeCountRange(min, max, p, RandomizationType.POISSON);
	}
	
	/**
	 * Get a {@code TradeCountRange} with probabilities of Poisson distribution with p = 0.5.
	 */
	public static TradeCountRange poisson(int min, int max)
	{
		return poisson(min, max, 0.5d);
	}
	
	/**
	 * Get a {@code TradeCountRange} with probabilities of uniform distribution.
	 */
	public static TradeCountRange uniform(int min, int max)
	{
		if (min > max) throw new IllegalArgumentException();
		return new TradeCountRange(min, max, 0.5, RandomizationType.UNIFORM);
	}

	public TradeCountRange setUniform()
	{
		return new TradeCountRange(this.minValue, this.maxValue, this.p, RandomizationType.UNIFORM);
	}
	
	/**
	 * Get a new {@code TradeCountRange} with the same min and max, using Poisson distributive randomization with p.
	 */
	public TradeCountRange setPoisson(double p)
	{
		return new TradeCountRange(this.minValue, this.maxValue, p, RandomizationType.POISSON);
	}

	public TradeCountRange setRange(int min, int max)
	{
		return new TradeCountRange(min, max, this.p, this.rndType);
	}

	/**
	 * Get a random value using the given RandomSource.
	 */
	public int getValue(RandomSource rnd)
	{
		switch (rndType)
		{
		case FIXED_VALUE:
		{
			this.lastValue = this.minValue;
			return this.minValue;
		}
		case UNIFORM:
		{
			int val = rnd.nextInt(minValue, maxValue + 1);
			this.lastValue = val;
			this.lastValueValid = true;
			return val;
		}
		case POISSON:
		{
			int res = minValue;
			for (int i = minValue; i < maxValue; ++i)
			{
				if (rnd.nextDouble() <= p) res++;
			}
			this.lastValue = res;
			this.lastValueValid = true;
			return res;
		}
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Get a random value using the internal RandomSource.
	 */
	public int getValue()
	{
		return this.getValue(RND);
	}
	
	/**
	 * Get the value last output.
	 * @throws IllegalStateException If it has never output via {@code getValue}.
	 */
	public int lastValue()
	{
		if (!this.lastValueValid)
			throw new IllegalStateException("NaUtils#VanillaMerchant#TradeCountRange: lastValue() requires to have run getValue() at least once already.");
		return this.lastValue;
	}
	
	private static enum RandomizationType
	{
		FIXED_VALUE,
		POISSON,
		UNIFORM
	}
}
