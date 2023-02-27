package com.sodium.dwmg.entities.ai.goals;

import com.sodium.dwmg.entities.IBefriendedMob;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class BefriendedGoal extends Goal {

	protected IBefriendedMob mob = null;

	public LivingEntity getLiving()
	{
		return (LivingEntity)mob;
	}
	
	public Mob getMob()
	{
		return (Mob)mob;
	}
	
	public PathfinderMob getPathfinder()
	{
		return (PathfinderMob)mob;
	}
	
	@Override
	public boolean canUse() 
	{
		return false;
	}

}
