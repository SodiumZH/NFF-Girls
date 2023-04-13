package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal;

import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;

/* Block the actions below under certain conditions */
public abstract class BefriendedBlockActionGoal extends BefriendedGoal
{
	
	public BefriendedBlockActionGoal(IBefriendedMob mob)
	{
		super(mob);
	}
	
	@Override
	public boolean canUse()
	{
		return blockCondition();
	}
	
	@Override
	public boolean canContinueToUse()
	{
		return blockCondition();
	}
	
	public abstract boolean blockCondition();
}

