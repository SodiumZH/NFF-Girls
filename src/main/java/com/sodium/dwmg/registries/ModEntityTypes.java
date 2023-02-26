package com.sodium.dwmg.registries;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.entities.EntityZombieGirlFriendly;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEntityTypes {

	public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Dwmg.MOD_ID);
	
	public static final RegistryObject<EntityType<EntityZombieGirlFriendly>> ZOMBIE_GIRL_FRIENDLY = ENTITY_TYPES.register("zombie_girl_friendly", () -> EntityType.Builder
			.of(EntityZombieGirlFriendly::new, MobCategory.CREATURE).sized(0.6F, 1.95F).setTrackingRange(8).setUpdateInterval(3).setShouldReceiveVelocityUpdates(false)
			.build(new ResourceLocation(Dwmg.MOD_ID, "zombie_girl_friendly").toString()));
	
}
