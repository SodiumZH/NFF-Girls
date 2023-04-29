package net.sodiumstudio.dwmg.befriendmobs.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;

public class ContainerHelper
{
	// Remove all elements fulfilling a condition from a set
	public static <T> void removeFromSet(Set<T> set, Predicate<T> condition)
	{
		HashSet<T> toRemove = new HashSet<T>();
		for (T t: set)
		{
			if (condition.test(t))
				toRemove.add(t);
		}
		for (T t: toRemove)
		{
			set.remove(t);
		}
	}
	
	// Remove all elements with the key fulfilling a condition from a map
	public static <T, U> void removeFromMapKey(Map<T, U> map, Predicate<T> keyCondition)
	{
		HashSet<T> toRemove = new HashSet<T>();
		for (T t: map.keySet())
		{
			if (keyCondition.test(t))
				toRemove.add(t);
		}
		for (T t: toRemove)
		{
			map.remove(t);
		}
	}
	
	// Remove all elements with the value fulfilling a condition from a map
	public static <T, U> void removeFromMapValue(Map<T, U> map, Predicate<U> valueCondition)
	{
		HashSet<T> toRemove = new HashSet<T>();
		for (T t: map.keySet())
		{
			if (valueCondition.test(map.get(t)))
				toRemove.add(t);
		}
		for (T t: toRemove)
		{
			map.remove(t);
		}
	}
	
	// Pick an element fulfilling the condition from a set.
	// If there are multiple, it will only pick one.
	public static <T> T pickSetElement(Set<T> set, Predicate<T> condition)
	{
		for (T t: set)
		{
			if (condition.test(t))
				return t;
		}
		return null;
	}
	
	// Pick all elements fulfilling the condition from a set.
	public static <T> HashSet<T> pickSetElements(Set<T> set, Predicate<T> condition)
	{
		HashSet<T> out = new HashSet<T>();
		for (T t: set)
		{
			if (condition.test(t))
				out.add(t);
		}
		return out;
	}
	
}
