package com.sodium.dwmg.entities.ai;

import com.sodium.dwmg.entities.IBefriendedMob;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

public abstract class BefriendedGoal extends Goal {

	protected final IBefriendedMob mob;
	
	public BefriendedGoal(IBefriendedMob mob)
	{
		this.mob = mob;
	}
	
	public LivingEntity getLiving()
	{
		return (LivingEntity)mob;
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
