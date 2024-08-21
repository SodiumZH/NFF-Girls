package net.sodiumstudio.befriendmobs.entity.ai.goal.preset;

import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

/* Block the actions below under certain conditions */
public abstract class BefriendedBlockActionGoal extends BefriendedGoal
{
	
	public BefriendedBlockActionGoal(IBefriendedMob mob)
	{
		super(mob);
	}
	
	@Override
	public boolean checkCanUse()
	{
		return blockCondition();
	}
	
	@Override
	public boolean checkCanContinueToUse()
	{
		return blockCondition();
	}
	
	@Override
	public void onStart()
	{
		mob.asMob().getNavigation().stop();
	}
	
	public abstract boolean blockCondition();
}

