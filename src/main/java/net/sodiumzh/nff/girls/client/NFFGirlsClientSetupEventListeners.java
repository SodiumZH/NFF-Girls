package net.sodiumzh.nff.girls.client;

import com.github.mechalopa.hmag.client.renderer.AlrauneRenderer;
import com.github.mechalopa.hmag.client.renderer.BansheeRenderer;
import com.github.mechalopa.hmag.client.renderer.CrimsonSlaughtererRenderer;
import com.github.mechalopa.hmag.client.renderer.CursedDollRenderer;
import com.github.mechalopa.hmag.client.renderer.DodomekiRenderer;
import com.github.mechalopa.hmag.client.renderer.DrownedGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.DullahanRenderer;
import com.github.mechalopa.hmag.client.renderer.GhastlySeekerRenderer;
import com.github.mechalopa.hmag.client.renderer.GlaryadRenderer;
import com.github.mechalopa.hmag.client.renderer.HarpyRenderer;
import com.github.mechalopa.hmag.client.renderer.HornetRenderer;
import com.github.mechalopa.hmag.client.renderer.HuskGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.ImpRenderer;
import com.github.mechalopa.hmag.client.renderer.JackFrostRenderer;
import com.github.mechalopa.hmag.client.renderer.KoboldRenderer;
import com.github.mechalopa.hmag.client.renderer.MagicBulletRenderer;
import com.github.mechalopa.hmag.client.renderer.MeltyMonsterRenderer;
import com.github.mechalopa.hmag.client.renderer.ModThrownItemRenderer;
import com.github.mechalopa.hmag.client.renderer.NecroticReaperRenderer;
import com.github.mechalopa.hmag.client.renderer.RedcapRenderer;
import com.github.mechalopa.hmag.client.renderer.SkeletonGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.SlimeGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.SnowCanineRenderer;
import com.github.mechalopa.hmag.client.renderer.StrayGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.WitherSkeletonGirlRenderer;
import com.github.mechalopa.hmag.client.renderer.ZombieGirlRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.sodiumzh.nff.girls.NFFGirls;
import net.sodiumzh.nff.girls.client.gui.screen.HmagBansheeGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagCreeperGirlGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagDodomekiGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagDullahanGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagEnderExecutorGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagGhastlySeekerGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagImpGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagJiangshiGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagKoboldGUI;
import net.sodiumzh.nff.girls.client.gui.screen.HmagMeltyMonsterGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsBowSecWeaponOneBaubleGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsEquipmentTwoBaublesGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsFourBaublesGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHandItemsSixBaublesGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHandItemsTwoBaublesGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagNecroticReaperGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagNightwalkerGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagRedcapGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsHmagSlimeGirlGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsSixBaublesGUI;
import net.sodiumzh.nff.girls.client.gui.screen.NFFGirlsThreeBaublesGUI;
import net.sodiumzh.nff.girls.client.particle.MagicalGelBallParticle;
import net.sodiumzh.nff.girls.client.renderer.NFFGirlsHmagCreeperGirlRenderer;
import net.sodiumzh.nff.girls.client.renderer.NFFGirlsHmagEnderExecutorRenderer;
import net.sodiumzh.nff.girls.inventory.HmagBansheeInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagDodomekiInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagEnderExecutorInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagGhastlySeekerInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagImpInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagJiangshiInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagKoboldInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagMeltyMonsterInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagNecroticReaperInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagNightwalkerInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagRedcapInventoryMenu;
import net.sodiumzh.nff.girls.inventory.HmagSlimeGirlInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsCreeperInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsEquipmentTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsFourBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsFourBaublesDefaultInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsSixBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsHandItemsTwoBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSixBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsSkeletonInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsThreeBaublesInventoryMenu;
import net.sodiumzh.nff.girls.inventory.NFFGirlsZombieGirlInventoryMenu;
import net.sodiumzh.nff.girls.item.ReinforcedFishingRodItem;
import net.sodiumzh.nff.girls.registry.NFFGirlsEntityTypes;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsParticleTypes;
import net.sodiumzh.nff.services.event.client.RegisterGuiScreenEvent;

