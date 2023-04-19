package net.sodiumstudio.dwmg.befriendmobs.entity.ai;

import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

public interface DynamicGoalHandler
{
	

	public HashSet<Goal> getGoalSet();
	
	public default Goal getGoal(Class<? extends Goal> type)
	{
		for (Goal goal: getGoalSet())
		{
			if (goal.getClass().equals(type))
				return goal;
		}
		return null;
	}
	
	public default Mob getMob()
	{
		return (Mob)this;
	}
	
}
