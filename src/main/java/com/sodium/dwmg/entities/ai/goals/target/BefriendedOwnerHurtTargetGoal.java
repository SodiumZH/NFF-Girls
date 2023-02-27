package com.sodium.dwmg.entities.ai.goals.target;

import java.util.EnumSet;

import com.sodium.dwmg.entities.IBefriendedMob;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;

public class BefriendedOwnerHurtTargetGoal extends BefriendedTargetGoal {
	
	private LivingEntity ownerLastHurt;
	private int timestamp;

	public BefriendedOwnerHurtTargetGoal(IBefriendedMob pMob) {
	      super(pMob, false);
	      mob = pMob;
	      this.setFlags(EnumSet.of(Goal.Flag.TARGET));
			allowedStates.add(WANDER);
			allowedStates.add(FOLLOW);
	   }

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	public boolean canUse() {
		if (!isDisabled()) {
			LivingEntity livingentity = mob.getOwner();
			if (livingentity == null) {
				return false;
			} else {
				this.ownerLastHurt = livingentity.getLastHurtMob();
				int i = livingentity.getLastHurtMobTimestamp();
				return i != this.timestamp && this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT)
						&& mob.wantsToAttack(this.ownerLastHurt, livingentity);
			}
		} else {
			return false;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		getPathfinder().setTarget(this.ownerLastHurt);
		LivingEntity livingentity = mob.getOwner();
		if (livingentity != null) {
			this.timestamp = livingentity.getLastHurtMobTimestamp();
		}
		super.start();
	}
}
