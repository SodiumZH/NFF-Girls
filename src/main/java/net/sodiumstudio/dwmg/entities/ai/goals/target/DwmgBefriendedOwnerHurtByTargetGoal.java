package net.sodiumstudio.dwmg.entities.ai.goals.target;

import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;

public class DwmgBefriendedOwnerHurtByTargetGoal extends BefriendedOwnerHurtByTargetGoal
{

	public DwmgBefriendedOwnerHurtByTargetGoal(IBefriendedMob inMob)
	{
		super(inMob);
	}
	
	@Override
	public boolean checkCanUse()
	{
		return super.checkCanUse() && !CFavorabilityHandler.isLowFavorability(mob.asMob());
	}
	
}
