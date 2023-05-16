package net.sodiumstudio.dwmg.entities.ai.goals;

import java.util.EnumSet;

import com.github.mechalopa.hmag.world.entity.AbstractFlyingMonsterEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedMoveGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedTargetGoal;
import net.sodiumstudio.befriendmobs.util.LevelHelper;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;

/* Ported from HMaG-AbstractFlyingMonsterEntity (Mechalopa)
 */
public interface HmagFlyingGoal
{
	
	public default AbstractFlyingMonsterEntity getFlying()
	{
		if (this instanceof BefriendedGoal g)
			return (AbstractFlyingMonsterEntity)(g.getMob());
		else if (this instanceof BefriendedTargetGoal g)
			return (AbstractFlyingMonsterEntity)(g.getMob());
		throw new UnsupportedOperationException("HmagFlyingGoal is only for BefriendedGoal and BefriendedTargetGoal.");
	}
	
	public static class ChargeAttackGoal extends BefriendedGoal implements HmagFlyingGoal
	{
		protected final double moveSpeed;
		protected final float attackRadius;
		protected final int chance;
		protected int attackTime;

		public ChargeAttackGoal(IBefriendedMob mob)
		{
			this(mob, 0.3D, 2.0F);
		}

		public ChargeAttackGoal(IBefriendedMob mob, double moveSpeed, float maxAttackDistance)
		{
			this(mob, moveSpeed, maxAttackDistance, 4);
		}

		public ChargeAttackGoal(IBefriendedMob mob, double moveSpeed, float maxAttackDistance, int chance)
		{
			super(mob);
			this.moveSpeed = moveSpeed;
			this.attackRadius = maxAttackDistance;
			this.chance = chance;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
			this.allowAllStatesExceptWait();
		}

		@Override
		public boolean canUse()
		{
			if (isDisabled())
				return false;
			if (getFlying().getTarget() != null 
					&& !getFlying().getMoveControl().hasWanted() 
					&& getFlying().getRandom().nextInt(this.chance) == 0)
			{
				return getFlying().distanceToSqr(getFlying().getTarget()) > this.attackRadius;
			}
			else
			{
				return false;
			}
		}

		@Override
		public boolean canContinueToUse()
		{
			if (isDisabled())
				return false;
			return getFlying().getMoveControl().hasWanted() 
					&& getFlying().isCharging() 
					&& getFlying().getTarget() != null 
					&& getFlying().getTarget().isAlive();
		}

		@Override
		public void start()
		{
			LivingEntity livingentity = getFlying().getTarget();

			if (getFlying().hasLineOfSight(livingentity) || getFlying().getAttackPhase() != 0)
			{
				Vec3 vec3 = livingentity.getEyePosition();
				getFlying().getMoveControl().setWantedPosition(vec3.x, vec3.y - 0.75D, vec3.z, this.moveSpeed);
				getFlying().setAttackPhase(2);
			}
		}

		@Override
		public void stop()
		{
			getFlying().setAttackPhase((getFlying().getTarget() != null && getFlying().getTarget().isAlive()) ? 1 : 0);
		}

		@Override
		public void tick()
		{
			AbstractFlyingMonsterEntity attacker = getFlying();
			LivingEntity livingentity = attacker.getTarget();

			this.attackTime = Math.max(this.attackTime - 1, 0);
			attacker.getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
			double d0 = attacker.distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
			double d1 = this.getAttackReachSqr(livingentity);

			if (d0 <= d1 && this.attackTime <= 0)
			{
				this.attackTime = 20;
				attacker.swing(InteractionHand.MAIN_HAND);
				attacker.doHurtTarget(livingentity);
				attacker.setAttackPhase(1);
			}
			else
			{
				if (attacker.hasLineOfSight(livingentity))
				{
					if (d0 < this.attackRadius + 15.0F)
					{
						Vec3 vec3 = livingentity.getEyePosition();
						attacker.getMoveControl().setWantedPosition(vec3.x, vec3.y - 0.75D, vec3.z, this.moveSpeed);
					}
				}
				else if (attacker.getRandom().nextInt(16) == 0)
				{
					attacker.setAttackPhase(0);
				}
			}
		}

		protected double getAttackReachSqr(LivingEntity attackTarget)
		{
			return getFlying().getBbWidth() * 2.0F * getFlying().getBbWidth() * 2.0F + attackTarget.getBbWidth();
		}
	}

	public static class MoveRandomGoal extends BefriendedMoveGoal implements HmagFlyingGoal
	{
		protected final double moveSpeed;
		protected final int chance;
		protected final int width;
		protected final int height;
		protected int heightLimit = -1;
		
		public MoveRandomGoal(IBefriendedMob mob)
		{
			this(mob, 0.25D);
		}

		public MoveRandomGoal(IBefriendedMob mob, double moveSpeed)
		{
			this(mob, moveSpeed, 6, 3, 2);
		}

		public MoveRandomGoal(IBefriendedMob mob, double moveSpeed, int chance, int width, int height)
		{
			super(mob);
			this.moveSpeed = moveSpeed;
			this.chance = chance;
			this.width = width;
			this.height = height;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
			this.allowAllStatesExceptWait();
		}

