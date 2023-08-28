package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.world.entity.DodomekiEntity;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.hmag.HmagBansheeEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagCreeperGirlEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagDodomekiEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagDrownedGirlEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagDullahanEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagEnderExecutorEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagGhastlySeekerEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagHarpyEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagHornetEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagHuskGirlEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagImpEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagJiangshiEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagKoboldEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagNecroticReaperEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagSkeletonGirlEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagSlimeGirlEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagSnowCanineEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagStrayGirlEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagWitherSkeletonGirlEntity;
import net.sodiumstudio.dwmg.entities.hmag.HmagZombieGirlEntity;
import net.sodiumstudio.dwmg.entities.projectile.BefriendedGhastFireballEntity;
import net.sodiumstudio.dwmg.entities.projectile.MagicalGelBallEntity;
import net.sodiumstudio.dwmg.entities.projectile.NecromancerMagicBulletEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Dwmg.MOD_ID);
	
	public static final RegistryObject<EntityType<HmagZombieGirlEntity>> HMAG_ZOMBIE_GIRL = 
			ENTITY_TYPES.register("hmag_zombie_girl", () -> EntityType.Builder.of(
			HmagZombieGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_zombie_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagSkeletonGirlEntity>> HMAG_SKELETON_GIRL = 
			ENTITY_TYPES.register("hmag_skeleton_girl", () -> EntityType.Builder.of(
			HmagSkeletonGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_skeleton_girl").toString()));

	public static final RegistryObject<EntityType<HmagHuskGirlEntity>> HMAG_HUSK_GIRL = 
			ENTITY_TYPES.register("hmag_husk_girl", () -> EntityType.Builder.of(
			HmagHuskGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_husk_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagDrownedGirlEntity>> HMAG_DROWNED_GIRL = 
			ENTITY_TYPES.register("hmag_drowned_girl", () -> EntityType.Builder.of(
			HmagDrownedGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_drowned_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagCreeperGirlEntity>> HMAG_CREEPER_GIRL = 
			ENTITY_TYPES.register("hmag_creeper_girl", () -> EntityType.Builder.of(
			HmagCreeperGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_creeper_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagEnderExecutorEntity>> HMAG_ENDER_EXECUTOR =
			ENTITY_TYPES.register("hmag_ender_executor", () -> EntityType.Builder.of(
			HmagEnderExecutorEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 2.9F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_ender_executor").toString()));
	
	public static final RegistryObject<EntityType<HmagStrayGirlEntity>> HMAG_STRAY_GIRL = 
			ENTITY_TYPES.register("hmag_stray_girl", () -> EntityType.Builder
			.of(HmagStrayGirlEntity::new, MobCategory.CREATURE)
			.immuneTo(Blocks.POWDER_SNOW)
			.sized(0.6F, 1.99F)			
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_stray_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagWitherSkeletonGirlEntity>> HMAG_WITHER_SKELETON_GIRL = 
			ENTITY_TYPES.register("hmag_wither_skeleton_girl", () -> EntityType.Builder
			.of(HmagWitherSkeletonGirlEntity::new, MobCategory.CREATURE)
			.fireImmune()
			.immuneTo(Blocks.WITHER_ROSE)
			.sized(0.7F, 2.4F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_wither_skeleton_girl").toString()));

	public static final RegistryObject<EntityType<HmagHornetEntity>> HMAG_HORNET = 
			ENTITY_TYPES.register("hmag_hornet", () -> EntityType.Builder
			.of(HmagHornetEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_hornet").toString()));
	
	public static final RegistryObject<EntityType<HmagNecroticReaperEntity>> HMAG_NECROTIC_REAPER =
			ENTITY_TYPES.register("hmag_necrotic_reaper", () -> EntityType.Builder
			.of(HmagNecroticReaperEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_necrotic_reaper").toString()));
	
	public static final RegistryObject<EntityType<HmagGhastlySeekerEntity>> HMAG_GHASTLY_SEEKER = 
			ENTITY_TYPES.register("hmag_ghastly_seeker", () -> EntityType.Builder
			.of(HmagGhastlySeekerEntity::new, MobCategory.CREATURE)
			.fireImmune()
			.sized(0.9F, 2.9F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_ghastly_seeker").toString()));
	
	public static final RegistryObject<EntityType<HmagBansheeEntity>> HMAG_BANSHEE = 
			ENTITY_TYPES.register("hmag_banshee", () -> EntityType.Builder
			.of(HmagBansheeEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_banshee").toString()));
	
	public static final RegistryObject<EntityType<HmagKoboldEntity>> HMAG_KOBOLD =
			ENTITY_TYPES.register("hmag_kobold", () -> EntityType.Builder
			.of(HmagKoboldEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_kobold").toString()));
	
	public static final RegistryObject<EntityType<HmagImpEntity>> HMAG_IMP =
			ENTITY_TYPES.register("hmag_imp", () -> EntityType.Builder
			.of(HmagImpEntity::new, MobCategory.CREATURE)
			.fireImmune()
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_imp").toString()));
	
	public static final RegistryObject<EntityType<HmagHarpyEntity>> HMAG_HARPY = 
			ENTITY_TYPES.register("hmag_harpy", () -> EntityType.Builder
			.of(HmagHarpyEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_harpy").toString()));
	
	public static final RegistryObject<EntityType<HmagSnowCanineEntity>> HMAG_SNOW_CANINE = 
			ENTITY_TYPES.register("hmag_snow_canine", () -> EntityType.Builder
			.of(HmagSnowCanineEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_snow_canine").toString()));
	
	public static final RegistryObject<EntityType<HmagSlimeGirlEntity>> HMAG_SLIME_GIRL = 
			ENTITY_TYPES.register("hmag_slime_girl", () -> EntityType.Builder
			.of(HmagSlimeGirlEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_slime_girl").toString()));
	
	public static final RegistryObject<EntityType<HmagJiangshiEntity>> HMAG_JIANGSHI = 
			ENTITY_TYPES.register("hmag_jiangshi", () -> EntityType.Builder
			.of(HmagJiangshiEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_jiangshi").toString()));
	
	public static final RegistryObject<EntityType<HmagDullahanEntity>> HMAG_DULLAHAN = 
			ENTITY_TYPES.register("hmag_dullahan", () -> EntityType.Builder
			.of(HmagDullahanEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.75F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_dullahan").toString()));
	
	public static final RegistryObject<EntityType<HmagDodomekiEntity>> HMAG_DODOMEKI = 
			ENTITY_TYPES.register("hmag_dodomeki", () -> EntityType.Builder
			.of(HmagDodomekiEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_dodomeki").toString()));
	
	@SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get(), HmagZombieGirlEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_SKELETON_GIRL.get(), HmagSkeletonGirlEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_HUSK_GIRL.get(), HmagHuskGirlEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_DROWNED_GIRL.get(), HmagDrownedGirlEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_CREEPER_GIRL.get(), HmagCreeperGirlEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_ENDER_EXECUTOR.get(), HmagEnderExecutorEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_STRAY_GIRL.get(), HmagStrayGirlEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(), HmagWitherSkeletonGirlEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_HORNET.get(), HmagHornetEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_NECROTIC_REAPER.get(), HmagNecroticReaperEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_GHASTLY_SEEKER.get(), HmagGhastlySeekerEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_BANSHEE.get(), HmagBansheeEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_KOBOLD.get(), HmagKoboldEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_IMP.get(), HmagImpEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_HARPY.get(), HmagHarpyEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_SNOW_CANINE.get(), HmagSnowCanineEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_SLIME_GIRL.get(), HmagSlimeGirlEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_JIANGSHI.get(), DwmgAttributes.HMAG_JIANGSHI_ATTRIBUTES.get().build());
        event.put(DwmgEntityTypes.HMAG_DULLAHAN.get(), DwmgAttributes.HMAG_DULLAHAN_ATTRIBUTES.get().build());
        event.put(DwmgEntityTypes.HMAG_DODOMEKI.get(), DwmgAttributes.HMAG_DODOMEKI_ATTRIBUTES.get().build());
	}
	
	// Projectiles
	
	public static final RegistryObject<EntityType<NecromancerMagicBulletEntity>> NECROMANCER_MAGIC_BULLET = 
			ENTITY_TYPES.register("necromancer_magic_bullet", () -> EntityType.Builder
			.<NecromancerMagicBulletEntity>of(NecromancerMagicBulletEntity::new, MobCategory.MISC)
			.sized(0.3215F, 0.3215F)
			.setTrackingRange(4)
			.setUpdateInterval(1)
			.setShouldReceiveVelocityUpdates(true)
			.setCustomClientFactory(NecromancerMagicBulletEntity::new)
			.build(new ResourceLocation(Dwmg.MOD_ID, "necromancer_magic_bullet").toString()));
	
	public static final RegistryObject<EntityType<BefriendedGhastFireballEntity>> BEFRIENDED_GHAST_FIREBALL = 
			ENTITY_TYPES.register("befriended_ghast_fireball", () -> EntityType.Builder
			.<BefriendedGhastFireballEntity>of(BefriendedGhastFireballEntity::new, MobCategory.MISC)
			.sized(1.0F, 1.0F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(new ResourceLocation(Dwmg.MOD_ID, "befriended_ghast_fireball").toString()));

	public static final RegistryObject<EntityType<MagicalGelBallEntity>> MAGICAL_GEL_BALL = 
			ENTITY_TYPES.register("magical_gel_ball", () -> EntityType.Builder
			.<MagicalGelBallEntity>of(MagicalGelBallEntity::new, MobCategory.MISC)
			.sized(0.25F, 0.25F)		
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(new ResourceLocation(Dwmg.MOD_ID, "magical_gel_ball").toString()));
	
}