package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;

public class BefriendedFlyingFollowOwnerGoal extends BefriendedFlyingMoveGoal implements IBefriendedFollowOwner
{
	public double teleportDistance = 12d;
	public double noFollowOnCombatDistance = 6d;
	public double minStartDistance = 4d;
	
	public BefriendedFlyingFollowOwnerGoal(IBefriendedMob mob, double moveSpeed, int width, int height)
	{
		super(mob);
		this.disallowAllStates();
		this.allowState(BefriendedAIState.FOLLOW);
	}

	public BefriendedFlyingFollowOwnerGoal(IBefriendedMob mob, double moveSpeed)
	{
		this(mob, moveSpeed, 3, 2);
	}

	public BefriendedFlyingFollowOwnerGoal(IBefriendedMob mob)
	{
		this(mob, 0.25D);
	}
	
	@Override
	public boolean checkCanUse()
	{
		if (mob.asMob().getMoveControl().hasWanted())
			return false;
		if (!mob.isOwnerPresent())
			return false;
		if (mob.asMob().getTarget() != null && mob.asMob().distanceToSqr(mob.getOwner()) < noFollowOnCombatDistance * noFollowOnCombatDistance)
			return false;
		if (mob.asMob().distanceToSqr(mob.getOwner()) < minStartDistance * minStartDistance)
			return false;
		else return true;
	}
	
	@Override
	public void tick() {
		if (!mob.isOwnerPresent())
			return;	// Prevent potential nullptr crash
		goToOwnerPreset(getActualSpeed());
	}	
	
	@Override
	public void moveToOwner(double param, Vec3 offset)
	{
		if (!goal().getMob().isOwnerPresent())
			return;
		Mob mob = goal().getMob().asMob();
		Player owner = goal().getMob().getOwner();
		Vec3 pos = owner.getEyePosition();
		Vec3 offset1 = owner.position().subtract(mob.position());
		offset1 = new Vec3(offset1.x, 0, offset1.z).normalize().reverse().scale(0.5);	// keep a little distance to player
		mob.getMoveControl().setWantedPosition(pos.x + offset.x + offset1.x, pos.y + offset.y, pos.z + offset.z + offset1.z, param);
	}
}
