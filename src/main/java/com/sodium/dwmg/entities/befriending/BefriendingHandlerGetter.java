package com.sodium.dwmg.entities.befriending;

// This is a wrapper of befriending method for a static value in the main class
public class BefriendingHandlerGetter {

	public BefriendingHandler method = new BefriendingHandler();
	
	public BefriendingHandlerGetter(BefriendingHandler method)
	{
		this.method = method;
	}
	
	public BefriendingHandlerGetter()
	{
		this.method = new BefriendingHandler();
	}
	
	public BefriendingHandler get() 
	{
		return method;
	}
	
	public void set(BefriendingHandler method)
	{
		this.method = method;
	}
	
}
