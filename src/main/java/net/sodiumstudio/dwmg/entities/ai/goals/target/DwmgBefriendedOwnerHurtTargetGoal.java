package net.sodiumstudio.dwmg.entities.ai.goals.target;

import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;

public class DwmgBefriendedOwnerHurtTargetGoal extends BefriendedOwnerHurtTargetGoal
{

	public DwmgBefriendedOwnerHurtTargetGoal(IBefriendedMob inMob)
	{
		super(inMob);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && !CFavorabilityHandler.isLowFavorability(mob.asMob());
	}
	
}
