package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal;

import java.util.HashSet;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.util.exceptions.UnimplementedException;

public abstract class BefriendedGoal extends Goal {

	// for simplification
	protected static final BefriendedAIState WAIT = BefriendedAIState.WAIT;
	protected static final BefriendedAIState FOLLOW = BefriendedAIState.FOLLOW;
	protected static final BefriendedAIState WANDER = BefriendedAIState.WANDER;
	
	protected IBefriendedMob mob = null;
	private HashSet<BefriendedAIState> allowedStates = new HashSet<BefriendedAIState>();
	private boolean isBlocked = false;
	
	@Deprecated
	public BefriendedGoal()
	{
	}
	
	public BefriendedGoal(IBefriendedMob mob)
	{
		this.mob = mob;
	}
	
	public boolean isStateAllowed()
	{
		return allowedStates.contains(mob.getAIState());
	}
	
	public BefriendedGoal allowState(BefriendedAIState state)
	{
		if (!allowedStates.contains(state))
			allowedStates.add(state);
		return this;
	}
	
	public BefriendedGoal excludeState(BefriendedAIState state)
	{
		if (allowedStates.contains(state))
			allowedStates.remove(state);
		return this;
	}
	
	public BefriendedGoal allowAllStates()
	{
		for (BefriendedAIState state : BefriendedAIState.values())
			allowedStates.add(state);
		return this;
	}
	
	public BefriendedGoal allowAllStatesExceptWait()
	{
		allowAllStates();
		excludeState(WAIT);
		return this;
	}
	
	public void disallowAllStates()
	{
		allowedStates.clear();
	}
	
	public BefriendedGoal block()
	{
		isBlocked = true;
		return this;
	}
	
	public BefriendedGoal unblock()
	{
		isBlocked = false;
		return this;
	}
	
	public boolean isDisabled()
	{
		return mob.getOwner() == null || isBlocked || !allowedStates.contains(mob.getAIState());
	}
	
	public IBefriendedMob getMob()
	{
		return mob;
	}
	
	public PathfinderMob getPathfinder()
	{
		return (PathfinderMob)mob;
	}
	
	@Override
	public boolean canUse() 
	{
		throw new UnimplementedException("BefriendedGoal must override canUse() function.");
	}

}
