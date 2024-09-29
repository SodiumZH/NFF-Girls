package net.sodiumzh.nff.services;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumzh.nautils.NaUtils;
import net.sodiumzh.nautils.savedata.redirector.SaveDataLocationRedirector;
import net.sodiumzh.nff.services.registry.NFFCapabilityAttachments;
import net.sodiumzh.nff.services.registry.NFFItemRegistry;

// This class will be the mod main class of future Befriending Mob API library.

@Mod(NFFServices.MOD_ID)
public class NFFServices {

	public static final String MOD_ID = "nffservices";
	public static final Logger LOGGER = LogUtils.getLogger();
	private static final String MOD_ID_LEGACY = "befriendmobs";
	
	/**
	 * @deprecated use {@code MOD_ID} instead
	 */
	@Deprecated
	public static String modDomain()
	{
		return MOD_ID;	// Temporary for locating resource
	}
	
	public NFFServices()
	{
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		//modEventBus.addListener(this::commonSetup);
		
		NFFItemRegistry.ITEMS.register(modEventBus);
		//EXAMPLE_EntityTypeRegister.EXAMPLE_ENTITY_TYPES.register(modEventBus);
		
        MinecraftForge.EVENT_BUS.register(this);
        redirectResourceLocations();
	}	
	
	// On debug set it true. On publication set it false to block verbose debug output.
    public static boolean IS_DEBUG_MODE = false;
	
    public static void setDebugMode(boolean value)
    {
    	IS_DEBUG_MODE = value;
    }
    
    private static void redirectResourceLocations()
    {
    	SaveDataLocationRedirector.get()
    	.redirectNamespace(NFFServices.MOD_ID_LEGACY, NFFServices.MOD_ID)
    	.redirectEntityCapability(new ResourceLocation(NFFServices.MOD_ID_LEGACY, NFFCapabilityAttachments.KEY_NFF_TAMABLE_LEGACY), new ResourceLocation(NFFServices.MOD_ID, NFFCapabilityAttachments.KEY_NFF_TAMABLE))
    	.redirectEntityCapability(new ResourceLocation(NFFServices.MOD_ID_LEGACY, NFFCapabilityAttachments.KEY_NFF_MOB_COMMON_DATA_LEGACY), new ResourceLocation(NFFServices.MOD_ID, NFFCapabilityAttachments.KEY_NFF_MOB_COMMON_DATA))
    	.redirectEntityCapability(new ResourceLocation(NFFServices.MOD_ID_LEGACY, NFFCapabilityAttachments.KEY_HEALING_HANDLER_LEGACY), new ResourceLocation(NFFServices.MOD_ID, NFFCapabilityAttachments.KEY_HEALING_HANDLER))
    	.redirectEntityCapability(new ResourceLocation(NFFServices.MOD_ID_LEGACY, NFFCapabilityAttachments.KEY_ATTRIBUTE_MONOTOR_LEGACY), new ResourceLocation(NFFServices.MOD_ID, NFFCapabilityAttachments.KEY_ATTRIBUTE_MONOTOR))
    	.redirectEntityCapability(new ResourceLocation(NFFServices.MOD_ID_LEGACY, NFFCapabilityAttachments.KEY_DELAYED_ACTION_HANDLER_LEGACY), new ResourceLocation(NFFServices.MOD_ID, NFFCapabilityAttachments.KEY_DELAYED_ACTION_HANDLER))
    	.redirectEntityCapability(new ResourceLocation(NFFServices.MOD_ID_LEGACY, NFFCapabilityAttachments.KEY_ITEM_STACK_MONITOR_LEGACY), new ResourceLocation(NFFServices.MOD_ID, NFFCapabilityAttachments.KEY_ITEM_STACK_MONITOR))
    	.redirectLevelCapability(new ResourceLocation(NFFServices.MOD_ID_LEGACY, NFFCapabilityAttachments.KEY_NFF_LEVEL_LEGACY), new ResourceLocation(NFFServices.MOD_ID, NFFCapabilityAttachments.KEY_NFF_LEVEL))
    	.redirectEntityCapability(new ResourceLocation(NFFServices.MOD_ID_LEGACY, NFFCapabilityAttachments.KEY_NFF_PLAYER_LEGACY), new ResourceLocation(NFFServices.MOD_ID, NFFCapabilityAttachments.KEY_NFF_PLAYER));
    }

}
