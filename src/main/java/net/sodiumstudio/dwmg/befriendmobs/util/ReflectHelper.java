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
	
	@SuppressWarnings("unchecked")
	public static <T> T forceGetCasted(Object obj, Class<?> objClass, String fieldName)
	{
		Object result = null;
		T resultCast = null;
		try
		{
		Field fld = objClass.getDeclaredField(fieldName);
		fld.setAccessible(true);
		result = fld.get(obj);
		fld.setAccessible(false);
		resultCast = (T) result;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return resultCast;
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
