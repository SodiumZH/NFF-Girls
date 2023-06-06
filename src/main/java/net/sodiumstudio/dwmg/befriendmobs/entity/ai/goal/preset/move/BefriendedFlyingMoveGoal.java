package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import javax.annotation.Nullable;

import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedMoveGoal;

/**
 * Movement base for FlyingMob. No PathfinderMob.
 */
public abstract class BefriendedFlyingMoveGoal extends BefriendedMoveGoal
{
	public double speed = 0.25d;
	
	
	public BefriendedFlyingMoveGoal(IBefriendedMob mob, double speed)
	{
		super(mob);
		this.speed = speed;
		flyOnly();		
	}
	
	public BefriendedFlyingMoveGoal(IBefriendedMob mob)
	{
		this(mob, 0.25d);
	}

	public void flyTo(Vec3 targetPos, double speed)
	{
		MoveControl control = mob.asMob().getMoveControl();
		control.setWantedPosition(targetPos.x, targetPos.y, targetPos.z, speed);
	}
	
	/* Util */
		
	public Vec3 getWantedMovementVector()
	{
		if (mob.asMob().getMoveControl() == null || !mob.asMob().getMoveControl().hasWanted())
			return Vec3.ZERO;
		double deltaX = mob.asMob().getMoveControl().getWantedX() - mob.asMob().getX();
		double deltaY = mob.asMob().getMoveControl().getWantedY() - mob.asMob().getY();
		double deltaZ = mob.asMob().getMoveControl().getWantedZ() - mob.asMob().getZ();
		return new Vec3(deltaX, deltaY, deltaZ);
	}
	
	public double distSqrToOwner()
	{
		if (!mob.isOwnerPresent())
			return 0;
		else return mob.asMob().distanceToSqr(mob.getOwner());
	}
	
	protected double getActualSpeed()
	{
		if (mob.asMob().getAttribute(Attributes.FLYING_SPEED) != null)
			return speed * mob.asMob().getAttributeValue(Attributes.FLYING_SPEED) / mob.asMob().getAttributeBaseValue(Attributes.FLYING_SPEED);
		else if (mob.asMob().getAttribute(Attributes.MOVEMENT_SPEED) != null)
			return speed * mob.asMob().getAttributeValue(Attributes.MOVEMENT_SPEED) / mob.asMob().getAttributeBaseValue(Attributes.MOVEMENT_SPEED);
		else return speed;
	}
}
