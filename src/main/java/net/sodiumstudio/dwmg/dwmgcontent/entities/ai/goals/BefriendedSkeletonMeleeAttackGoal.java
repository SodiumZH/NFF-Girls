package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import net.minecraft.world.item.BowItem;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedMeleeAttackGoal;

public class BefriendedSkeletonMeleeAttackGoal extends BefriendedMeleeAttackGoal
{

	public BefriendedSkeletonMeleeAttackGoal(IBefriendedMob pMob, double pSpeedModifier,
			boolean pFollowingTargetEvenIfNotSeen)
	{
		super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
	}

	@Override
	public boolean canUse()
	{
		if (mob.asMob().isHolding(is -> is.getItem() instanceof BowItem))
			return false;
		else return super.canUse();
	}
}
