package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedMoveGoal;
import net.sodiumstudio.nautils.LevelHelper;
import net.sodiumstudio.nautils.annotation.DontOverride;
import net.sodiumstudio.nautils.exceptions.UnimplementedException;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.AiMaths;

/**
 * Sound methods for all follow-owner goals.
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
	public default BefriendedMoveGoal goal()
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
		if (!goal().getMob().isOwnerPresent())
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
		if (!goal().getMob().isOwnerPresent())
			return;
		Mob mob = goal().getMob().asMob();
		Player owner = goal().getMob().getOwner();
		Vec3 pos = owner.getEyePosition();
		if (mob instanceof PathfinderMob pm)
		{
			pm.getNavigation().moveTo(owner, param);
		}
		else if (mob instanceof FlyingMob fm)
		{
			Vec3 offset1 = owner.position().subtract(fm.position());
			offset1 = new Vec3(offset1.x, 0, offset1.z).normalize().reverse().scale(0.5);	// keep a little distance to player
			fm.getMoveControl().setWantedPosition(pos.x + offset.x + offset1.x, pos.y + offset.y, pos.z + offset.z + offset1.z, param);
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
	@DontOverride
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
		if (!goal().getMob().isOwnerPresent())
			return;
		Mob mob = goal().getMob().asMob();
		Player owner = goal().getMob().getOwner();
		Vec3 ownerPos = owner.getEyePosition();
		if (mob.distanceToSqr(owner) < getTeleportDistance() * getTeleportDistance())
			return;
			
		for (int i = 0; i < 20; ++i) {
			Vec3 pos = ownerPos.add(teleportOffset());
			if (goal().shouldAvoidSun.test(goal().getMob()) && LevelHelper.isUnderSun(new BlockPos(pos), mob))
				continue;
			if (tryTeleportToOwner())
				return;
		}
	}
	
	public default Vec3 teleportOffset()
	{
		return teleportOffsetDefault();
	}
	
	@DontOverride
	public default Vec3 teleportOffsetDefault()
	{
		return AiMaths.randomOvalVector(3, 1, 3).scale(goal().getMob().asMob().getRandom().nextDouble());
	}
	
	/**
	 * Try getting a position around owner and teleport.
	 * @param randomRange Random position offset scale.
	 * @return Whether succeeded.
	 */
	public default boolean tryTeleportToOwner() {
		if (!goal().getMob().isOwnerPresent())
			return false;
		Mob mob = goal().getMob().asMob();
		Player owner = goal().getMob().getOwner();
		Vec3 targetPos = owner.position().add(new Vec3(0, 1, 0)).add(teleportOffset());
		if (!this.posNoCollision(targetPos))
			return false;
		if (goal().shouldAvoidSun.test(goal().getMob()) && LevelHelper.isUnderSun(new BlockPos(targetPos), mob))
			return false;
		else {
			mob.moveTo(targetPos);
			return true;
		}
	}

	@DontOverride
	public default boolean posNoCollision(Vec3 pos) {
		Mob mob = goal().getMob().asMob();
		Vec3 deltaVec = pos.subtract(mob.position());
		return mob.level.noCollision(mob, mob.getBoundingBox().move(deltaVec));
	}

	public default boolean allowTeleport()
	{
		return true;
	}
	
}
