package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;

public class BefriendedLeapAtOwnerGoal extends BefriendedLeapAtGoal implements IBefriendedFollowOwner
{

	public BefriendedLeapAtOwnerGoal(IBefriendedMob mob, float yd, float xzd, float maxAttackDistance, int chance)
	{
		super(mob, yd, xzd, maxAttackDistance, chance);
		this.allowState(BefriendedAIState.FOLLOW);
	}

	public BefriendedLeapAtOwnerGoal(IBefriendedMob mob, float yd, float xzd)
	{
		super(mob, yd, xzd);
		this.allowState(BefriendedAIState.FOLLOW);
	}

	public BefriendedLeapAtOwnerGoal(IBefriendedMob mob, float yd)
	{
		super(mob, yd);
		this.allowState(BefriendedAIState.FOLLOW);
	}

	@Override
	protected boolean canLeap()
	{
		if (targetPos.isEmpty())
			return false;
		return this.mob.asMob().distanceToSqr(this.targetPos.get()) > 4d;
	}
	
	@Override
	public boolean checkCanUse()
	{
		if (!mob.isOwnerPresent())
		{
			this.removeTargetPos();;
			return false;
		}
		this.setTargetPos(mob.getOwner());
		return super.checkCanUse();
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		teleportToOwner();
		leap();
	}
}
