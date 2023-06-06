package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;

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
	public void moveToOwner(double param)
	{
		if (!asGoal().getMob().isOwnerPresent())
			return;
		Mob mob = asGoal().getMob().asMob();
		Player owner = asGoal().getMob().getOwner();
		Vec3 pos = owner.getEyePosition();
		mob.getMoveControl().setWantedPosition(pos.x, pos.y, pos.z, param);
	}
}
