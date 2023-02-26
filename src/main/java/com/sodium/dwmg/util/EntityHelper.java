package com.sodium.dwmg.util;

import java.util.Random;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.registries.ModCapabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;

// Static function library for befriending-related actions.
public class EntityHelper {

	/** Replace a living entity with another one.
	 * 
	 * @param newType EntityType of the new entity.
	 * @param from The entity to be replaced.
	 * @return New entity
	 */
	public static Entity replaceLivingEntity(EntityType<?> newType, LivingEntity from)
	{
		if (from.level.isClientSide())
		{
			return null;
		}
		Entity newEntity = newType.create(from.level);
		newEntity.moveTo(from.getX(), from.getY(), from.getZ(), from.getYRot(), from.getXRot());
		if(newEntity instanceof LivingEntity living)
			living.yBodyRot = from.yBodyRot;
		if (from.hasCustomName()) {
           newEntity.setCustomName(from.getCustomName());
           newEntity.setCustomNameVisible(from.isCustomNameVisible());
        }

		if(from instanceof Mob fromMob && newEntity instanceof Mob newMob)
			if (fromMob.isPersistenceRequired()) {
				newMob.setPersistenceRequired();
				newMob.setBaby(fromMob.isBaby());
        }

        newEntity.setInvulnerable(from.isInvulnerable());
        from.level.addFreshEntity(newEntity);
        from.discard();
		return newEntity;
	}
	
	/** Send heart particles (like on animal tamed) from server on a mob
	 * 
	 */
	public static void sendParticlesToMob(LivingEntity entity, ParticleOptions options, Vec3 offset, int amount, double speed)
	{
		if (entity.level.isClientSide)
			return;
		Vec3 pos = entity.position();
		for(int i = 0; i < amount; ++i) {
			double d0 = new Random().nextGaussian() * 0.1;
			double d1 = new Random().nextGaussian() * 0.2;
			double d2 = new Random().nextGaussian() * 0.1;
			double d3 = new Random().nextGaussian() * 0.5D + 1;
			((ServerLevel)(entity.level)).sendParticles(options, pos.x + offset.x + d0, pos.y + entity.getBbHeight() + offset.y + d1, pos.z + offset.z + d2, 1, 0, 0, 0, speed * d3);
		}
	}
	
	public static void sendHeartParticlesToMob(LivingEntity entity)
	{
		sendParticlesToMob(entity, ParticleTypes.HEART, new Vec3(0, -0.5, 0), 7, 5);
	}
	
} 
