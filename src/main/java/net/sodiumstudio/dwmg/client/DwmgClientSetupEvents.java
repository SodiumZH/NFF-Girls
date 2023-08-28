package net.sodiumstudio.dwmg.client;

import com.github.mechalopa.hmag.client.renderer.BansheeRenderer;
import com.github.mechalopa.hmag.client.renderer.DodomekiRenderer;
import com.github.mechalopa.hmag.client.renderer.DrownedGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.DullahanRenderer;
import com.github.mechalopa.hmag.client.renderer.GhastlySeekerRenderer;
import com.github.mechalopa.hmag.client.renderer.HarpyRenderer;
import com.github.mechalopa.hmag.client.renderer.HornetRenderer;
import com.github.mechalopa.hmag.client.renderer.HuskGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.ImpRenderer;
import com.github.mechalopa.hmag.client.renderer.JiangshiRenderer;
import com.github.mechalopa.hmag.client.renderer.KoboldRenderer;
import com.github.mechalopa.hmag.client.renderer.MagicBulletRenderer;
import com.github.mechalopa.hmag.client.renderer.NecroticReaperRenderer;
import com.github.mechalopa.hmag.client.renderer.SkeletonGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.SlimeGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.SnowCanineRenderer;
import com.github.mechalopa.hmag.client.renderer.StrayGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.WitherSkeletonGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.ZombieGirlRenderer;

