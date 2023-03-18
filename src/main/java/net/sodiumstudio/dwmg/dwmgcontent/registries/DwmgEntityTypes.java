package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedCreeperGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedDrownedGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedHuskGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedSkeletonGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedZombieGirl;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Dwmg.MOD_ID);
	
	public static final RegistryObject<EntityType<EntityBefriendedZombieGirl>> BEF_ZOMBIE_GIRL = 
			ENTITY_TYPES.register("bef_zombie_girl", () -> EntityType.Builder.of(
			EntityBefriendedZombieGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "bef_zombie_girl").toString()));
	public static final RegistryObject<EntityType<EntityBefriendedSkeletonGirl>> BEF_SKELETON_GIRL = 
			ENTITY_TYPES.register("bef_skeleton_girl", () -> EntityType.Builder.of(
			EntityBefriendedSkeletonGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "bef_skeleton_girl").toString()));

	public static final RegistryObject<EntityType<EntityBefriendedHuskGirl>> BEF_HUSK_GIRL = 
			ENTITY_TYPES.register("bef_husk_girl", () -> EntityType.Builder.of(
			EntityBefriendedHuskGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "husk_girl").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedDrownedGirl>> BEF_DROWNED_GIRL = 
			ENTITY_TYPES.register("bef_drowned_girl", () -> EntityType.Builder.of(
			EntityBefriendedDrownedGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "drowned_girl").toString()));
	
	public static final RegistryObject<EntityType<EntityBefriendedCreeperGirl>> BEF_CREEPER_GIRL = 
			ENTITY_TYPES.register("bef_creeper_girl", () -> EntityType.Builder.of(
			EntityBefriendedCreeperGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "creeper_girl").toString()));
	
	@SubscribeEvent
    public static void onAttributeCreate(EntityAttributeCreationEvent event) {
        event.put(DwmgEntityTypes.BEF_ZOMBIE_GIRL.get(), EntityBefriendedZombieGirl.createAttributes().build());
        event.put(DwmgEntityTypes.BEF_SKELETON_GIRL.get(), EntityBefriendedSkeletonGirl.createAttributes().build());
        event.put(DwmgEntityTypes.BEF_HUSK_GIRL.get(), EntityBefriendedHuskGirl.createAttributes().build());
        event.put(DwmgEntityTypes.BEF_DROWNED_GIRL.get(), EntityBefriendedDrownedGirl.createAttributes().build());
        event.put(DwmgEntityTypes.BEF_CREEPER_GIRL.get(), EntityBefriendedCreeperGirl.createAttributes().build());
	}
	
}