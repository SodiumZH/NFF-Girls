package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset;

import java.util.EnumSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;

public class BefriendedRangedBowAttackGoal extends BefriendedGoal
{
	private final double speedModifier;
	private int attackIntervalMin;
	private final float attackRadiusSqr;
	private int attackTime = -1;
	private int seeTime;
	private boolean strafingClockwise;
	private boolean strafingBackwards;
	private int strafingTime = -1;

	public BefriendedRangedBowAttackGoal(IBefriendedMob pMob, double pSpeedModifier, int pAttackIntervalMin,
			float pAttackRadius)
	{
		super(pMob);
		if (!(pMob instanceof RangedAttackMob))
			throw new UnsupportedOperationException(
					"BefriendedRangedBowAttackGoal can appliy only on mobs implementing RangedAttackMob.");
		this.speedModifier = pSpeedModifier;
		this.attackIntervalMin = pAttackIntervalMin;
		this.attackRadiusSqr = pAttackRadius * pAttackRadius;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		allowAllStatesExceptWait();
	}

	public void setMinAttackInterval(int pAttackCooldown) {
		this.attackIntervalMin = pAttackCooldown;
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	public boolean canUse() {
		if (isDisabled())
			return false;
		return this.mob.asMob().getTarget() == null ? false : this.isHoldingBow();
	}

	protected boolean isHoldingBow() {
		return this.mob.asMob().isHolding(is -> is.getItem() instanceof BowItem);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		return (this.canUse() || !this.mob.asMob().getNavigation().isDone()) && this.isHoldingBow();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		super.start();
		this.mob.asMob().setAggressive(true);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void stop() {
		super.stop();
		this.mob.asMob().setAggressive(false);
		this.seeTime = 0;
		this.attackTime = -1;
		this.mob.asMob().stopUsingItem();
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		LivingEntity livingentity = this.mob.asMob().getTarget();
		if (livingentity != null)
		{
			double d0 = this.mob.asMob().distanceToSqr(livingentity.getX(), livingentity.getY(), livingentity.getZ());
			boolean flag = this.mob.asMob().getSensing().hasLineOfSight(livingentity);
			boolean flag1 = this.seeTime > 0;
			if (flag != flag1)
			{
				this.seeTime = 0;
			}

			if (flag)
			{
				++this.seeTime;
			} else
			{
				--this.seeTime;
			}

			if (!(d0 > (double) this.attackRadiusSqr) && this.seeTime >= 20)
			{
				this.mob.asMob().getNavigation().stop();
				++this.strafingTime;
			} else
			{
				this.mob.asMob().getNavigation().moveTo(livingentity, this.speedModifier);
				this.strafingTime = -1;
			}

			if (this.strafingTime >= 20)
			{
				if ((double) this.mob.asMob().getRandom().nextFloat() < 0.3D)
				{
					this.strafingClockwise = !this.strafingClockwise;
				}

				if ((double) this.mob.asMob().getRandom().nextFloat() < 0.3D)
				{
					this.strafingBackwards = !this.strafingBackwards;
				}

				this.strafingTime = 0;
			}

			if (this.strafingTime > -1)
			{
				if (d0 > (double) (this.attackRadiusSqr * 0.75F))
				{
					this.strafingBackwards = false;
				} else if (d0 < (double) (this.attackRadiusSqr * 0.25F))
				{
					this.strafingBackwards = true;
				}

				this.mob.asMob().getMoveControl().strafe(this.strafingBackwards ? -0.5F : 0.5F,
						this.strafingClockwise ? 0.5F : -0.5F);
				this.mob.asMob().lookAt(livingentity, 30.0F, 30.0F);
			} else
			{
				this.mob.asMob().getLookControl().setLookAt(livingentity, 30.0F, 30.0F);
			}

			if (this.mob.asMob().isUsingItem())
			{
				if (!flag && this.seeTime < -60)
				{
					this.mob.asMob().stopUsingItem();
				} else if (flag)
				{
					int i = this.mob.asMob().getTicksUsingItem();
					if (i >= 20)
					{
						this.mob.asMob().stopUsingItem();
						((RangedAttackMob) this.mob).performRangedAttack(livingentity, BowItem.getPowerForTime(i));
						this.attackTime = this.attackIntervalMin;
					}
				}
			} else if (--this.attackTime <= 0 && this.seeTime >= -60)
			{
				this.mob.asMob().startUsingItem(
						ProjectileUtil.getWeaponHoldingHand(this.mob.asMob(), item -> item instanceof BowItem));
			}

		}
	}

}