		public MoveRandomGoal heightLimit(int value)
		{
			heightLimit = value;
			return this;
		}
		
		@Override
		public boolean canUse()
		{
			if (isDisabled())
				return false;		
			return !getFlying().getMoveControl().hasWanted() && getFlying().getRandom().nextInt(this.chance) == 0;
		}

		@Override
		public boolean canContinueToUse()
		{
			return false;
		}

		@Override
		public void start()
		{
			if (!(getFlying().getTarget() != null && getFlying().getTarget().isAlive()))
			{
				getFlying().setAttackPhase(0);
			}
		}

		
		protected BlockPos getWantedPosition()
		{
			BlockPos blockpos = getFlying().blockPosition();
			BlockPos blockpos1 = blockpos.offset(
					getFlying().getRandom().nextInt(this.width * 2 + 1) - this.width,
					getFlying().getRandom().nextInt(this.height * 2 + 1) - this.height,
					getFlying().getRandom().nextInt(this.width * 2 + 1) - this.width);
			if (heightLimit <= 0)
				return blockpos1;
			// No height limit if it's above the void
			else if (LevelHelper.getHeightToGround(blockpos1, getFlying()) == -1)
				return blockpos1;
			else if (LevelHelper.getHeightToGround(blockpos1, getFlying()) > heightLimit)
			{
				// If it's already too high, fly to the height limit first
				int it = 32;
				while (LevelHelper.getHeightToGround(blockpos1, getFlying()) > heightLimit)
				{
					blockpos1 = blockpos1.below();
					it--;
					if (it <= 0)
						break;
				}
				// Case when it didn't find a position, try flying down
				if (it <= 0)
				{
					blockpos1 = new BlockPos(blockpos);
					while (LevelHelper.getHeightToGround(blockpos1, getFlying()) > heightLimit)
						blockpos1 = blockpos1.below();
				}
				return blockpos1;
			}
			else
			{
				int it = 32;	// To avoid potential infinite loop 				
				while (LevelHelper.getHeightToGround(blockpos1, getFlying()) > heightLimit)
				{
					// Search until an acceptable
					blockpos1 = blockpos.offset(
							getFlying().getRandom().nextInt(this.width * 2 + 1) - this.width,
							getFlying().getRandom().nextInt(this.height * 2 + 1) - this.height,
							getFlying().getRandom().nextInt(this.width * 2 + 1) - this.width);
					it--;
					if (it <= 0)
						break;
				}
				// If failed, find below to get an acceptable position
				while (LevelHelper.getHeightToGround(blockpos1, getFlying()) > heightLimit)
					blockpos1 = blockpos1.below();
				return blockpos1;
			}

		}
		
		@Override
		public void tick()
		{
			AbstractFlyingMonsterEntity flyingentity = getFlying();
			BlockPos blockpos = flyingentity.getBoundOrigin();

			if (blockpos == null)
			{
				blockpos = flyingentity.blockPosition();
			}

			for (int i = 0; i < 3; ++i)
			{
				BlockPos blockpos1 = getWantedPosition();
				
				if (flyingentity.level.isEmptyBlock(blockpos1))
				{
					flyingentity.getMoveControl().setWantedPosition(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D, this.moveSpeed);

					if (flyingentity.getTarget() == null)
					{
						flyingentity.getLookControl().setLookAt(blockpos1.getX() + 0.5D, blockpos1.getY() + 0.5D, blockpos1.getZ() + 0.5D, 180.0F, 20.0F);
					}

					break;
				}
			}
		}	
	}
	
	public static class FollowOwnerGoal extends MoveRandomGoal implements HmagFlyingGoal
	{
		public FollowOwnerGoal(IBefriendedMob mob, double moveSpeed, int width, int height)
		{
			super(mob, moveSpeed, 1, width, height);
			this.disallowAllStates();
			this.allowState(BefriendedAIState.FOLLOW);
		}

		public FollowOwnerGoal(IBefriendedMob mob, double moveSpeed)
		{
			this(mob, moveSpeed, 3, 2);
		}

		public FollowOwnerGoal(IBefriendedMob mob)
		{
			this(mob, 0.25D);
		}
		
		@Override
		public boolean canUse()
		{
			return super.canUse() && !CFavorabilityHandler.isLowFavorability(mob.asMob());
		}
		
		@Override
		public void tick() {
			AbstractFlyingMonsterEntity flyingentity = getFlying();
			BlockPos blockpos = flyingentity.getBoundOrigin();
			if (blockpos == null)
			{
				blockpos = flyingentity.blockPosition();
			}
			BlockPos playerPos = new BlockPos(this.mob.getOwner().getEyePosition());
			if (flyingentity.level.isEmptyBlock(playerPos))
			{
				flyingentity.getMoveControl().setWantedPosition(playerPos.getX() + 0.5D, playerPos.getY() + 0.5D,
						playerPos.getZ() + 0.5D, this.moveSpeed);
			}
		}
					
	}
	
}
