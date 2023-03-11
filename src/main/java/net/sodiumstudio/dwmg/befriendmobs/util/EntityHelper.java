package net.sodiumstudio.dwmg.befriendmobs.util;

import java.util.Random;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.registries.ModCapabilities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

// Static function library for befriending-related actions.
public class EntityHelper {

	/** Replace a living entity with another one.
	 *  Only works in server. Calling in client always returns null.
	 * 
	 * @param newType EntityType of the new entity.
	 * @param from The entity to be replaced.
	 * @return New entity
	 */
	@Deprecated
	public static Entity replaceLivingEntity(EntityType<?> newType, LivingEntity from, boolean allowNewEntityDespawn)
	{
		if (from.level.isClientSide())
			return null;

		Entity newEntity = newType.create(from.level);
		newEntity.moveTo(from.getX(), from.getY(), from.getZ(), from.getYRot(), from.getXRot());

		if (from.hasCustomName())
		{
           newEntity.setCustomName(from.getCustomName());
           newEntity.setCustomNameVisible(from.isCustomNameVisible());
        }

		if (newEntity instanceof LivingEntity living)
		{
			living.yBodyRot = from.yBodyRot;
			living.setItemInHand(InteractionHand.MAIN_HAND, from.getItemBySlot(EquipmentSlot.MAINHAND));
			living.setItemInHand(InteractionHand.OFF_HAND, from.getItemBySlot(EquipmentSlot.OFFHAND));
			living.setItemSlot(EquipmentSlot.HEAD, from.getItemBySlot(EquipmentSlot.HEAD));
			living.setItemSlot(EquipmentSlot.CHEST, from.getItemBySlot(EquipmentSlot.CHEST));
			living.setItemSlot(EquipmentSlot.LEGS, from.getItemBySlot(EquipmentSlot.LEGS));
			living.setItemSlot(EquipmentSlot.FEET, from.getItemBySlot(EquipmentSlot.FEET));
			if(from instanceof Mob fromMob && newEntity instanceof Mob newMob)
			{
				if (!allowNewEntityDespawn || fromMob.isPersistenceRequired()) 
					newMob.setPersistenceRequired();
				newMob.setBaby(fromMob.isBaby());
	        }
		}

        newEntity.setInvulnerable(from.isInvulnerable());
        from.level.addFreshEntity(newEntity);
        from.discard();
		return newEntity;
	}
	
	@Deprecated() // TODO: use replaceMob instead
	public static Entity replaceLivingEntity(EntityType<?> newType, LivingEntity from)
	{
		return replaceLivingEntity(newType, from, false);
	}
	
	public static <T extends Mob> Mob replaceMob(EntityType<T> newType, Mob from, boolean allowNewEntityDespawn)
	{
		if (!allowNewEntityDespawn)
			from.setPersistenceRequired();
		return from.convertTo(newType, true);
	}
	
	public static <T extends Mob> Mob replaceMob(EntityType<T> newType, Mob from)
	{
		return replaceMob(newType, from, false);
	}
	
	public static void sendParticlesToMob(LivingEntity entity, ParticleOptions options, Vec3 offset, int amount, double speed, double positionRndScale, double speedRndScale)
	{
		if (entity.level.isClientSide)
			return;
		Vec3 pos = entity.position();
		for(int i = 0; i < amount; ++i) {
			double d0 = new Random().nextGaussian() * 0.1 * positionRndScale;
			double d1 = new Random().nextGaussian() * 0.2 * positionRndScale;
			double d2 = new Random().nextGaussian() * 0.1 * positionRndScale;
			double d3 = new Random().nextGaussian() * 0.5 * speedRndScale + 1;
			((ServerLevel)(entity.level)).sendParticles(options, pos.x + offset.x + d0, pos.y + entity.getBbHeight() + offset.y + d1, pos.z + offset.z + d2, 1, 0, 0, 0, speed * d3);
		}
	}
	
	public static void sendParticlesToMob(LivingEntity entity, ParticleOptions options, Vec3 offset, int amount, double speed)
	{
		sendParticlesToMob(entity, options, offset, amount, speed, 1, 1);
	}
	
	public static void sendHeartParticlesToMob(LivingEntity entity)
	{
		sendParticlesToMob(entity, ParticleTypes.HEART, new Vec3(0, -0.5, 0), 5, 5, 4, 1);
	}
	
	public static void sendStarParticlesToMob(LivingEntity entity)
	{
		sendParticlesToMob(entity, ParticleTypes.HAPPY_VILLAGER, new Vec3(0, -0.5, 0), 10, 0, 5, 0);
	}
	
	public static void sendSmokeParticlesToMob(LivingEntity entity)
	{
		sendParticlesToMob(entity, ParticleTypes.LARGE_SMOKE, new Vec3(0, -0.5, 0), 5, 5, 10, -10);
	}
	
	public static void sendAngryParticlesToMob(LivingEntity entity)
	{
		sendParticlesToMob(entity, ParticleTypes.ANGRY_VILLAGER, new Vec3(0, -0.5, 0), 5, 5, 3, 1);
	}
	
} 
