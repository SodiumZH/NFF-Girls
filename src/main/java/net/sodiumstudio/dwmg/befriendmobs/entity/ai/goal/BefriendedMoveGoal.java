package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.dwmgcontent.entities.IBefriendedAmphibious;

// Superclass of all befriended goals about "pathfinding move to somewhere".
public abstract class BefriendedMoveGoal extends BefriendedGoal
{
	public boolean shouldAvoidSun = false;
	public boolean isAmphibious = false;
	public double speedModifier = 1.0d;
	public boolean canFly = false;
	public boolean canStepOntoLeaves = false;
	
	
	public BefriendedMoveGoal(IBefriendedMob mob) {
		super(mob);
	}
	
	public BefriendedMoveGoal(IBefriendedMob mob, double speedModifier) {
		super(mob);
		this.speedModifier = speedModifier;
	}
	
	/* Additional modules */
	
	// Set the goal should avoid sun
	public BefriendedMoveGoal avoidSun()
	{
		shouldAvoidSun = true;
		return this;
	}
	
	// Set this goal should support amphibious mobs i.e. both water and ground navigations.
	// If amphibious, the mob must implement IBefriendedAmphibious interface.
	public BefriendedMoveGoal amphibious()
	{
		isAmphibious = true;
		return this;
	}
	
	public BefriendedMoveGoal canFly()
	{
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
			if (mob.asMob().isInWater() && mob.asMob().level.getBlockState(new BlockPos(mob.asMob().getEyePosition())).is(Blocks.WATER))
				((IBefriendedAmphibious)mob).switchNav(true);
			else ((IBefriendedAmphibious)mob).switchNav(false);		
		}
	}
	
}
