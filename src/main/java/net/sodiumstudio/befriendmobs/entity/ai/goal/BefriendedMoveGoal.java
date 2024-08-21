package net.sodiumstudio.befriendmobs.entity.ai.goal;


import java.util.function.Predicate;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedAmphibious;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

/**
 * The base class of all befriended mob goals for moving to somewhere,
 * including pathfinding for PathfinderMob, flying for FlyMob, etc.
 */
public abstract class BefriendedMoveGoal extends BefriendedGoal
{
	/** True if the mob is a PathfinderMob. */
	public boolean isPathfinding = true;
	/** Check condition that the mob should avoid sun. */
	public Predicate<IBefriendedMob> shouldAvoidSun = (mob -> false);
	/** Only for Pathfinder. If true, the mob should have both ground and water navigation, and must implement {@link IBefriendedAmphibious} interface. */
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
	
	/** Set the goal should always avoid sun */
	public BefriendedMoveGoal alwaysAvoidSun()
	{
		shouldAvoidSun = (mob -> true);
		return this;
	}
	
	/** Set the sun-avoiding condition of the mob */
	public BefriendedMoveGoal avoidSunCondition(Predicate<IBefriendedMob> condition)
	{
		shouldAvoidSun = condition;
		return this;
	}
	
	/** (Must be pathfinding)
	 * <p>Set this goal should support amphibious mobs (having both water and ground navigations).
	* If amphibious, the mob must implement IBefriendedAmphibious interface.
	 */
	public BefriendedMoveGoal amphibious()
	{
		canSwim = true;
		isAmphibious = true;
		return this;
	}
	
	/** (Must be pathfinding)
	 * <p>Set the mob can only swim in water (or other liquid) and cannot move outside water.
	 */
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
	
	/**
	 * Set the mob can only fly. Either pathfinding or not.
	 */
	public BefriendedMoveGoal flyOnly()
	{
		canWalk = false;
		canSwim = false;
		canFly = true;
		return this;
	}
	
	/** Only for ground pathfinding, set the mob can step onto leaves. */
	public BefriendedMoveGoal canStepOntoLeaves()
	{
		canStepOntoLeaves = true;
		return this;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		if (isAmphibious)
		{
			if (mob.asMob().isInWater() && mob.asMob().level.getBlockState(new BlockPos(mob.asMob().getEyePosition())).is(Blocks.WATER))
				((IBefriendedAmphibious)mob).switchNav(true);
			else ((IBefriendedAmphibious)mob).switchNav(false);		
		}
	}
	
}
