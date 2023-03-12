package net.sodiumstudio.dwmg.befriendmobs.util;


/**
 * A class to hide variable inside to 
 * enable changing in final variables or lambda functions
 * @param <T> Type of wrapped variable
 */
public class Wrapped<T> {
	
	private T value;
	
	public Wrapped(T value)
	{
		this.value = value;
	}
	
	public T get()
	{
		return value;
	}
	
	public void set(T value)
	{
		this.value = value;
	}
	
}
