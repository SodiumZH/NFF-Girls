package net.sodiumstudio.dwmg.entities.ai.goals.target;

import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;

public class DwmgBefriendedOwnerHurtTargetGoal extends BefriendedOwnerHurtTargetGoal
{

	public DwmgBefriendedOwnerHurtTargetGoal(IBefriendedMob inMob)
	{
		super(inMob);
	}
	
	@Override
	public boolean checkCanUse()
	{
		return super.checkCanUse(); //&& !CFavorabilityHandler.isLowFavorability(mob.asMob());
	}
	
}
