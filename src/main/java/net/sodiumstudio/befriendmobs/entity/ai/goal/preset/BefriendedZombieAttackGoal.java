package net.sodiumstudio.befriendmobs.entity.ai.goal.preset;

import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

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
	public void onStart() {
		super.onStart();
		this.raiseArmTicks = 0;
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void onStop() {
		super.onStop();
		this.mob.asMob().setAggressive(false);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void onTick() {
		super.onTick();
		++this.raiseArmTicks;
		if (this.raiseArmTicks >= 5 && this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
			this.mob.asMob().setAggressive(true);
		} else {
			this.mob.asMob().setAggressive(false);
		}

	}
}
