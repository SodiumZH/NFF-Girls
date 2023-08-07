package net.sodiumstudio.dwmg.util;

import java.util.function.Supplier;

import net.sodiumstudio.nautils.ReflectHelper;

public class DwmgMiscUtil
{
	/** Try an action with boolean result for given times. Once the action returns true, it will break and return true. 
	 Otherwise if the action returns all false for given times, it returns false. */
	public static boolean tryFor(int times, Supplier<Boolean> action)
	{
		if (times <= 0)
			return false;
		for (int i = 0; i < times; ++i)
		{
			boolean res = action.get();
			if (res)
				return true;
		}
		return false;
	}

	
}
