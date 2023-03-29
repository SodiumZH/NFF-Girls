package net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal;

import java.util.HashSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.BefriendedAIState;

public abstract class BefriendedGoal extends Goal {

	// for simplification
	protected static final BefriendedAIState WAIT = BefriendedAIState.WAIT;
	protected static final BefriendedAIState FOLLOW = BefriendedAIState.FOLLOW;
	protected static final BefriendedAIState WANDER = BefriendedAIState.WANDER;
	
	protected IBefriendedMob mob = null;
	private HashSet<BefriendedAIState> allowedStates = new HashSet<BefriendedAIState>();
	private boolean isBlocked = false;
	
	
	
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
		return isBlocked || !allowedStates.contains(mob.getAIState());
	}
	
	@Deprecated // use mob.asMob() instead
	public Mob getMob()
	{
		return mob.asMob();
	}
	
	public PathfinderMob getPathfinder()
	{
		return (PathfinderMob)mob;
	}
	
	@Override
	public boolean canUse() 
	{
		throw new UnsupportedOperationException("BefriendedGoal must override canUse() function.");
	}

}
