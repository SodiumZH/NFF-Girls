package net.sodiumstudio.befriendmobs.entity.ai.goal.preset;

import java.util.EnumSet;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

/** Adjusted from vanilla RangedAttackGoal.
 * <p>Base class for mobs shooting any type of projectiles.
 * <p>This goal doesn't require the mob to implement {@link RangedAttackMob}.
 * For vanilla {@link RangedAttackMob} support, use {@link BefriendedRangedAttackGoal}.
 */
public abstract class BefriendedShootProjectileGoal extends BefriendedGoal {
	@Nullable
	protected LivingEntity target;
	protected int attackTime = -1;
	protected final double speedModifier;
	protected int seeTime;
	protected int attackIntervalMin;
	protected int attackIntervalMax;
	protected final float attackRadius;
	protected final float attackRadiusSqr;

	/**
	 * If non-null, the {@code attackIntervalMin} will be updated every tick.
	 * @throws IllegalStateException If the interval minimum is larger than maximum.
	 */
	@Nullable
	protected Supplier<Integer> attackIntervalMinGetter = null;
	
	/**
	 * If non-null, the {@code attackIntervalMax} will be updated every tick.
	 */
	@Nullable
	protected Supplier<Integer> attackIntervalMaxGetter = null;
	
	/**
	 * If non-null, the {@code attackIntervalMin} and {@code attackIntervalMax} will be updated samely every tick.
	 * It overrides {@code attackIntervalMinGetter} and {@code attackIntervalMaxGetter}.
	 */
	@Nullable
	protected Supplier<Integer> attackIntervalGetter = null;
	
	
	public BefriendedShootProjectileGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackInterval,
			float pAttackRadius) {
		this(mob, pSpeedModifier, pAttackInterval, pAttackInterval, pAttackRadius);
	}

	public BefriendedShootProjectileGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackIntervalMin,
			int pAttackIntervalMax, float pAttackRadius) {
		super(mob);
		this.mob = mob;
		this.speedModifier = pSpeedModifier;
		this.attackIntervalMin = pAttackIntervalMin;
		this.attackIntervalMax = pAttackIntervalMax;
		this.attackRadius = pAttackRadius;
		this.attackRadiusSqr = pAttackRadius * pAttackRadius;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		allowAllStatesExceptWait();
	}

	/**
	 * Set attack interval getter. It will update min and max interval samely every tick on running.
	 */
	@SuppressWarnings("unchecked")
	public <T extends BefriendedShootProjectileGoal> T setAttackIntervalGetter(Supplier<Integer> getter)
	{
		this.attackIntervalGetter = getter;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BefriendedShootProjectileGoal> T setAttackIntervalMinGetter(Supplier<Integer> getter)
	{
		this.attackIntervalMinGetter = getter;
		return (T) this;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends BefriendedShootProjectileGoal> T setAttackIntervalMaxGetter(Supplier<Integer> getter)
	{
		this.attackIntervalMaxGetter = getter;
		return (T) this;
	}
	
	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	@Override
	public boolean checkCanUse() {
		if (isDisabled())
			return false;
		LivingEntity livingentity = this.updateTarget();
		if (livingentity != null && livingentity.isAlive()) {
			this.target = livingentity;
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean checkCanContinueToUse() {
		return this.checkCanUse() || !this.mob.asMob().getNavigation().isDone();
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void onStop() {
		this.target = null;
		this.seeTime = 0;
		this.attackTime = -1;
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	@Override
	public void onTick() {
		double d0 = this.mob.asMob().distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
		boolean flag = this.mob.asMob().getSensing().hasLineOfSight(this.target);
		if (flag) {
			++this.seeTime;
		} else {
			this.seeTime = 0;
		}

		if (!(d0 > this.attackRadiusSqr) && this.seeTime >= 5) {
			this.mob.asMob().getNavigation().stop();
		} else {
			this.mob.asMob().getNavigation().moveTo(this.target, this.speedModifier);
		}

		// Update attack interval
		if (this.attackIntervalGetter != null)
		{
			int interval = this.attackIntervalGetter.get();
			this.attackIntervalMax = interval;
			this.attackIntervalMin = interval;
		}
		else
		{
			if (this.attackIntervalMaxGetter != null)
				this.attackIntervalMax = attackIntervalMaxGetter.get();
			if (this.attackIntervalMinGetter != null)
				this.attackIntervalMin = attackIntervalMinGetter.get();
		}
		// Update attack interval end
		
		this.mob.asMob().getLookControl().setLookAt(this.target, 30.0F, 30.0F);
		if (--this.attackTime == 0) {
			if (!flag) {
				return;
			}

			float f = (float) Math.sqrt(d0) / this.attackRadius;
			float f1 = Mth.clamp(f, 0.1F, 1.0F);
			this.performShooting(this.target, f1);	// Removed RangedAttackMob 
			this.attackTime = Mth.floor(
					f * (this.attackIntervalMax - this.attackIntervalMin) + this.attackIntervalMin);
		} else if (this.attackTime < 0) {
			this.attackTime = Mth.floor(Mth.lerp(Math.sqrt(d0) / this.attackRadius,
					this.attackIntervalMin, this.attackIntervalMax));
		}
	}
	
	/**
	 * Method to perform shooting. This includes adding projectiles, setting up its properties and any other actions on shooting.
	 * <p>Actually you can do anything in this, even other than adding projectiles.
	 */
	protected abstract void performShooting(LivingEntity target, float velocity);
	
	/**
	 * Method to update target. Invoked on each time it checks can use or can continue to use.
	 * <p>If the result is non-null, the goal will start or continue. Return null to stop the goal.
	 * @return New target to set.
	 */
	protected abstract LivingEntity updateTarget();
}
