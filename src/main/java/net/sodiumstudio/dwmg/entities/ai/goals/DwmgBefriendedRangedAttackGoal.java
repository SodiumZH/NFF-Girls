package net.sodiumstudio.dwmg.entities.ai.goals;

import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedRangedAttackGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class DwmgBefriendedRangedAttackGoal extends BefriendedRangedAttackGoal
{

	public DwmgBefriendedRangedAttackGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackIntervalMin,
			int pAttackIntervalMax, float pAttackRadius)
	{
		super(mob, pSpeedModifier, pAttackIntervalMin, pAttackIntervalMax, pAttackRadius);
	}

	public DwmgBefriendedRangedAttackGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackInterval,
			float pAttackRadius)
	{
		super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
	}

	@Override
	public boolean checkCanContinueToUse()
	{
		if (this.mob.isOwnerPresent() && this.mob.getAIState() == BefriendedAIState.FOLLOW
				&& this.mob.asMob().distanceToSqr(this.mob.getOwner()) > 256d)
			return false;
		return super.checkCanContinueToUse();
	}
	
}
