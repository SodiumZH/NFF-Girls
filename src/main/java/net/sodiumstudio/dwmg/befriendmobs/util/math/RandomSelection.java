package net.sodiumstudio.dwmg.befriendmobs.util.math;

import java.util.ArrayList;
import java.util.Random;

public class RandomSelection<T>
{

	protected Random rnd = new Random();
	protected ArrayList<Double> probSequence = new ArrayList<Double>();
	protected ArrayList<T> valSequence = new ArrayList<T>();
	protected T defaultVal = null;

	protected RandomSelection()
	{
	}

	public static <T> RandomSelection<T> create(T defaultValue) {
		return new RandomSelection<T>().defaultValue(defaultValue);
	}

	public static RandomSelection<Double> createDouble(double defaultValue)
	{
		return new RandomSelection<Double>().defaultValue(Double.valueOf(defaultValue));
	}
	
	public RandomSelection<T> add(T value, double probability) {
		double last = this.probSequence.size() > 0 ? this.probSequence.get(probSequence.size() - 1) : 0d;
		probSequence.add(last + probability);
		valSequence.add(value);
		return this;
	}
	
	public RandomSelection<T> defaultValue(T value) {
		defaultVal = value;
		return this;
	}

	public T getValue() {
		double rnd = this.rnd.nextDouble();
		for (int i = 0; i < probSequence.size(); ++i)
		{
			if (rnd < probSequence.get(i))
				return valSequence.get(i);
		}
		return defaultVal;
	}

	public double getDouble() {
		if (defaultVal instanceof Double)
			return ((Double) (getValue())).doubleValue();
		else
			throw new ClassCastException("RandomSelection::getDouble() must invoke on RandomSelection<Double>.");
	}
	
}
