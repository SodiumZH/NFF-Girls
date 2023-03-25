package net.sodiumstudio.dwmg.befriendmobs.entitiy.vanillapreset.creeper;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.BefriendedGoal;

public class BefriendedCreeperSwellGoal extends BefriendedGoal
{
	// Merged from vanilla creeper swell goal
	protected final AbstractBefriendedCreeper creeper;
	@Nullable
	protected LivingEntity target;
	protected double stopDistance = 7.0d;	// Stop swelling when target is further than this distance
	protected double startDistance = 3.0d;	// Start swelling when target is closer than this distance
	protected boolean targetedSwelling = true;	// 

	public BefriendedCreeperSwellGoal(AbstractBefriendedCreeper creeper)
	{
		this.creeper = creeper;
		this.mob = (IBefriendedMob) creeper;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE));
		this.allowAllStates();
	}

	public BefriendedCreeperSwellGoal targetless()
	{
		targetedSwelling = false;
		return this;
	}
	
	public BefriendedCreeperSwellGoal startDistance(double distance)
	{
		this.startDistance = distance;
		return this;
	}
	
	public BefriendedCreeperSwellGoal stopDistance(double distance)
	{
		this.stopDistance = distance;
		return this;
	}
	
	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	public boolean canUse() {
		if (isDisabled())
			return false;
		return this.creeper.getSwellDir() > 0
				|| this.targetedSwelling && this.creeper.getTarget() != null && this.creeper.distanceToSqr(this.creeper.getTarget()) < startDistance * startDistance;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		//this.creeper.getNavigation().stop();
		this.target = this.creeper.getTarget();
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void stop() {
	//	this.target = null;
	}

	public boolean requiresUpdateEveryTick() {
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		if (targetedSwelling)
		{
			if (this.target == null)
				this.creeper.setSwellDir(-1);
			else if (this.creeper.distanceToSqr(this.target) > stopDistance * stopDistance)	
				this.creeper.setSwellDir(-1);
			else if (!this.creeper.getSensing().hasLineOfSight(this.target))
				this.creeper.setSwellDir(-1);
		}
		else
			this.creeper.setSwellDir(1);
	}
}
