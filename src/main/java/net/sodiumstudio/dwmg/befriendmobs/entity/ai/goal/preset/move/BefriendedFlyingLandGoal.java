package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.nautils.LevelHelper;

public class BefriendedFlyingLandGoal extends BefriendedFlyingMoveGoal
{

	public BefriendedFlyingLandGoal(IBefriendedMob mob, double speed)
	{
		super(mob, speed);
		this.disallowAllStates();
		this.allowState(WAIT);
	}

	public BefriendedFlyingLandGoal(IBefriendedMob mob)
	{
		this(mob, 0.25d);
	}
	
	@Override
	public boolean checkCanUse() {
		Level level = mob.asMob().level();
		if (mob.asMob().getMoveControl().hasWanted())
			return false;
		if (!level.getBlockState(mob.asMob().blockPosition().below()).isAir())
			return false;
		if (LevelHelper.isAboveVoid(mob.asMob().blockPosition(), mob.asMob()))
			return false;
		if (mob.asMob().getTarget() != null)
			return false;
		else return true;		
	}

	@Override
	public void tick()
	{
		if (!mob.isOwnerPresent())
			return;	// Prevent potential nullptr crash
		if (mob.asMob().getMoveControl().hasWanted())
			return;
		if (LevelHelper.isAboveVoid(mob.asMob().blockPosition(), mob.asMob()))
			return;
		BlockPos pos = mob.asMob().blockPosition();
		while (mob.asMob().level().getBlockState(pos).isAir() && pos.getY() >= mob.asMob().level().getMinBuildHeight())
			pos = pos.below();
		pos = pos.above();
		mob.asMob().getMoveControl().setWantedPosition(pos.getX(), pos.getY(), pos.getZ(), speed);
	}
	
}
