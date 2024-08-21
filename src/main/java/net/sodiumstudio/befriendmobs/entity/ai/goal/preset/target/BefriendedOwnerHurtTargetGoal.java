package net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target;

import java.util.EnumSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class BefriendedOwnerHurtTargetGoal extends BefriendedTargetGoal {
	
	private LivingEntity ownerLastHurt;
	private int timestamp;

	public BefriendedOwnerHurtTargetGoal(IBefriendedMob pMob) {
	      super(pMob, false);
	      mob = pMob;
	      this.setFlags(EnumSet.of(Goal.Flag.TARGET));
	      allowAllStatesExceptWait();
	      this.setNoExpireCondition(() -> (this.mob.isOwnerPresent() && this.mob.asMob().getLastHurtByMob() == this.ownerLastHurt));
	      this.setExpireTicks(30 * 20);	// Expires after 30s by default
	   }

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
	@Override
	public boolean checkCanUse() {
		if (isDisabled())
			return false;
		Player owner = mob.getOwner();
		if(owner == null)
			return false;
		this.ownerLastHurt = owner.getLastHurtMob();
		if (owner == this.ownerLastHurt)
			return false;
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
	@Override
	public void onStart() {
		mob.asMob().setTarget(this.ownerLastHurt);
		LivingEntity livingentity = mob.getOwner();
		if (livingentity != null) {
			this.timestamp = livingentity.getLastHurtMobTimestamp();
		}
		super.onStart();
	}
	
	@Override
	public void onStop()
	{
		this.ownerLastHurt = null;
		this.mob.asMob().setTarget(null);
	}
}
