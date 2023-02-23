package com.sodium.dwmg.entities.befriending;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.registries.ModCapabilities;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

// Static function library for befriending-related actions.
public class BefriendingUtil {

	/** Replace a living entity with another one.
	 * 
	 * @param newType EntityType of the new entity.
	 * @param from The entity to be replaced.
	 * @return New entity
	 */
	public static Entity replaceLivingEntity(EntityType<?> newType, LivingEntity from)
	{
		if(!from.getLevel().isClientSide())
		{
			return null;
		}
		Entity newEntity = newType.create(from.getLevel());
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
        }

        newEntity.setInvulnerable(from.isInvulnerable());
        from.level.addFreshEntity(newEntity);
        from.discard();
		return newEntity;
	}
} 
