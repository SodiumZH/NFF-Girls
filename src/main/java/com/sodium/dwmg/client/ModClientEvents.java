package com.sodium.dwmg.client;

import com.github.mechalopa.hmag.client.renderer.ZombieGirlRenderer;
import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.entities.ZombieGirlFriendly;
import com.sodium.dwmg.registries.ModEntityTypes;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModClientEvents 
{

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
    
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntityTypes.ZOMBIE_GIRL_FRIENDLY.get(), ZombieGirlRenderer::new);
    }

	@SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.ZOMBIE_GIRL_FRIENDLY.get(), ZombieGirlFriendly.createAttributes().build());
    }
}
