package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal;

import java.util.HashSet;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;

public abstract class BefriendedTargetGoal extends TargetGoal
{

	// for simplification
	protected static final BefriendedAIState WAIT = BefriendedAIState.WAIT;
	protected static final BefriendedAIState FOLLOW = BefriendedAIState.FOLLOW;
	protected static final BefriendedAIState WANDER = BefriendedAIState.WANDER;

	protected IBefriendedMob mob = null;
	protected HashSet<BefriendedAIState> allowedStates = new HashSet<BefriendedAIState>();
	protected boolean isBlocked = false;

	public BefriendedTargetGoal(IBefriendedMob mob, boolean mustSee)
	{
		this(mob, mustSee, false);
	}

	public BefriendedTargetGoal(IBefriendedMob mob, boolean mustSee, boolean mustReach)
	{
		super(mob.asMob(), mustSee, mustReach);
		this.mob = mob;
	}

	public HashSet<BefriendedAIState> getAllowedStates()
	{
		return allowedStates;
	}
	
	public boolean isStateAllowed() {
		return allowedStates.contains(mob.getAIState());
	}

	public BefriendedTargetGoal allowState(BefriendedAIState state) {
		if (!allowedStates.contains(state))
			allowedStates.add(state);
		return this;
	}

	public BefriendedTargetGoal excludeState(BefriendedAIState state) {
		if (allowedStates.contains(state))
			allowedStates.remove(state);
		return this;
	}

	public BefriendedTargetGoal allowAllStates() {
		for (BefriendedAIState state : BefriendedAIState.values())
			allowedStates.add(state);
		return this;
	}

	public BefriendedTargetGoal allowAllStatesExceptWait() {
		allowAllStates();
		excludeState(WAIT);
		return this;
	}

	// Disable this goal
	public void blockGoal() {
		isBlocked = true;
	}

	public void resumeGoal() {
		isBlocked = false;
	}

	public boolean isDisabled() {
		return isBlocked || !isStateAllowed();
	}

	public LivingEntity getLiving() {
		return (LivingEntity) mob;
	}

	public Mob getMob() {
		return (Mob) mob;
	}

	public PathfinderMob getPathfinder() {
		return (PathfinderMob) mob;
	}

	@Override
	public boolean canUse() {
		return false;
	}

}
