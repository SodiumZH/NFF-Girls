package net.sodiumstudio.dwmg.befriendmobs.util;

import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraft.network.chat.Component;
//import net.sodiumstudio.dwmg.dwmgcontent.effects.EnderProtectionTeleportEvent;

// Static function library for befriending-related actions.
public class EntityHelper
{

	/**
	 * Replace a living mob with another one. Only works in server. Calling in
	 * client always returns null.
	 * 
	 * @param newType EntityType of the new mob.
	 * @param from    The mob to be replaced.
	 * @return New mob
	 */
	@Deprecated
	public static Entity replaceLivingEntity(EntityType<?> newType, LivingEntity from, boolean allowNewEntityDespawn) {
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
			if (from instanceof Mob fromMob && newEntity instanceof Mob newMob)
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
	public static Entity replaceLivingEntity(EntityType<?> newType, LivingEntity from) {
		return replaceLivingEntity(newType, from, false);
	}

	public static <T extends Mob> Mob replaceMob(EntityType<T> newType, Mob from, boolean allowNewMobDespawn) {
		Mob newMob = from.convertTo(newType, true);

		if (!allowNewMobDespawn || from.isPersistenceRequired())
			newMob.setPersistenceRequired();
		newMob.setBaby(from.isBaby());
		newMob.setCustomName(from.getCustomName());
		newMob.setCustomNameVisible(from.isCustomNameVisible());
		return newMob;
	}

	public static <T extends Mob> Mob replaceMob(EntityType<T> newType, Mob from) {
		return replaceMob(newType, from, false);
	}

	@Deprecated // Use sendParticlesToEntity() instead
	public static void sendParticlesToMob(LivingEntity entity, ParticleOptions options, Vec3 offset, int amount,
			double speed, double positionRndScale, double speedRndScale) {
		if (entity.level.isClientSide)
			return;
		Vec3 pos = entity.position();
		for (int i = 0; i < amount; ++i)
		{
			double d0 = new Random().nextGaussian() * 0.1 * positionRndScale;
			double d1 = new Random().nextGaussian() * 0.2 * positionRndScale;
			double d2 = new Random().nextGaussian() * 0.1 * positionRndScale;
			double d3 = new Random().nextGaussian() * 0.5 * speedRndScale + 1;
			((ServerLevel) (entity.level)).sendParticles(options, pos.x + offset.x + d0,
					pos.y + entity.getBbHeight() + offset.y + d1, pos.z + offset.z + d2, 1, 0, 0, 0, speed * d3);
		}
	}

	@Deprecated // Use sendParticlesToEntity() instead
	public static void sendParticlesToMob(LivingEntity entity, ParticleOptions options, Vec3 offset, int amount,
			double speed) {
		sendParticlesToMob(entity, options, offset, amount, speed, 1, 1);
	}

	@Deprecated
	public static void sendHeartParticlesToMob(LivingEntity entity) {
		sendParticlesToMob(entity, ParticleTypes.HEART, new Vec3(0, -0.5, 0), 5, 5, 4, 1);
	}

	@Deprecated
	public static void sendStarParticlesToMob(LivingEntity entity) {
		sendParticlesToMob(entity, ParticleTypes.HAPPY_VILLAGER, new Vec3(0, -0.5, 0), 10, 0, 5, 0);
	}

	@Deprecated
	public static void sendSmokeParticlesToMob(LivingEntity entity) {
		sendParticlesToMob(entity, ParticleTypes.LARGE_SMOKE, new Vec3(0, -0.5, 0), 5, 5, 10, -10);
	}

	@Deprecated
	public static void sendAngryParticlesToMob(LivingEntity entity) {
		sendParticlesToMob(entity, ParticleTypes.ANGRY_VILLAGER, new Vec3(0, -0.5, 0), 5, 5, 3, 1);
	}

	public static void sendParticlesToEntity(Entity entity, ParticleOptions options, Vec3 positionOffset, Vec3 rndScale,
			int amount, double speed) {
		if (entity.level.isClientSide)
			return;
		Vec3 pos = entity.position();
		((ServerLevel) (entity.level)).sendParticles(options, pos.x + positionOffset.x, pos.y + positionOffset.y,
				pos.z + positionOffset.z, amount, rndScale.x, rndScale.y, rndScale.z, speed);
	}

	public static void sendParticlesToEntity(Entity entity, ParticleOptions options, double posOffsetX,
			double posOffsetY, double posOffsetZ, double rndScaleX, double rndScaleY, double rndScaleZ, int amount,
			double speed) {
		sendParticlesToEntity(entity, options, new Vec3(posOffsetX, posOffsetY, posOffsetZ),
				new Vec3(rndScaleX, rndScaleY, rndScaleZ), amount, speed);
	}

	public static void sendParticlesToEntity(Entity entity, ParticleOptions options, Vec3 posOffset, double rndScale,
			int amount, double speed) {
		sendParticlesToEntity(entity, options, posOffset, new Vec3(rndScale, rndScale, rndScale), amount, speed);
	}

	public static void sendParticlesToEntity(Entity entity, ParticleOptions options, double heightOffset,
			double rndScale, int amount, double speed) {
		sendParticlesToEntity(entity, options, new Vec3(0d, heightOffset, 0d), rndScale, amount, speed);
	}

	public static void sendHeartParticlesToLivingDefault(LivingEntity entity, float heightOffset) {
		sendParticlesToEntity(entity, ParticleTypes.HEART, entity.getBbHeight() - 0.2d + heightOffset, 0.5d, 10, 1d);
	}

	public static void sendHeartParticlesToLivingDefault(LivingEntity entity) {
		sendHeartParticlesToLivingDefault(entity, 0f);
	}
	
	public static void sendGlintParticlesToLivingDefault(LivingEntity entity) {
		sendParticlesToEntity(entity, ParticleTypes.HAPPY_VILLAGER, entity.getBbHeight() - 0.2, 0.5d, 20, 1d);
	}

	public static void sendSmokeParticlesToLivingDefault(LivingEntity entity, float heightOffset) {
		sendParticlesToEntity(entity, ParticleTypes.SMOKE, entity.getBbHeight() - 0.2 + heightOffset, 0.2d, 30, 0d);
	}

	public static void sendSmokeParticlesToLivingDefault(LivingEntity entity) {
		sendSmokeParticlesToLivingDefault(entity, 0f);
	}
	
	public static void sendAngryParticlesToLivingDefault(LivingEntity entity) {
		sendParticlesToEntity(entity, ParticleTypes.ANGRY_VILLAGER, entity.getBbHeight() - 0.2, 0.3d, 5, 1d);
	}

	// Get current swell value (private for Creeper class) as int. Max swell is 30.
	public static int getCreeperSwell(Creeper creeper) {
		return Math.round(creeper.getSwelling(1.0f) * 28.0f);
	}

	// Add effect, preventing to override the existing one if has longer time than this
	public static void addEffectSafe(LivingEntity entity, MobEffect effect, int ticks, int lvl) {
		if (!entity.hasEffect(effect) || entity.getEffect(effect).getDuration() < ticks)
		{
			entity.addEffect(new MobEffectInstance(effect, ticks, lvl));
		}
	}

	public static void addEffectSafe(LivingEntity entity, MobEffect effect, int ticks) {
		addEffectSafe(entity, effect, ticks, 0);
	}

	// Teleport a living entity as if it ate a chorus fruit
	public static boolean chorusLikeTeleport(LivingEntity living) {
		if (!living.level.isClientSide)
		{

			double atX = living.getX();
			double atY = living.getY();
			double atZ = living.getZ();

			for (int i = 0; i < 16; ++i)
			{
				double tryX = living.getX() + (living.getRandom().nextDouble() - 0.5D) * 16.0D;
				double tryY = Mth.clamp(living.getY() + (double) (living.getRandom().nextInt(16) - 8),
						(double) living.level.getMinBuildHeight(), (double) (living.level.getMinBuildHeight()
								+ ((ServerLevel) (living.level)).getLogicalHeight() - 1));
				double tryZ = living.getZ() + (living.getRandom().nextDouble() - 0.5D) * 16.0D;
				if (living.isPassenger())
				{
					living.stopRiding();
				}

				tryTeleportOntoGroundEvent event = new tryTeleportOntoGroundEvent(living, tryX, tryY, tryZ);
				MinecraftForge.EVENT_BUS.post(event);
				if (event.isCanceled())
					return false;
				if (living.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true))
				{
					SoundEvent soundevent = living instanceof Fox ? SoundEvents.FOX_TELEPORT
							: SoundEvents.CHORUS_FRUIT_TELEPORT;
					living.level.playSound((Player) null, atX, atY, atZ, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
					living.playSound(soundevent, 1.0F, 1.0F);
					return true;
				}
			}
			return false;
		}
		else return false;
	}
	
	// Random teleport an entity with given radius
	public static boolean tryTeleportOntoGround(Entity entity, Vec3 radius, int tryTimes) {
		if (!entity.level.isClientSide)
		{
			for (int i = 0; i < tryTimes; ++i)
			{
				Random rnd = new Random();
				double tryX = entity.getX() + (rnd.nextDouble() - 0.5D) * radius.x * 2d;
				double tryY = Mth.clamp(entity.getY() + (rnd.nextDouble() - 0.5D) * radius.y * 2d,
						(double) entity.level.getMinBuildHeight(), (double) (entity.level.getMinBuildHeight()
								+ ((ServerLevel) (entity.level)).getLogicalHeight() - 1));
				double tryZ = entity.getZ() + (rnd.nextDouble() - 0.5D) * radius.z * 2d;
				if (entity.isPassenger())
				{
					entity.stopRiding();
				}

				tryTeleportOntoGroundEvent event = new tryTeleportOntoGroundEvent(entity, tryX, tryY, tryZ);
				MinecraftForge.EVENT_BUS.post(event);
				if (event.isCanceled())
					return false;
				boolean succeeded = false;
				if (entity instanceof LivingEntity living)
				{
					succeeded = living.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
				}
				else
				{
					succeeded = randomTeleportNonLivingEntity(entity, event.getTargetX(), event.getTargetY(), event.getTargetZ());
				}
				if (succeeded)
				{
					return true;
				}
			}
			return false;
		}
		else return false;
	}
	
	// Fired on an entity teleported by EntityHelper::chorusLikeTeleport or EntityHelper::tryTeleportOntoGround function.
	public static class tryTeleportOntoGroundEvent extends EntityTeleportEvent
	{

		public tryTeleportOntoGroundEvent(Entity entity, double targetX, double targetY, double targetZ)
		{
			super(entity, targetX, targetY, targetZ);
		}
		
	}
	
	// Static method to access EnderMan::isLookingAtMe
	// Ported from vanilla EnderMan
	public static boolean isEnderManLookedAt(EnderMan enderman, Player player) {
		ItemStack itemstack = player.getInventory().armor.get(3);
		if (net.minecraftforge.common.ForgeHooks.shouldSuppressEnderManAnger(enderman, player, itemstack))
		{
			return false;
		} else
		{
			Vec3 vec3 = player.getViewVector(1.0F).normalize();
			Vec3 vec31 = new Vec3(enderman.getX() - player.getX(), enderman.getEyeY() - player.getEyeY(),
					enderman.getZ() - player.getZ());
			double d0 = vec31.length();
			vec31 = vec31.normalize();
			double d1 = vec3.dot(vec31);
			return d1 > 1.0D - 0.025D / d0 ? player.hasLineOfSight(enderman) : false;
		}
	}
	
	// Check if an enderman will freeze (be angry) on looked at
	// Ported from vanilla EnderMan.EndermanFreezeWhenLookedAt::canUse
	public static boolean shouldEnderManFreezeOnLookedAt(EnderMan enderman, Player player)
	{
        LivingEntity target = enderman.getTarget();
        if (!(target instanceof Player)) {
           return false;
        } else {
           double d0 = target.distanceToSqr(enderman);
           return d0 > 256.0D ? false : isEnderManLookedAt(enderman, ((Player)target));
        }
	}
	
	public static void resetAttributeModifier(LivingEntity target, Attribute inAttibute, 
	AttributeModifier inModifier, boolean value)
	{
		AttributeInstance instance = target.getAttribute(inAttibute);
		instance.removeModifier(inModifier);
		if (value)
		{
			instance.addTransientModifier(inModifier);
		}
		return;
	}

	@SuppressWarnings("deprecation")
	public static boolean randomTeleportNonLivingEntity(Entity entity, double inX, double inY, double inZ) {
		if (entity instanceof LivingEntity)
		{
			throw new UnsupportedOperationException(
					"randomTeleportNonLivingEntity() supports only Non-living entities. For LivingEntity use LivingEntity#randomTeleport instead.");
		}
		double atX = entity.getX();
		double atY = entity.getY();
		double atZ = entity.getZ();
		double actualY = inY;
		boolean noCollision = false;
		BlockPos currentPos = new BlockPos(inX, inY, inZ);
		Level level = entity.level;
		if (level.hasChunkAt(currentPos))
		{
			boolean solidBlockFound = false;
			while (!solidBlockFound && currentPos.getY() > level.getMinBuildHeight())
			{
				BlockPos nextPos = currentPos.below();
				BlockState blockstate = level.getBlockState(nextPos);
				if (blockstate.getMaterial().blocksMotion())
				{
					solidBlockFound = true;
				} else
				{
					--actualY;
					currentPos = nextPos;
				}
			}

			if (solidBlockFound)
			{
				entity.teleportTo(inX, actualY, inZ);
				if (level.noCollision(entity) && !level.containsAnyLiquid(entity.getBoundingBox()))
				{
					noCollision = true;
				}
			}
		}

		if (!noCollision)
		{
			entity.teleportTo(atX, atY, atZ);
			return false;
		} 
		else
		{
			return true;
		}
	}
	
	public static void removeAllEquipment(Mob target)
	{
		target.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
		target.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
		target.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
		target.setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
		target.setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
		target.setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
	}
	
	// Spawn mob at a given position without any post-natural-spawn initialization, finalizeSpawn() or sth else
	public static Mob spawnDefaultMob(EntityType<? extends Mob> type, ServerLevel level, @Nullable CompoundTag compound,
			@Nullable Component customName, @Nullable Player player, BlockPos pos, 
			boolean shouldOffsetY, boolean shouldOffsetYMore) 
	{
		Mob mob = type.create(level);
		if (mob == null)
		{
			return null;
		} 
		else
		{
			double d0;
			if (shouldOffsetY)
			{
				mob.setPos((double) pos.getX() + 0.5D, (double) (pos.getY() + 1), (double) pos.getZ() + 0.5D);
				/* EntityType.getYOffset */
				AABB aabb = new AABB(pos);
				if (shouldOffsetYMore)
				{
					aabb = aabb.expandTowards(0.0D, -1.0D, 0.0D);
				}

				Iterable<VoxelShape> iterable = level.getCollisions((Entity) null, aabb);
				d0 = 1.0D + Shapes.collide(Direction.Axis.Y, mob.getBoundingBox(), iterable,
						shouldOffsetYMore ? -2.0D : -1.0D);
			} else
			{
				d0 = 0.0D;
			}

			mob.moveTo((double) pos.getX() + 0.5D, (double) pos.getY() + d0, (double) pos.getZ() + 0.5D,
					Mth.wrapDegrees(level.random.nextFloat() * 360.0F), 0.0F);

			mob.yHeadRot = mob.getYRot();
			mob.yBodyRot = mob.getYRot();
			/* No further initialization */

			if (customName != null)
			{
				mob.setCustomName(customName);
			}

			EntityType.updateCustomEntityTag(level, player, mob, compound);
			level.addFreshEntityWithPassengers(mob);
			return mob;
		}
	}

}