@Mod.EventBusSubscriber(modid = NFFGirls.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NFFGirlsClientSetupEventListeners 
{

    @SubscribeEvent
    public static void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
    
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_ZOMBIE_GIRL.get(), ZombieGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_SKELETON_GIRL.get(), SkeletonGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_HUSK_GIRL.get(), HuskGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_DROWNED_GIRL.get(), DrownedGirlRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_CREEPER_GIRL.get(), NFFGirlsHmagCreeperGirlRenderer::new); 
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_ENDER_EXECUTOR.get(), NFFGirlsHmagEnderExecutorRenderer::new);  
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_STRAY_GIRL.get(), StrayGirlRenderer::new);  
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(), WitherSkeletonGirlRenderer::new); 
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_HORNET.get(), HornetRenderer::new); 
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_NECROTIC_REAPER.get(), NecroticReaperRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_GHASTLY_SEEKER.get(), GhastlySeekerRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_BANSHEE.get(), BansheeRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_KOBOLD.get(), KoboldRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_IMP.get(), ImpRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_HARPY.get(), HarpyRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_SNOW_CANINE.get(), SnowCanineRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_SLIME_GIRL.get(), SlimeGirlRenderer::new);
        //event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_JIANGSHI.get(), JiangshiRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_DULLAHAN.get(), DullahanRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_DODOMEKI.get(), DodomekiRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_ALRAUNE.get(), AlrauneRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_GLARYAD.get(), GlaryadRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_CRIMSON_SLAUGHTERER.get(), CrimsonSlaughtererRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_CURSED_DOLL.get(), CursedDollRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_REDCAP.get(), RedcapRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_JACK_FROST.get(), JackFrostRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_MELTY_MONSTER.get(), MeltyMonsterRenderer::new);
        //event.registerEntityRenderer(NFFGirlsEntityTypes.HMAG_NIGHTWALKER.get(), NightwalkerRenderer::new);
        
        event.registerEntityRenderer(NFFGirlsEntityTypes.NECROMANCER_MAGIC_BULLET.get(), MagicBulletRenderer::new); 
        event.registerEntityRenderer(NFFGirlsEntityTypes.BEFRIENDED_GHAST_FIREBALL.get(), c -> new ThrownItemRenderer<>(c, 3.0F, true));
        event.registerEntityRenderer(NFFGirlsEntityTypes.MAGICAL_GEL_BALL.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.ALRAUNE_POISON_SEED.get(), ModThrownItemRenderer::new);
        event.registerEntityRenderer(NFFGirlsEntityTypes.ALRAUNE_HEALING_SEED.get(), ModThrownItemRenderer::new);
        event.<FishingHook>registerEntityRenderer(NFFGirlsEntityTypes.REINFORCED_FISHING_HOOK.get(), FishingHookRenderer::new);
    }

    public static void onRegisterParticleProvider(ParticleFactoryRegisterEvent event)
    {
    	@SuppressWarnings("resource")
		Minecraft mc = Minecraft.getInstance();    	
    	mc.particleEngine.register(NFFGirlsParticleTypes.MAGICAL_GEL_BALL.get(), new MagicalGelBallParticle.Provider());
    }
    
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event)
    {
    	event.enqueueWork(() -> 
    	{
    		// Item properties
    		ItemProperties.register(NFFGirlsItems.REINFORCED_FISHING_ROD.get(), new ResourceLocation("cast"), ReinforcedFishingRodItem::isCastClient);
    	});
    }
    
    @SubscribeEvent
    public static void registerGuiScreen(RegisterGuiScreenEvent event)
    {
		event.registerDefault(NFFGirlsCreeperInventoryMenu.class, HmagCreeperGirlGUI::new);
		event.registerDefault(HmagEnderExecutorInventoryMenu.class, HmagEnderExecutorGUI::new);
		event.registerDefault(NFFGirlsEquipmentTwoBaublesInventoryMenu.class, NFFGirlsEquipmentTwoBaublesGUI::new);
		event.registerDefault(HmagGhastlySeekerInventoryMenu.class, HmagGhastlySeekerGUI::new);
		event.registerDefault(NFFGirlsHandItemsTwoBaublesInventoryMenu.class, NFFGirlsHandItemsTwoBaublesGUI::new);
		event.registerDefault(HmagNecroticReaperInventoryMenu.class, NFFGirlsHmagNecroticReaperGUI::new);
		event.registerDefault(NFFGirlsSkeletonInventoryMenu.class, NFFGirlsBowSecWeaponOneBaubleGUI::new);
		event.registerDefault(HmagBansheeInventoryMenu.class, HmagBansheeGUI::new);
		event.registerDefault(HmagKoboldInventoryMenu.class, HmagKoboldGUI::new);
		event.registerDefault(HmagImpInventoryMenu.class, HmagImpGUI::new);
		event.registerDefault(NFFGirlsFourBaublesInventoryMenu.class, NFFGirlsFourBaublesGUI::new);
		event.registerDefault(HmagSlimeGirlInventoryMenu.class, NFFGirlsHmagSlimeGirlGUI::new);
		event.registerDefault(NFFGirlsHandItemsFourBaublesDefaultInventoryMenu.class, HmagDullahanGUI::new);
		event.registerDefault(HmagDodomekiInventoryMenu.class, HmagDodomekiGUI::new);
		event.registerDefault(HmagJiangshiInventoryMenu.class, HmagJiangshiGUI::new);
		event.registerDefault(NFFGirlsThreeBaublesInventoryMenu.class, NFFGirlsThreeBaublesGUI::new);
		event.registerDefault(NFFGirlsSixBaublesInventoryMenu.class, NFFGirlsSixBaublesGUI::new);
		event.registerDefault(NFFGirlsHandItemsSixBaublesInventoryMenu.class, NFFGirlsHandItemsSixBaublesGUI::new);
		event.registerDefault(HmagRedcapInventoryMenu.class, NFFGirlsHmagRedcapGUI::new);
		event.registerDefault(HmagMeltyMonsterInventoryMenu.class, HmagMeltyMonsterGUI::new);
		event.registerDefault(HmagNightwalkerInventoryMenu.class, NFFGirlsHmagNightwalkerGUI::new);
		
		event.registerDefault(NFFGirlsZombieGirlInventoryMenu.class, NFFGirlsEquipmentTwoBaublesGUI::new);
    }

}
