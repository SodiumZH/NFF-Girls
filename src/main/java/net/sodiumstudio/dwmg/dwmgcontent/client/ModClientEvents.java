package net.sodiumstudio.dwmg.dwmgcontent.client;

import com.github.mechalopa.hmag.client.renderer.HuskGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.SkeletonGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.ZombieGirlRenderer;

import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedHuskGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedSkeletonGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedZombieGirl;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEntityTypes;
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
        event.registerEntityRenderer(DwmgEntityTypes.BEF_ZOMBIE_GIRL.get(), ZombieGirlRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.BEF_SKELETON_GIRL.get(), SkeletonGirlRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.BEF_HUSK_GIRL.get(), HuskGirlRenderer::new);
    }

	@SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(DwmgEntityTypes.BEF_ZOMBIE_GIRL.get(), EntityBefriendedZombieGirl.createAttributes().build());
        event.put(DwmgEntityTypes.BEF_SKELETON_GIRL.get(), EntityBefriendedSkeletonGirl.createAttributes().build());
        event.put(DwmgEntityTypes.BEF_HUSK_GIRL.get(), EntityBefriendedHuskGirl.createAttributes().build());
    }
}
