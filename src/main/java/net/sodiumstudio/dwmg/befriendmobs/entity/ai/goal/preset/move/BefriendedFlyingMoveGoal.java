package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.PathfinderMob;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedMoveGoal;

public class BefriendedFlyingMoveGoal extends BefriendedMoveGoal
{

	public BefriendedFlyingMoveGoal(IBefriendedMob mob)
	{
		super(mob);
	}

	@Override
	public PathfinderMob getPathfinder()
	{
		throw new IllegalArgumentException("getPathfinder() is invalid in BefriendedFlyingMoveGoal as it doesn't accept PathfinderMob.");
	}

	public FlyingMob getFlying()
	{
		return (FlyingMob)mob;
	}
	
}
