package net.sodiumzh.nff.girls;

import org.slf4j.Logger;
import net.sodiumzh.nff.girls.registry.*;
import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumzh.nautils.savedata.redirector.SaveDataLocationRedirector;
import net.sodiumzh.nff.girls.registry.NFFGirlsBlocks;
import net.sodiumzh.nff.girls.registry.NFFGirlsConfigs;
import net.sodiumzh.nff.girls.registry.NFFGirlsEffects;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsParticleTypes;
import net.sodiumzh.nff.girls.registry.NFFGirlsPotions;
import net.sodiumzh.nff.girls.registry.NFFGirlsRecipes;

@Mod(NFFGirls.MOD_ID)
public class NFFGirls
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "nffgirls";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final String MOD_ID_LEGACY = "dwmg";
    
    public static void logInfo(String info)
    {
    	LOGGER.info(info);
    }
    
    public NFFGirls()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        // Config
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NFFGirlsConfigs.CONFIG);
        modEventBus.addListener(NFFGirlsConfigs::loadConfig);
        
        // Set up registries
        NFFGirlsEffects.EFFECTS.register(modEventBus);
        NFFGirlsBlocks.BLOCKS.register(modEventBus);
        NFFGirlsItems.ITEMS.register(modEventBus);
        NFFGirlsEntityTypes.ENTITY_TYPES.register(modEventBus);
        NFFGirlsRecipes.RECIPES.register(modEventBus);
        NFFGirlsParticleTypes.PARTICLE_TYPES.register(modEventBus);
        NFFGirlsPotions.POTIONS.register(modEventBus);
        
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        redirectSaveDataLocations();
    }
    
    private static void redirectSaveDataLocations()
    {
		SaveDataLocationRedirector.get().redirectNamespace(MOD_ID_LEGACY, MOD_ID)
		.redirectEntityCapability(new ResourceLocation(NFFGirls.MOD_ID_LEGACY, NFFGirlsCapabilityAttachment.KEY_FAVORABILITY_LEGACY), new ResourceLocation(NFFGirls.MOD_ID, NFFGirlsCapabilityAttachment.KEY_FAVORABILITY))
		.redirectEntityCapability(new ResourceLocation(NFFGirls.MOD_ID_LEGACY, NFFGirlsCapabilityAttachment.KEY_TRADE_LEGACY), new ResourceLocation(NFFGirls.MOD_ID, NFFGirlsCapabilityAttachment.KEY_TRADE))
		.redirectEntityCapability(new ResourceLocation(NFFGirls.MOD_ID_LEGACY, NFFGirlsCapabilityAttachment.KEY_UNDEAD_AFFINITY_HANDLER_LEGACY), new ResourceLocation(NFFGirls.MOD_ID, NFFGirlsCapabilityAttachment.KEY_UNDEAD_AFFINITY_HANDLER))
		.redirectEntityCapability(new ResourceLocation(NFFGirls.MOD_ID_LEGACY, NFFGirlsCapabilityAttachment.KEY_XP_LEVEL_LEGACY), new ResourceLocation(NFFGirls.MOD_ID, NFFGirlsCapabilityAttachment.KEY_XP_LEVEL));
    }

}
