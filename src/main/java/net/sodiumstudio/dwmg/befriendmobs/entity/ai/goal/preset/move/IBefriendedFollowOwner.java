package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedMoveGoal;
import net.sodiumstudio.befriendmobs.util.LevelHelper;
import net.sodiumstudio.befriendmobs.util.annotation.DontCallManually;
import net.sodiumstudio.befriendmobs.util.exceptions.UnimplementedException;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.AiMaths;

/**
 * Common methods for all follow-owner goals.
 */
public interface IBefriendedFollowOwner
{
	
	/**
	 * How far away should the mob try teleporting to owner. 12 by default.
	 */
	public default double getTeleportDistance()
	{
		return 12d;
	}
	
	/**
	 * Cast from interface to goal
	 */
	public default BefriendedMoveGoal asGoal()
	{
		if (this instanceof BefriendedMoveGoal bg)
			return bg;
		else throw new RuntimeException("IBefriendedFollowOwner interface can only attached to BefriendedMoveGoal.");
	}

	/**
	 * Try teleport to owner if far enough, then move to owner.
	 * @param speedModifier For PathfinderMob it's speed modifier rate; For FlyingMob it's movement speed.
	 */
	public default void goToOwnerPreset(double speedModifier)
	{
		if (!asGoal().getMob().isOwnerPresent())
			return;
		//Mob mob = asGoal().getMob().asMob();
		teleportToOwner();	// Distance check is inside
		moveToOwner(speedModifier);
	}
	
	/**
	 * Do normal movement to owner
	 * @param param For PathfinderMob it's speed modifier rate; For FlyingMob it's movement speed.
	 * @param offset Vector away from player the mob should set target position to.
	 */
	public default void moveToOwner(double param, Vec3 offset)
	{
		if (!asGoal().getMob().isOwnerPresent())
			return;
		Mob mob = asGoal().getMob().asMob();
		Player owner = asGoal().getMob().getOwner();
		Vec3 pos = owner.getEyePosition();
		if (mob instanceof PathfinderMob pm)
		{
			pm.getNavigation().moveTo(owner, param);
		}
		else if (mob instanceof FlyingMob fm)
		{
			fm.getMoveControl().setWantedPosition(pos.x + offset.x, pos.y + offset.y, pos.z + offset.z, param);
		}
		else 
		{
			throw new UnimplementedException("IBefriendedFollowOwner: the mob is neither PathfinderMob nor FlyingMob, so doesn't have a movement preset. Please implement IBefriendedFollowOwner::moveToOwner for custom types.");
		}
	}
	
	/**
	 * Do normal movement to owner
	 * @param param For PathfinderMob it's speed modifier rate; For FlyingMob it's movement speed.
	 */
	public default void moveToOwner(double param)
	{
		moveToOwner(param, Vec3.ZERO);
	}
	
	/**
	 * Do teleport attempt to owner
	 */
	public default void teleportToOwner() {
		if (!allowTeleport())
			return;	
		if (!asGoal().getMob().isOwnerPresent())
			return;
		Mob mob = asGoal().getMob().asMob();
		Player owner = asGoal().getMob().getOwner();
		Vec3 ownerPos = owner.getEyePosition();
		if (mob.distanceToSqr(owner) < getTeleportDistance() * getTeleportDistance())
			return;
			
		for (int i = 0; i < 20; ++i) {
			Vec3 pos = ownerPos.add(teleportOffset());
			if (asGoal().shouldAvoidSun.test(asGoal().getMob()) && LevelHelper.isUnderSun(new BlockPos(pos), mob))
				continue;
			if (tryTeleportToOwner())
				return;
		}
	}
	
	public default Vec3 teleportOffset()
	{
		return AiMaths.randomOvalVector(3, 1, 3).scale(asGoal().getMob().asMob().getRandom().nextDouble());
	}
	
	/**
	 * Try getting a position around owner and teleport.
	 * @param randomRange Random position offset scale.
	 * @return Whether succeeded.
	 */
	public default boolean tryTeleportToOwner() {
		if (!asGoal().getMob().isOwnerPresent())
			return false;
		Mob mob = asGoal().getMob().asMob();
		Player owner = asGoal().getMob().getOwner();
		Vec3 targetPos = owner.position().add(new Vec3(0, 1, 0)).add(teleportOffset());
		if (!this.posNoCollision(targetPos))
			return false;
		if (asGoal().shouldAvoidSun.test(asGoal().getMob()) && LevelHelper.isUnderSun(new BlockPos(targetPos), mob))
			return false;
		else {
			mob.moveTo(targetPos);
			return true;
		}
	}

	public default boolean posNoCollision(Vec3 pos) {
		Mob mob = asGoal().getMob().asMob();
		Vec3 deltaVec = pos.subtract(mob.position());
		return mob.level.noCollision(mob, mob.getBoundingBox().move(deltaVec));
	}

	public default boolean allowTeleport()
	{
		return true;
	}
	
}