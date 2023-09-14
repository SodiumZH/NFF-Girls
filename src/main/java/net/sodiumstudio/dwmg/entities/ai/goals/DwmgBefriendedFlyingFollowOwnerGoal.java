package net.sodiumstudio.dwmg.entities.ai.goals;

import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedFlyingFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;

public class DwmgBefriendedFlyingFollowOwnerGoal extends BefriendedFlyingFollowOwnerGoal
{

	public DwmgBefriendedFlyingFollowOwnerGoal(IBefriendedMob mob)
	{
		super(mob);
	}

	@Override
	public boolean checkCanUse()
	{
		return super.checkCanUse() && !CFavorabilityHandler.isLowFavorability(mob.asMob());
	}
	
}
