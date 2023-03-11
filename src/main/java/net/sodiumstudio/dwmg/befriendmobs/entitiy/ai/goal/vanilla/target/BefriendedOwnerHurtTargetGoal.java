package net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target;

import java.util.EnumSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.target.BefriendedTargetGoal;

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
	 * necessary for execution in this handler as well.
	 */
	public boolean canUse() {
		if (isDisabled())
			return false;
		Player owner = mob.getOwner();
		if(owner == null)
			return false;
		this.ownerLastHurt = owner.getLastHurtMob();
		int i = owner.getLastHurtMobTimestamp();
		if (i == this.timestamp)
			return false;
		else if (!this.canAttack(this.ownerLastHurt, TargetingConditions.DEFAULT))
			return false;
		else if (!mob.wantsToAttack(this.ownerLastHurt))
			return false;
		else return true;

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
