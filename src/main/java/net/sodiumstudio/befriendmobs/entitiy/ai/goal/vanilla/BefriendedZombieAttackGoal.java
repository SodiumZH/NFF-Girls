package net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla;

import net.sodiumstudio.befriendmobs.entitiy.IBefriendedMob;

// Adjusted from vanilla ZombieAttackGoal
public class BefriendedZombieAttackGoal extends BefriendedMeleeAttackGoal {
	
	private int raiseArmTicks;

	public BefriendedZombieAttackGoal(IBefriendedMob mob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
	      super(mob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
	   }

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		super.start();
		this.raiseArmTicks = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void stop() {
		super.stop();
		this.getMob().setAggressive(false);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		super.tick();
		++this.raiseArmTicks;
		if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
			this.getMob().setAggressive(true);
		} else {
			this.getMob().setAggressive(false);
		}

	}
}
