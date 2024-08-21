package net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move;

import javax.annotation.Nullable;

import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class BefriendedRandomSwimGoal extends BefriendedRandomStrollGoal
{
	public BefriendedRandomSwimGoal(IBefriendedMob mob, double speedModifier, int interval)
	{
		super(mob, speedModifier, interval);
	}

	@Override
	@Nullable
	protected Vec3 getPosition() 
	{
		return BehaviorUtils.getRandomSwimmablePos(this.getPathfinder(), 10, 7);
	}
}
