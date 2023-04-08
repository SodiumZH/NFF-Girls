package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset;

import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;

// Adjusted from vanilla ZombieAttackGoal
public class BefriendedZombieAttackGoal extends BefriendedMeleeAttackGoal {
	
	private int raiseArmTicks;

	public BefriendedZombieAttackGoal(IBefriendedMob mob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
	      super(mob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
	   }

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		super.start();
		this.raiseArmTicks = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void stop() {
		super.stop();
		this.mob.asMob().setAggressive(false);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void tick() {
		super.tick();
		++this.raiseArmTicks;
		if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
			this.mob.asMob().setAggressive(true);
		} else {
			this.mob.asMob().setAggressive(false);
		}

	}
}
