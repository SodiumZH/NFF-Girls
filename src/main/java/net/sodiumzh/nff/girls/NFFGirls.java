package net.sodiumzh.nff.girls;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
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
import net.sodiumzh.nff.services.NFFServices;
import net.sodiumzh.nff.services.registry.NFFCapabilityAttachments;

@Mod(NFFGirls.MOD_ID)
public class NFFGirls
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "nffgirls";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Temporary BefriendMobAPI instance.
    // Will be removed after isolating BefriendMobAPI out as a library.
    //@SuppressWarnings("unused")
	//private static NFFServices TEMP_BM = new NFFServices();
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
        redirectResourceLocations();
		//NFFServices.setDebugMode(true);
    }


}
