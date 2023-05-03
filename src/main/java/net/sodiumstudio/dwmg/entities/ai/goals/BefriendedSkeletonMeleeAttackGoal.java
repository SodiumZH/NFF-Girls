package net.sodiumstudio.dwmg.entities.ai.goals;

import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;


public class BefriendedSkeletonMeleeAttackGoal extends BefriendedMeleeAttackGoal
{

	public BefriendedSkeletonMeleeAttackGoal(IBefriendedMob pMob, double pSpeedModifier,
			boolean pFollowingTargetEvenIfNotSeen)
	{
		super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
	}

}
