package net.sodiumstudio.dwmg.befriendmobs.example;

import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EXAMPLE_EntityClientEvents 
{

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
    
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EXAMPLE_EntityTypeRegister.EXAMPLE_BEFRIENDED_ZOMBIE.get(), ZombieRenderer::new);
        event.registerEntityRenderer(EXAMPLE_EntityTypeRegister.EXAMPLE_BEFRIENDABLE_ZOMBIE.get(), ZombieRenderer::new);
    }

	@SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(EXAMPLE_EntityTypeRegister.EXAMPLE_BEFRIENDED_ZOMBIE.get(), Zombie.createAttributes().build());
        event.put(EXAMPLE_EntityTypeRegister.EXAMPLE_BEFRIENDABLE_ZOMBIE.get(), Zombie.createAttributes().build());
    }
}
