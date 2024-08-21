package net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target;

import java.util.EnumSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class BefriendedOwnerHurtByTargetGoal extends BefriendedTargetGoal {

	private LivingEntity ownerLastHurtBy;
	private int timestamp;

	public BefriendedOwnerHurtByTargetGoal(IBefriendedMob inMob) {
		super(inMob, false);
		this.setFlags(EnumSet.of(Goal.Flag.TARGET));
		allowAllStatesExceptWait();
		this.setNoExpireCondition(() -> (this.mob.isOwnerPresent() && this.mob.asMob().getLastHurtByMob() == this.ownerLastHurtBy));
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
		LivingEntity owner = mob.getOwner();
		if (owner == null)
			return false;
		else 
		{
			this.ownerLastHurtBy = owner.getLastHurtByMob();
			if (BefriendedHelper.isLivingAlliedToBM(mob, this.ownerLastHurtBy) || !mob.wantsToAttack(this.ownerLastHurtBy))
				return false;
			int i = owner.getLastHurtByMobTimestamp();
			if (i == this.timestamp)
				return false;
			else if (!this.canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT))
				return false;
			else if (!mob.wantsToAttack(this.ownerLastHurtBy))
				return false;
			else return true;
		}
	}

	@Override
	public boolean checkCanContinueToUse()
	{
		return super.checkCanContinueToUse();
	}
	
	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void onStart() {
		mob.asMob().setTarget(this.ownerLastHurtBy);
		LivingEntity livingentity = mob.getOwner();
		if (livingentity != null) {
			this.timestamp = livingentity.getLastHurtByMobTimestamp();
		}
		super.onStart();
	}
	
	@Override
	public void onStop()
	{
		this.mob.asMob().setTarget(null);
		this.ownerLastHurtBy = null;
	}
	
}
