package net.sodiumstudio.dwmg.Util;

import java.lang.reflect.Method;

/**
 * Utility functions.
 * If the function isn't DWMG-dependent, it will be put into BefriendMobs on next update.
 */
public class DwmgUtil
{
	/**
	 * Force invoke a unaccessible method without return value.
	 */
	public static void forceInvoke(Object obj, Class<?> objClass, String methodName, Object... params)
	{
		try
		{
		Method method = objClass.getDeclaredMethod(methodName);
		method.setAccessible(true);
		method.invoke(obj, params);
		method.setAccessible(false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Force invoke a unaccessible method with return value.
	 */
	public static Object forceInvokeRetVal(Object obj, Class<?> objClass, String methodName, Object... params)
	{
		Object result = null;
		try
		{
		Method method = objClass.getDeclaredMethod(methodName);
		method.setAccessible(true);
		result = method.invoke(obj, params);
		method.setAccessible(false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
}
