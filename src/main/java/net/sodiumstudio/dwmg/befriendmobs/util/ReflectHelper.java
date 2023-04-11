package net.sodiumstudio.dwmg.befriendmobs.util;

import java.lang.reflect.Field;

public class ReflectHelper
{
	public static Object forceGet(Object obj, Class<?> objClass, String fieldName)
	{
		Object result = null;
		try
		{
		Field fld = objClass.getDeclaredField(fieldName);
		fld.setAccessible(true);
		result = fld.get(obj);
		fld.setAccessible(false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return result;
	}
	
	public static void forceSet(Object obj, Class<?> objClass, String fieldName, Object value)
	{
		try
		{
		Field fld = objClass.getDeclaredField(fieldName);
		fld.setAccessible(true);
		fld.set(obj, value);
		fld.setAccessible(false);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
