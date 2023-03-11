package net.sodiumstudio.dwmg.befriendmobs.example;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedZombieGirl;

// Register the new mob type
// This is just the same as registering any other entity types. 
public class EXAMPLE_EntityTypeRegister
{
	public static final DeferredRegister<EntityType<?>> EXAMPLE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BefriendMobs.MOD_ID);
	
	public static final RegistryObject<EntityType<EXAMPLE_BefriendedZombie>> EXAMPLE_BEFRIENDED_ZOMBIE = EXAMPLE_ENTITY_TYPES.register(
			"example_befriended_zombie", () -> 
			EntityType.Builder.of(EXAMPLE_BefriendedZombie::new, MobCategory.CREATURE).sized(0.6F, 1.95F).setTrackingRange(8)
			.build(new ResourceLocation(Dwmg.MOD_ID, "example_befriended_zombie").toString()));
		
	public static final RegistryObject<EntityType<EXAMPLE_BefriendableZombie>> EXAMPLE_BEFRIENDABLE_ZOMBIE = EXAMPLE_ENTITY_TYPES.register(
		"example_befriendable_zombie", () -> 
		EntityType.Builder.of(EXAMPLE_BefriendableZombie::new, MobCategory.MONSTER).sized(0.6F, 1.95F).setTrackingRange(8)
		.build(new ResourceLocation(Dwmg.MOD_ID, "example_befriendable_zombie").toString()));
}