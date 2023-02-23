package com.sodium.dwmg.entities.befriending;

// This is a wrapper of befriending method for a static value in the main class
public class BefriendingMethodGetter {

	public BefriendingMethod method = new BefriendingMethod();
	
	public BefriendingMethodGetter(BefriendingMethod method)
	{
		this.method = method;
	}
	
	public BefriendingMethodGetter()
	{
		this.method = new BefriendingMethod();
	}
	
	public BefriendingMethod get() 
	{
		return method;
	}
	
	public void set(BefriendingMethod method)
	{
		this.method = method;
	}
	
}