import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.sodiumstudio.befriendmobs.client.gui.screens.BefriendedGuiScreenMaker;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.client.gui.screens.GuiBanshee;
import net.sodiumstudio.dwmg.client.gui.screens.GuiBowSecWeaponOneBauble;
import net.sodiumstudio.dwmg.client.gui.screens.GuiCreeperGirl;
import net.sodiumstudio.dwmg.client.gui.screens.GuiDodomeki;
import net.sodiumstudio.dwmg.client.gui.screens.GuiDullahan;
import net.sodiumstudio.dwmg.client.gui.screens.GuiEnderExecutor;
import net.sodiumstudio.dwmg.client.gui.screens.GuiEquipmentTwoBaubles;
import net.sodiumstudio.dwmg.client.gui.screens.GuiFourBaubles;
import net.sodiumstudio.dwmg.client.gui.screens.GuiGhastlySeeker;
import net.sodiumstudio.dwmg.client.gui.screens.GuiHandItemsTwoBaubles;
import net.sodiumstudio.dwmg.client.gui.screens.GuiImp;
import net.sodiumstudio.dwmg.client.gui.screens.GuiKobold;
import net.sodiumstudio.dwmg.client.gui.screens.GuiNecroticReaper;
import net.sodiumstudio.dwmg.client.gui.screens.GuiSlimeGirl;
import net.sodiumstudio.dwmg.client.particles.MagicalGelBallParticle;
import net.sodiumstudio.dwmg.client.renderer.BefriendedCreeperGirlRenderer;
import net.sodiumstudio.dwmg.client.renderer.BefriendedEnderExecutorRenderer;
import net.sodiumstudio.dwmg.inventory.InventoryMenuBanshee;
import net.sodiumstudio.dwmg.inventory.InventoryMenuCreeper;
import net.sodiumstudio.dwmg.inventory.InventoryMenuDodomeki;
import net.sodiumstudio.dwmg.inventory.InventoryMenuDullahan;
import net.sodiumstudio.dwmg.inventory.InventoryMenuEnderExecutor;
import net.sodiumstudio.dwmg.inventory.InventoryMenuEquipmentTwoBaubles;
import net.sodiumstudio.dwmg.inventory.InventoryMenuFourBaubles;
import net.sodiumstudio.dwmg.inventory.InventoryMenuGhastlySeeker;
import net.sodiumstudio.dwmg.inventory.InventoryMenuHandItemsTwoBaubles;
import net.sodiumstudio.dwmg.inventory.InventoryMenuImp;
import net.sodiumstudio.dwmg.inventory.InventoryMenuKobold;
import net.sodiumstudio.dwmg.inventory.InventoryMenuNecroticReaper;
import net.sodiumstudio.dwmg.inventory.InventoryMenuSkeleton;
import net.sodiumstudio.dwmg.inventory.InventoryMenuSlimeGirl;
import net.sodiumstudio.dwmg.registries.DwmgEntityTypes;
import net.sodiumstudio.dwmg.registries.DwmgParticleTypes;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgClientSetupEvents 
{

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
    
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get(), ZombieGirlRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_SKELETON_GIRL.get(), SkeletonGirlRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_HUSK_GIRL.get(), HuskGirlRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_DROWNED_GIRL.get(), DrownedGirlRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_CREEPER_GIRL.get(), BefriendedCreeperGirlRenderer::new); 
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_ENDER_EXECUTOR.get(), BefriendedEnderExecutorRenderer::new);  
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_STRAY_GIRL.get(), StrayGirlRenderer::new);  
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(), WitherSkeletonGirlRenderer::new); 
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_HORNET.get(), HornetRenderer::new); 
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_NECROTIC_REAPER.get(), NecroticReaperRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_GHASTLY_SEEKER.get(), GhastlySeekerRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_BANSHEE.get(), BansheeRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_KOBOLD.get(), KoboldRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_IMP.get(), ImpRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_HARPY.get(), HarpyRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_SNOW_CANINE.get(), SnowCanineRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_SLIME_GIRL.get(), SlimeGirlRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_JIANGSHI.get(), JiangshiRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_DULLAHAN.get(), DullahanRenderer::new);
        event.registerEntityRenderer(DwmgEntityTypes.HMAG_DODOMEKI.get(), DodomekiRenderer::new);
        
        event.registerEntityRenderer(DwmgEntityTypes.NECROMANCER_MAGIC_BULLET.get(), MagicBulletRenderer::new); 
        event.registerEntityRenderer(DwmgEntityTypes.BEFRIENDED_GHAST_FIREBALL.get(), c -> new ThrownItemRenderer<>(c, 3.0F, true));
        event.registerEntityRenderer(DwmgEntityTypes.MAGICAL_GEL_BALL.get(), ThrownItemRenderer::new);
    }

    public static void onRegisterParticleProvider(RegisterParticleProvidersEvent event)
    {
    	event.register(DwmgParticleTypes.MAGICAL_GEL_BALL.get(), new MagicalGelBallParticle.Provider());
    }
    
	@SubscribeEvent
	public static void addGuiScreens(FMLClientSetupEvent event)
	{
		BefriendedGuiScreenMaker.put(InventoryMenuCreeper.class, (menu) -> new GuiCreeperGirl(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuEnderExecutor.class, (menu) -> new GuiEnderExecutor(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuEquipmentTwoBaubles.class, (menu) -> new GuiEquipmentTwoBaubles(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuGhastlySeeker.class, (menu) -> new GuiGhastlySeeker(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuHandItemsTwoBaubles.class, (menu) -> new GuiHandItemsTwoBaubles(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuNecroticReaper.class, (menu) -> new GuiNecroticReaper(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuSkeleton.class, (menu) -> new GuiBowSecWeaponOneBauble(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuBanshee.class, (menu) -> new GuiBanshee(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuKobold.class, (menu) -> new GuiKobold(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuImp.class, (menu) -> new GuiImp(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuFourBaubles.class, (menu) -> new GuiFourBaubles(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuSlimeGirl.class, (menu) -> new GuiSlimeGirl(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuDullahan.class, (menu) -> new GuiDullahan(menu, menu.playerInventory, menu.mob));
		BefriendedGuiScreenMaker.put(InventoryMenuDodomeki.class, (menu) -> new GuiDodomeki(menu, menu.playerInventory, menu.mob));
	}
	
}
