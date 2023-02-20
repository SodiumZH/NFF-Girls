package com.sodium.dwmg.entities.befriending;

import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;
import com.sodium.dwmg.entities.ZombieGirlFriendly;
import com.sodium.dwmg.registries.ModEntityTypes;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;

public class BefriendingHelper {

	/** Replace a living entity with another one.
	 * 
	 * @param newType EntityType of the new entity.
	 * @param from The entity to be replaced.
	 * @return New entity
	 */
	public static LivingEntity replaceLivingEntity(EntityType<? extends LivingEntity> newType, LivingEntity from)
	{
		if(!from.getLevel().isClientSide())
		{
			return null;
		}
		LivingEntity newEntity = newType.create(from.getLevel());
		newEntity.moveTo(from.getX(), from.getY(), from.getZ(), from.getYRot(), from.getXRot());
		newEntity.yBodyRot = from.yBodyRot;
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
	
	
	public static ZombieGirlFriendly befriendZombieGirl(Player player, ZombieGirlEntity target)
	{
		if(target.getLevel().isClientSide())
		{
			return null;
		}
		if(target instanceof ZombieGirlFriendly z)
		{
			return z;
		}
		ZombieGirlFriendly newMob = (ZombieGirlFriendly)replaceLivingEntity(ModEntityTypes.ZOMBIE_GIRL_FRIENDLY.get(), target);
		newMob.init(player, target);
		newMob.setHealth(target.getHealth());
		return newMob;
	}
	
}
