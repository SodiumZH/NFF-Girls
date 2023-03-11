package net.sodiumstudio.befriendmobs.entitiy.ai.goal;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sodiumstudio.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entitiy.ai.BefriendedAIState;

@SuppressWarnings("unused")
public abstract class BefriendedGoal extends Goal {

	// for simplification
	protected static final BefriendedAIState WAIT = BefriendedAIState.WAIT;
	protected static final BefriendedAIState FOLLOW = BefriendedAIState.FOLLOW;
	protected static final BefriendedAIState WANDER = BefriendedAIState.WANDER;
	
	protected IBefriendedMob mob = null;
	protected HashSet<BefriendedAIState> allowedStates = new HashSet<BefriendedAIState>();
	protected boolean isBlocked = false;
	
	public boolean isStateAllowed()
	{
		return allowedStates.contains(mob.getAIState());
	}
	
	public void allowAllStates()
	{
		for (BefriendedAIState state : BefriendedAIState.values())
			allowedStates.add(state);
	}
	
	public void blockGoal()
	{
		isBlocked = true;
	}
	
	public void resumeGoal()
	{
		isBlocked = false;
	}
	
	public boolean isDisabled()
	{
		return isBlocked || !allowedStates.contains(mob.getAIState());
	}
	
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
