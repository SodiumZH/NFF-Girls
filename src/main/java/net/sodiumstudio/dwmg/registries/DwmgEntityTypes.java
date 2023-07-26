package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.world.entity.HarpyEntity;
import com.github.mechalopa.hmag.world.entity.ImpEntity;
import com.github.mechalopa.hmag.world.entity.SnowCanineEntity;

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
import net.sodiumstudio.dwmg.entities.hmag.BefriendedHarpyEntity;
import net.sodiumstudio.dwmg.entities.hmag.BefriendedSnowCanineEntity;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedBanshee;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedCreeperGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedDrownedGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedEnderExecutor;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedGhastlySeeker;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedHornet;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedHuskGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedImp;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedKobold;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedNecroticReaper;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedSkeletonGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedStrayGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedWitherSkeletonGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedZombieGirl;
import net.sodiumstudio.dwmg.entities.projectile.BefriendedGhastFireball;
import net.sodiumstudio.dwmg.entities.projectile.NecromancerMagicBulletEntity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Dwmg.MOD_ID);
	
	public static final RegistryObject<EntityType<EntityBefriendedZombieGirl>> HMAG_ZOMBIE_GIRL = 
			ENTITY_TYPES.register("hmag_zombie_girl", () -> EntityType.Builder.of(
			EntityBefriendedZombieGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_zombie_girl").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedSkeletonGirl>> HMAG_SKELETON_GIRL = 
			ENTITY_TYPES.register("hmag_skeleton_girl", () -> EntityType.Builder.of(
			EntityBefriendedSkeletonGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_skeleton_girl").toString()));

	public static final RegistryObject<EntityType<EntityBefriendedHuskGirl>> HMAG_HUSK_GIRL = 
			ENTITY_TYPES.register("hmag_husk_girl", () -> EntityType.Builder.of(
			EntityBefriendedHuskGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_husk_girl").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedDrownedGirl>> HMAG_DROWNED_GIRL = 
			ENTITY_TYPES.register("hmag_drowned_girl", () -> EntityType.Builder.of(
			EntityBefriendedDrownedGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_drowned_girl").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedCreeperGirl>> HMAG_CREEPER_GIRL = 
			ENTITY_TYPES.register("hmag_creeper_girl", () -> EntityType.Builder.of(
			EntityBefriendedCreeperGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_creeper_girl").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedEnderExecutor>> HMAG_ENDER_EXECUTOR =
			ENTITY_TYPES.register("hmag_ender_executor", () -> EntityType.Builder.of(
			EntityBefriendedEnderExecutor::new, MobCategory.CREATURE)
			.sized(0.6F, 2.9F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_ender_executor").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedStrayGirl>> HMAG_STRAY_GIRL = 
			ENTITY_TYPES.register("hmag_stray_girl", () -> EntityType.Builder
			.of(EntityBefriendedStrayGirl::new, MobCategory.CREATURE)
			.immuneTo(Blocks.POWDER_SNOW)
			.sized(0.6F, 1.99F)			
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_stray_girl").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedWitherSkeletonGirl>> HMAG_WITHER_SKELETON_GIRL = 
			ENTITY_TYPES.register("hmag_wither_skeleton_girl", () -> EntityType.Builder
			.of(EntityBefriendedWitherSkeletonGirl::new, MobCategory.CREATURE)
			.fireImmune()
			.immuneTo(Blocks.WITHER_ROSE)
			.sized(0.7F, 2.4F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_wither_skeleton_girl").toString()));

	public static final RegistryObject<EntityType<EntityBefriendedHornet>> HMAG_HORNET = 
			ENTITY_TYPES.register("hmag_hornet", () -> EntityType.Builder
			.of(EntityBefriendedHornet::new, MobCategory.CREATURE)
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_hornet").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedNecroticReaper>> HMAG_NECROTIC_REAPER =
			ENTITY_TYPES.register("hmag_necrotic_reaper", () -> EntityType.Builder
			.of(EntityBefriendedNecroticReaper::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_necrotic_reaper").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedGhastlySeeker>> HMAG_GHASTLY_SEEKER = 
			ENTITY_TYPES.register("hmag_ghastly_seeker", () -> EntityType.Builder
			.of(EntityBefriendedGhastlySeeker::new, MobCategory.CREATURE)
			.fireImmune()
			.sized(0.9F, 2.9F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_ghastly_seeker").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedBanshee>> HMAG_BANSHEE = 
			ENTITY_TYPES.register("hmag_banshee", () -> EntityType.Builder
			.of(EntityBefriendedBanshee::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_banshee").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedKobold>> HMAG_KOBOLD =
			ENTITY_TYPES.register("hmag_kobold", () -> EntityType.Builder
			.of(EntityBefriendedKobold::new, MobCategory.CREATURE)
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_kobold").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedImp>> HMAG_IMP =
			ENTITY_TYPES.register("hmag_imp", () -> EntityType.Builder
			.of(EntityBefriendedImp::new, MobCategory.CREATURE)
			.fireImmune()
			.sized(0.6F, 1.7F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_imp").toString()));
	
	public static final RegistryObject<EntityType<BefriendedHarpyEntity>> HMAG_HARPY = 
			ENTITY_TYPES.register("hmag_harpy", () -> EntityType.Builder
			.of(BefriendedHarpyEntity::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.noSummon()
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_harpy").toString()));
	
	public static final RegistryObject<EntityType<BefriendedSnowCanineEntity>> HMAG_SNOW_CANINE = 
			ENTITY_TYPES.register("hmag_snow_canine", () -> EntityType.Builder
			.of(BefriendedSnowCanineEntity::new, MobCategory.MONSTER)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "hmag_snow_canine").toString()));
	
	@SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get(), EntityBefriendedZombieGirl.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_SKELETON_GIRL.get(), EntityBefriendedSkeletonGirl.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_HUSK_GIRL.get(), EntityBefriendedHuskGirl.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_DROWNED_GIRL.get(), EntityBefriendedDrownedGirl.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_CREEPER_GIRL.get(), EntityBefriendedCreeperGirl.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_ENDER_EXECUTOR.get(), EntityBefriendedEnderExecutor.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_STRAY_GIRL.get(), EntityBefriendedStrayGirl.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_WITHER_SKELETON_GIRL.get(), EntityBefriendedWitherSkeletonGirl.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_HORNET.get(), EntityBefriendedHornet.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_NECROTIC_REAPER.get(), EntityBefriendedNecroticReaper.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_GHASTLY_SEEKER.get(), EntityBefriendedGhastlySeeker.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_BANSHEE.get(), EntityBefriendedBanshee.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_KOBOLD.get(), EntityBefriendedKobold.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_IMP.get(), EntityBefriendedImp.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_HARPY.get(), BefriendedHarpyEntity.createAttributes().build());
        event.put(DwmgEntityTypes.HMAG_SNOW_CANINE.get(), BefriendedSnowCanineEntity.createAttributes().build());
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
	
	public static final RegistryObject<EntityType<BefriendedGhastFireball>> BEFRIENDED_GHAST_FIREBALL = 
			ENTITY_TYPES.register("befriended_ghast_fireball", () -> EntityType.Builder
			.<BefriendedGhastFireball>of(BefriendedGhastFireball::new, MobCategory.MISC)
			.sized(1.0F, 1.0F)
			.clientTrackingRange(4)
			.updateInterval(10)
			.build(new ResourceLocation(Dwmg.MOD_ID, "befriended_ghast_fireball").toString()));

}