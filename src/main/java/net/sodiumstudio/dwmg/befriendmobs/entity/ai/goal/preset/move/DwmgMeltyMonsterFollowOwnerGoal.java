package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;

public class DwmgMeltyMonsterFollowOwnerGoal extends DwmgBefriendedFollowOwnerGoal
{

	protected float oldLavaCost;
	
	public DwmgMeltyMonsterFollowOwnerGoal(IBefriendedMob inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly)
	{
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected boolean canTeleportTo(BlockPos pos)
	{
		if (super.canTeleportTo(pos))
			return true;
		if (mob.asMob().level().getBlockState(pos).is(Blocks.LAVA))
		{
			BlockPos blockpos = pos.subtract(getPathfinder().blockPosition());
			return this.level.noCollision(getPathfinder(), getPathfinder().getBoundingBox().move(blockpos));
		} 
		else return false;
	}
	
	@Override
	public void start()
	{
		super.start();
		this.oldLavaCost = getPathfinder().getPathfindingMalus(BlockPathTypes.LAVA);
		getPathfinder().setPathfindingMalus(BlockPathTypes.LAVA, 0.0F);
	}
	
	@Override
	public void stop()
	{
		super.stop();
		getPathfinder().setPathfindingMalus(BlockPathTypes.LAVA, oldLavaCost);
	}
	
	
}



