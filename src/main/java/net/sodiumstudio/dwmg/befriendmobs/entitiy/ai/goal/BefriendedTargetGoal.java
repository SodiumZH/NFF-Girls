package net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal;

import java.util.HashSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.BefriendedAIState;

public abstract class BefriendedTargetGoal extends TargetGoal {
	
	// for simplification
	protected static final BefriendedAIState WAIT = BefriendedAIState.WAIT;
	protected static final BefriendedAIState FOLLOW = BefriendedAIState.FOLLOW;
	protected static final BefriendedAIState WANDER = BefriendedAIState.WANDER;
	
	protected IBefriendedMob mob = null;
	private HashSet<BefriendedAIState> allowedStates = new HashSet<BefriendedAIState>();
	protected boolean isBlocked = false;
	
	public BefriendedTargetGoal(Mob pMob, boolean pMustSee) {
		super(pMob, pMustSee);
		mob = (IBefriendedMob)pMob;
	}

	public BefriendedTargetGoal(IBefriendedMob pMob, boolean pMustSee) {
		super((Mob)pMob, pMustSee);
		mob = pMob;
	}
	
	public boolean isStateAllowed()
	{
		return allowedStates.contains(mob.getAIState());
	}
	
	public void allowState(BefriendedAIState state)
	{
		if (!allowedStates.contains(state))
			allowedStates.add(state);
	}
	
	public void disallowState(BefriendedAIState state)
	{
		if (allowedStates.contains(state))
			allowedStates.remove(state);
	}
	
	public void allowAllStates()
	{
		for (BefriendedAIState state : BefriendedAIState.values())
			allowedStates.add(state);
	}
	
	public void allowAllStatesExceptWait()
	{
		allowAllStates();
		disallowState(WAIT);
	}
	
	// Disable this goal
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
		return isBlocked || !isStateAllowed();
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
	public boolean canUse() {
		return false;
	}

}
