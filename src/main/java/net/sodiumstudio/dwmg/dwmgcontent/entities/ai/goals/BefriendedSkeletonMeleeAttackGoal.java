package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;


public class BefriendedSkeletonMeleeAttackGoal extends BefriendedMeleeAttackGoal
{

	public BefriendedSkeletonMeleeAttackGoal(IBefriendedMob pMob, double pSpeedModifier,
			boolean pFollowingTargetEvenIfNotSeen)
	{
		super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
	}

}
