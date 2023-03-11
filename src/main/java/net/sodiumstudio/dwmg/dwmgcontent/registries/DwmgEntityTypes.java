package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedSkeletonGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedZombieGirl;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Dwmg.MOD_ID);
	
	public static final RegistryObject<EntityType<EntityBefriendedZombieGirl>> BEF_ZOMBIE_GIRL = ENTITY_TYPES.register("bef_zombie_girl", () -> EntityType.Builder.of(
			EntityBefriendedZombieGirl::new, MobCategory.CREATURE)
			.sized(0.6F, 1.95F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "bef_zombie_girl").toString()));
	public static final RegistryObject<EntityType<EntityBefriendedSkeletonGirl>> BEF_SKELETON_GIRL = ENTITY_TYPES.register("skeleton_girl", () -> EntityType.Builder.of(
			EntityBefriendedSkeletonGirl::new, MobCategory.MONSTER)
			.sized(0.6F, 1.99F)
			.setTrackingRange(8)
			.setUpdateInterval(3)
			.setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "bef_skeleton_girl").toString()));
	
}