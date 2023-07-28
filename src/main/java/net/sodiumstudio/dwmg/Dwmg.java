package net.sodiumstudio.dwmg;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.registries.DwmgBlocks;
import net.sodiumstudio.dwmg.registries.DwmgEffects;
import net.sodiumstudio.dwmg.registries.DwmgEntityTypes;
import net.sodiumstudio.dwmg.registries.DwmgItems;

@Mod(Dwmg.MOD_ID)
public class Dwmg
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "dwmg";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Temporary BefriendMobAPI instance.
    // Will be removed after isolating BefriendMobAPI out as a library.
    //@SuppressWarnings("unused")
	//private static BefriendMobs TEMP_BM = new BefriendMobs();
    
    public static void logInfo(String info)
    {
    	LOGGER.info(info);
    }
    
    public Dwmg()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Set up registries
        DwmgEffects.EFFECTS.register(modEventBus);
        DwmgBlocks.BLOCKS.register(modEventBus);
        DwmgItems.ITEMS.register(modEventBus);
        DwmgEntityTypes.ENTITY_TYPES.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        
        
		//BefriendMobs.setDebugMode(true);
    }
    
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
        LOGGER.info("DWMG: Server started.");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents
    {
        @SuppressWarnings("resource")
		@SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("DwMG: Client started.");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

}
