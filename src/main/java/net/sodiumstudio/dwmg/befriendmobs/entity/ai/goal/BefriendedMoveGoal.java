package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal;

import java.util.function.Predicate;

import net.minecraft.world.level.block.Blocks;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.IBefriendedAmphibious;

// Superclass of all befriended goals about "pathfinding move to somewhere".
public abstract class BefriendedMoveGoal extends BefriendedGoal
{
	
	public Predicate<IBefriendedMob> shouldAvoidSun = (mob -> false);
	public boolean isAmphibious = false;
	public double speedModifier = 1.0d;
	public boolean canFly = false;
	public boolean canSwim = false;
	public boolean canWalk = true;
	public boolean canStepOntoLeaves = false;
	protected boolean isFlying = false;
	
	/* additional modules */
	
	public BefriendedMoveGoal(IBefriendedMob mob) {
		super(mob);
	}
	
	public BefriendedMoveGoal(IBefriendedMob mob, double speedModifier) {
		super(mob);
		this.speedModifier = speedModifier;
	}
	
	/* Additional modules */
	
	// Set the goal should always avoid sun
	public BefriendedMoveGoal alwaysAvoidSun()
	{
		shouldAvoidSun = (mob -> true);
		return this;
	}
	
	public BefriendedMoveGoal avoidSunCondition(Predicate<IBefriendedMob> condition)
	{
		shouldAvoidSun = condition;
		return this;
	}
	
	
	// Set this goal should support amphibious mobs i.e. both water and ground navigations.
	// If amphibious, the mob must implement IBefriendedAmphibious interface.
	public BefriendedMoveGoal amphibious()
	{
		canSwim = true;
		isAmphibious = true;
		return this;
	}
	
	public BefriendedMoveGoal waterOnly()
	{
		canWalk = false;
		canSwim = true;
		return this;
	}
	
	public BefriendedMoveGoal canFly()
	{
		canFly = true;
		return this;
	}
	
	public BefriendedMoveGoal flyOnly()
	{
		canWalk = false;
		canSwim = false;
		canFly = true;
		return this;
	}
	
	
	public BefriendedMoveGoal canStepOntoLeaves()
	{
		canStepOntoLeaves = true;
		return this;
	}
	
	@Override
	public void start()
	{
		super.start();
		if (isAmphibious)
		{
			if (mob.asMob().isInWater() && mob.asMob().level.getBlockState(mob.asMob().eyeBlockPosition()).is(Blocks.WATER))
				((IBefriendedAmphibious)mob).switchNav(true);
			else ((IBefriendedAmphibious)mob).switchNav(false);		
		}
	}
	
}
