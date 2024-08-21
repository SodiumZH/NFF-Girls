package net.sodiumstudio.befriendmobs;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumstudio.befriendmobs.registry.BMItems;
import net.sodiumstudio.nautils.NaUtils;

// This class will be the mod main class of future Befriending Mob API library.

@Mod(BefriendMobs.MOD_ID)
public class BefriendMobs {

	public static final String MOD_ID = "befriendmobs";
	public static final Logger LOGGER = LogUtils.getLogger();
	
	/**
	 * @deprecated use {@code MOD_ID} instead
	 */
	@Deprecated
	public static String modDomain()
	{
		return "befriendmobs";	// Temporary for locating resource
	}
	
	public BefriendMobs()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		//modEventBus.addListener(this::commonSetup);
		
		BMItems.ITEMS.register(modEventBus);
		//EXAMPLE_EntityTypeRegister.EXAMPLE_ENTITY_TYPES.register(modEventBus);
		
        //MinecraftForge.EVENT_BUS.register(this);
		new NaUtils();	// TODO remove after NaUtils separation
	}	
	
	// On debug set it true. On publication set it false to block verbose debug output.
    public static boolean IS_DEBUG_MODE = false;
	
    public static void setDebugMode(boolean value)
    {
    	IS_DEBUG_MODE = value;
    }
}
