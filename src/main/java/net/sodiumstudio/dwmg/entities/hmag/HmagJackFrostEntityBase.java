package net.sodiumstudio.dwmg.entities.hmag;

import javax.annotation.Nonnull;

import com.github.mechalopa.hmag.ModConfigs;
import com.github.mechalopa.hmag.registry.ModSoundEvents;
import com.github.mechalopa.hmag.util.ModTags;
import com.github.mechalopa.hmag.world.entity.projectile.HardSnowballEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FrostedIceBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.sodiumstudio.nautils.function.MutablePredicate;

/**
 * A re-implementation of HMaG Jack o'Frost, allowing to prevent damage in hot biomes.
 */
public abstract class HmagJackFrostEntityBase extends Monster implements RangedAttackMob
{
	
	public final MutablePredicate<HmagJackFrostEntityBase> immuneToHotBiomes = new MutablePredicate<>();
	
	public HmagJackFrostEntityBase(EntityType<? extends HmagJackFrostEntityBase> type, Level level)
	{
		super(type, level);
		this.xpReward = 15;
	}

	@Override
	public void aiStep()
	{
		super.aiStep();

		if (!this.level().isClientSide())
		{
			if (isMeltingBiome(this, this.level()) && immuneToHotBiomes.test(this))
			{
				this.hurt(this.damageSources().onFire(), 1.0F);
			}
			else if (ModConfigs.cachedServer.JACK_FROST_FREEZES_WATER && ForgeEventFactory.getMobGriefingEvent(this.level(), this))
			{
				if (this.onGround())
				{
					BlockState blockstate = Blocks.FROSTED_ICE.defaultBlockState();
					int i = 2;
					BlockPos blockpos = this.blockPosition();
					BlockPos.MutableBlockPos blockpos$mutable = new BlockPos.MutableBlockPos();

					for (BlockPos blockpos1 : BlockPos.betweenClosed(blockpos.offset(-i, -1, -i), blockpos.offset(i, -1, i)))
					{
						if (blockpos1.closerToCenterThan(this.position(), (double)i))
						{
							blockpos$mutable.set(blockpos1.getX(), blockpos1.getY() + 1, blockpos1.getZ());
							BlockState blockstate1 = this.level().getBlockState(blockpos$mutable);

							if (blockstate1.isAir())
							{
								if (this.level().getBlockState(blockpos1) == FrostedIceBlock.meltsInto() && blockstate.canSurvive(this.level(), blockpos1) && this.level().isUnobstructed(blockstate, blockpos1, CollisionContext.empty()) && !ForgeEventFactory.onBlockPlace(this, BlockSnapshot.create(this.level().dimension(), this.level(), blockpos1), Direction.UP))
								{
									this.level().setBlockAndUpdate(blockpos1, blockstate);
									this.level().scheduleTick(blockpos1, Blocks.FROSTED_ICE, Mth.nextInt(this.getRandom(), 60, 120));
								}
							}
						}
					}
				}
			}
		}
	}

	protected static boolean isMeltingBiome(Entity entity, Level level)
	{
		return entity.level().getBiome(entity.blockPosition()).is(ModTags.BiomeTags.JACK_FROST_MELTS);
	}

	@Override
	protected void customServerAiStep()
	{
		super.customServerAiStep();

		if (this.isAlive() && ModConfigs.cachedServer.JACK_FROST_REGEN)
		{
			if ((this.isInPowderSnow || this.wasInPowderSnow) && !this.hasEffect(MobEffects.REGENERATION))
			{
				this.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 2));
			}
		}
	}

	@Override
	public boolean hurt(DamageSource source, float amount)
	{
		if (source.is(DamageTypes.IN_FIRE))
		{
			amount = amount * 2.0F;
		}

		return super.hurt(source, amount);
	}

	@Override
	public void performRangedAttack(LivingEntity target, float distance)
	{
		int c = this.getRandom().nextInt(3) + 3;

		for (int i = 0; i < c; ++i)
		{
			throwSnowballTo(target.getBoundingBox().getCenter());
		}

		this.playThrowingSound();
	}

	public void throwSnowballTo(Vec3 targetPos)
	{
		HardSnowballEntity snowball = getNewSnowball();
		double deltaX = targetPos.x() - snowball.getX();
		double deltaY = targetPos.y() - snowball.getY();
		double deltaZ = targetPos.z() - snowball.getZ();
		double yOffset = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 0.05D;
		snowball.shoot(deltaX, deltaY + yOffset, deltaZ, getShootSpeed(), getShootInaccuracy());
		snowball.setDamage(getShootDamage());
		this.level().addFreshEntity(snowball);
	}
	
	public void throwSnowball(Vec3 direction)
	{
		Vec3 n = direction.normalize();
		//double yOffset = Math.sqrt(n.x * n.x + n.z * n.z) * 0.1D;
		HardSnowballEntity snowball = getNewSnowball();
		snowball.shoot(n.x, n.y, n.z, getShootSpeed(), getShootInaccuracy());
		snowball.setDamage(getShootDamage());
		this.level().addFreshEntity(snowball);
	}
	
	public void playThrowingSound()
	{
		this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	}
	
	protected HardSnowballEntity getNewSnowball()
	{
		return new HardSnowballEntity(this.level(), this);
	}
	
	protected float getShootSpeed()
	{
		return 1.5f;
	}
	
	protected float getShootInaccuracy()
	{
		return 10f;
	}
	
	protected float getShootDamage()
	{
		return 3f;
	}
	
	@Override
	public int getMaxSpawnClusterSize()
	{
		return 2;
	}

	@Override
	public double getMyRidingOffset()
	{
		return -0.45D;
	}

	@Override
	protected float getStandingEyeHeight(Pose pose, EntityDimensions size)
	{
		return 1.74F;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return ModSoundEvents.GIRL_MOB_AMBIENT.get();
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return ModSoundEvents.GIRL_MOB_HURT.get();
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return ModSoundEvents.GIRL_MOB_DEATH.get();
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState block)
	{
		this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
	}

	@Nonnull
	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket()
	{
		return NetworkHooks.getEntitySpawningPacket(this);
	}
}
