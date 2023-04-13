package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset;

import java.util.EnumSet;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;

public class BefriendedMeleeAttackGoal extends BefriendedGoal
{

	protected final double speedModifier;
	protected final boolean followingTargetEvenIfNotSeen;
	protected Path path;
	protected double pathedTargetX;
	protected double pathedTargetY;
	protected double pathedTargetZ;
	protected int ticksUntilNextPathRecalculation;
	protected int ticksUntilNextAttack;
	protected final int attackInterval = 20;
	protected long lastCanUseCheck;
	protected static final long COOLDOWN_BETWEEN_CAN_USE_CHECKS = 20L;
	protected int failedPathFindingPenalty = 0;
	protected boolean canPenalize = false;
	protected boolean noResetTargetOnInterrupted = false;

	public BefriendedMeleeAttackGoal(IBefriendedMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen)
	{
		super(pMob);
		this.speedModifier = pSpeedModifier;
		this.followingTargetEvenIfNotSeen = pFollowingTargetEvenIfNotSeen;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		allowAllStatesExceptWait();
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
	@Override
	public boolean canUse() {
		if (isDisabled())
			return false;
		long i = getPathfinder().level.getGameTime();
		// Check canUse each 20s
		if (i - this.lastCanUseCheck < 20L)
		{
			return false;
		} else
		{
			this.lastCanUseCheck = i;
			// Check target valid
			LivingEntity livingentity = getPathfinder().getTarget();
			if (livingentity == null)
			{
				return false;
			} else if (!livingentity.isAlive())
			{
				return false;
			} else
			{
				// WHAT'S THIS?
				if (canPenalize)
				{
					if (--this.ticksUntilNextPathRecalculation <= 0)
					{
						this.path = getPathfinder().getNavigation().createPath(livingentity, 0);
						this.ticksUntilNextPathRecalculation = 4 + getPathfinder().getRandom().nextInt(7);
						if (this.path != null)
							return true;
						else
							return false;
					} else
						return true;
				}
				// Try create path
				this.path = getPathfinder().getNavigation().createPath(livingentity, 0);
				if (this.path != null)
					// If create path succeeded, pass
					return true;
				else
				{
					// If the attack can reach now, pass
					if (this.getAttackReachSqr(livingentity) >= getPathfinder().distanceToSqr(livingentity.getX(),
							livingentity.getY(), livingentity.getZ()))
						return true;
					else
						return false;
				}
			}
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean canContinueToUse() {
		LivingEntity livingentity = getPathfinder().getTarget();
		// Check target is valid
		if (livingentity == null)
		{
			return false;
		} else if (!livingentity.isAlive())
		{
			return false;
		} else if (!this.followingTargetEvenIfNotSeen)
		{
			// Force follow target, end only when nav ends
			return !getPathfinder().getNavigation().isDone();
		} else if (!getPathfinder().isWithinRestriction(livingentity.blockPosition()))
		{
			// Otherwise if cannot see, stop
			return false;
		} else
		{
			// don't attack creative/spectator player
			return !(livingentity instanceof Player)
					|| !livingentity.isSpectator() && !((Player) livingentity).isCreative();
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		// Try nav
		getPathfinder().getNavigation().moveTo(this.path, this.speedModifier);
		getPathfinder().setAggressive(true);
		this.ticksUntilNextPathRecalculation = 0;
		this.ticksUntilNextAttack = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void stop() {
		LivingEntity livingentity = getPathfinder().getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity))
		{
			getPathfinder().setTarget((LivingEntity) null);
		}

		getPathfinder().setAggressive(false);
		getPathfinder().getNavigation().stop();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void tick() {
		LivingEntity livingentity = getPathfinder().getTarget();
		if (livingentity != null)
		{
			getPathfinder().getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
			double d0 = getPathfinder().distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
			this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
			if ((this.followingTargetEvenIfNotSeen || getPathfinder().getSensing().hasLineOfSight(livingentity))
					&& this.ticksUntilNextPathRecalculation <= 0
					&& (this.pathedTargetX == 0.0D && this.pathedTargetY == 0.0D && this.pathedTargetZ == 0.0D
							|| livingentity.distanceToSqr(this.pathedTargetX, this.pathedTargetY,
									this.pathedTargetZ) >= 1.0D
							|| getPathfinder().getRandom().nextFloat() < 0.05F))
			{
				this.pathedTargetX = livingentity.getX();
				this.pathedTargetY = livingentity.getY();
				this.pathedTargetZ = livingentity.getZ();
				this.ticksUntilNextPathRecalculation = 4 + getPathfinder().getRandom().nextInt(7);
				if (this.canPenalize)
				{
					this.ticksUntilNextPathRecalculation += failedPathFindingPenalty;
					if (getPathfinder().getNavigation().getPath() != null)
					{
						net.minecraft.world.level.pathfinder.Node finalPathPoint = getPathfinder().getNavigation()
								.getPath().getEndNode();
						if (finalPathPoint != null
								&& livingentity.distanceToSqr(finalPathPoint.x, finalPathPoint.y, finalPathPoint.z) < 1)
							failedPathFindingPenalty = 0;
						else
							failedPathFindingPenalty += 10;
					} else
					{
						failedPathFindingPenalty += 10;
					}
				}
				if (d0 > 1024.0D)
				{
					this.ticksUntilNextPathRecalculation += 10;
				} else if (d0 > 256.0D)
				{
					this.ticksUntilNextPathRecalculation += 5;
				}

				if (!getPathfinder().getNavigation().moveTo(livingentity, this.speedModifier))
				{
					this.ticksUntilNextPathRecalculation += 15;
				}

				this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
			}

			this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
			this.checkAndPerformAttack(livingentity, d0);
		}
	}

	protected void checkAndPerformAttack(LivingEntity pEnemy, double pDistToEnemySqr) {
		double d0 = this.getAttackReachSqr(pEnemy);
		if (pDistToEnemySqr <= d0 && this.ticksUntilNextAttack <= 0)
		{
			this.resetAttackCooldown();
			getPathfinder().swing(InteractionHand.MAIN_HAND);
			getPathfinder().doHurtTarget(pEnemy);
		}

	}

	protected void resetAttackCooldown() {
		this.ticksUntilNextAttack = this.adjustedTickDelay(20);
	}

	protected boolean isTimeToAttack() {
		return this.ticksUntilNextAttack <= 0;
	}

	protected int getTicksUntilNextAttack() {
		return this.ticksUntilNextAttack;
	}

	protected int getAttackInterval() {
		return this.adjustedTickDelay(20);
	}

	protected double getAttackReachSqr(LivingEntity pAttackTarget) {
		return (double) (getPathfinder().getBbWidth() * 2.0F * getPathfinder().getBbWidth() * 2.0F
				+ pAttackTarget.getBbWidth());
	}
}
