package com.sodium.dwmg.entities.befriending;

import com.sodium.dwmg.befriendmobsapi.entitiy.befriending.AbstractBefriendingHandler;

// This is a wrapper of befriending handler for a static value in the main class
public class BefriendingHandlerGetter {

	public AbstractBefriendingHandler handler = new BefriendingHandler();
	
	public BefriendingHandlerGetter(AbstractBefriendingHandler handler)
	{
		this.handler = handler;
	}
	
	public BefriendingHandlerGetter()
	{
		this.handler = new BefriendingHandler();
	}
	
	public AbstractBefriendingHandler get() 
	{
		return handler;
	}
	
	public void set(BefriendingHandler handler)
	{
		this.handler = handler;
	}
	
}
