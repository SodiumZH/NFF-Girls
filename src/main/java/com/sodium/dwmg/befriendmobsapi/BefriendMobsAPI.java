package com.sodium.dwmg.befriendmobsapi;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.befriendmobsapi.entitiy.befriending.AbstractBefriendingHandler;
import com.sodium.dwmg.befriendmobsapi.registry.RegItems;
import com.sodium.dwmg.entities.befriending.BefriendingHandlerGetter;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// This class will be the mod main class of future Befriending Mob API library.

//@Mod(....)
public class BefriendMobsAPI {

	public static final String MOD_ID = Dwmg.MOD_ID;
	//public static final String MOD_ID = "befriendedmobsapi";
	public static String modDomain()
	{
		return "befriendmobsapi";	// Temporary for locating resource
	}
	
	public BefriendMobsAPI()
	{
		
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		//modEventBus.addListener(this::commonSetup);
		
		RegItems.ITEMS.register(modEventBus);
		
        //MinecraftForge.EVENT_BUS.register(this);
	}
	
    // You can reset this value to add implementations of befriending more mobs 
    private static BefriendingHandlerGetter befriendingHandlerGetter = new BefriendingHandlerGetter();

    public static AbstractBefriendingHandler getBefriendingHandler()
    {
    	return befriendingHandlerGetter.get();
    }
    
    public static void setBefriendingHandler(AbstractBefriendingHandler newHandler)
    {
    	befriendingHandlerGetter = new BefriendingHandlerGetter(newHandler);
    }
	
}
