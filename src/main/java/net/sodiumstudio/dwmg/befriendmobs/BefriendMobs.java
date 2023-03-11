package net.sodiumstudio.dwmg.befriendmobs;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;
import net.sodiumstudio.dwmg.befriendmobs.example.EXAMPLE_EntityTypeRegister;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegItems;

// This class will be the mod main class of future Befriending Mob API library.

//@Mod(....)
public class BefriendMobs {

	public static final String MOD_ID = Dwmg.MOD_ID;
	//public static final String MOD_ID = "befriendmobs";
	public static String modDomain()
	{
		return "befriendmobs";	// Temporary for locating resource
	}
	
	public BefriendMobs()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		//modEventBus.addListener(this::commonSetup);
		
		RegItems.ITEMS.register(modEventBus);
		EXAMPLE_EntityTypeRegister.EXAMPLE_ENTITY_TYPES.register(modEventBus);
		
        //MinecraftForge.EVENT_BUS.register(this);
	}	
	
    public static final boolean IS_DEBUG_MODE = true;
	
}
