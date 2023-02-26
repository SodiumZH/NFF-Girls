package com.sodium.dwmg;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import com.sodium.dwmg.entities.befriending.BefriendingHandler;
import com.sodium.dwmg.entities.befriending.BefriendingHandlerGetter;
import com.sodium.dwmg.registries.*;
import com.sodium.dwmg.util.Debug;
// The value here should match an entry in the META-INF/mods.toml file
@Mod(Dwmg.MOD_ID)
public class Dwmg
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "dwmg";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    // Whether this project is under debug, or for release.
    // This controls if debug outputs should appear.
    public static final boolean IS_DEBUG = true;
    
    public static void logInfo(String info)
    {
    	LOGGER.info(info);
    }
    
    public Dwmg()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for mod loading
        modEventBus.addListener(this::commonSetup);

        // Set up registries
        ModEffects.EFFECTS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModEntityTypes.ENTITY_TYPES.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
   
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("DwMG: Server started.");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("DwMG: Client started.");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
    
    /* Utils */
    
    // You can reset this value to add implementations of befriending more mobs 
    public static BefriendingHandlerGetter befriendingHandlerGetter = new BefriendingHandlerGetter();
    
    public static void setBefriendingHandler(BefriendingHandler newHandler)
    {
    	befriendingHandlerGetter = new BefriendingHandlerGetter(newHandler);
    }
}
