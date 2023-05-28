package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.util.LevelHelper;

public class BefriendedFlyingFollowOwnerGoal extends BefriendedFlyingMoveGoal
{
	
	public float maxStepLength = 8f;
	public double teleportDistance = 12d;
	
    public BefriendedFlyingFollowOwnerGoal(IBefriendedMob mob) {
    	super(mob);
       this.setFlags(EnumSet.of(Goal.Flag.MOVE));
       this.allowState(WANDER);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    @Override
	public boolean canUse() {
       MoveControl movecontrol = this.mob.asMob().getMoveControl();
       if (!movecontrol.hasWanted()) {
          return true;
       } else {
          double d0 = movecontrol.getWantedX() - this.mob.asMob().getX();
          double d1 = movecontrol.getWantedY() - this.mob.asMob().getY();
          double d2 = movecontrol.getWantedZ() - this.mob.asMob().getZ();
          double d3 = d0 * d0 + d1 * d1 + d2 * d2;
          return d3 < 1.0D || d3 > 3600.0D;
       }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
	public boolean canContinueToUse() {
       return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void start() {
    	// Sometimes it happens to invoke on initialization
    	if (mob.getOwner() == null)
    		return;
    	if (this.mob.asMob().distanceToSqr(mob.getOwner()) >= teleportDistance * teleportDistance)
    	{
    		teleportToOwner();
    	}
    	else
    	{
    		moveToOwner();
    	}
    }
    
	protected void teleportToOwner() {
		BlockPos blockpos = mob.getOwner().blockPosition();

		for (int i = 0; i < 20; ++i) {
			int j = this.randomIntInclusive(-3, 3);
			int k = this.randomIntInclusive(-1, 1);
			int l = this.randomIntInclusive(-3, 3);
			BlockPos wanted = new BlockPos(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
			// Don't teleport to positions under sun if avoiding
			if (shouldAvoidSun.test(mob) && LevelHelper.isUnderSun(wanted, mob.asMob()) && !LevelHelper.isAboveWater(wanted, mob.asMob()))
				continue;
			boolean flag = this.tryTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
			if (flag) {
				return;
			}
		}
	}

	protected void moveToOwner() {
		BlockPos blockpos = mob.getOwner().blockPosition();

		int j = 0;
		int k = 0;
		int l = 0;
		boolean flag = false;
		
		for (int i = 0; i < 20; ++i) {
			j = this.randomIntInclusive(-3, 3);
			k = this.randomIntInclusive(-1, 1);
			l = this.randomIntInclusive(-3, 3);
			BlockPos wanted = new BlockPos(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
			// Don't move to positions under sun if avoiding
			if (shouldAvoidSun.test(mob) && LevelHelper.isUnderSun(wanted, mob.asMob()) && !LevelHelper.isAboveWater(wanted, mob.asMob()))
				continue;
			else
			{
				flag = true;
				break;
			}
		}
		if (flag)
			this.mob.asMob().getMoveControl().setWantedPosition(j, k, l, 1.0D);
	}
	
	protected boolean tryTeleportTo(int pX, int pY, int pZ) {
		if (Math.abs((double) pX - mob.getOwner().getX()) < 2.0D
				&& Math.abs((double) pZ - mob.getOwner().getZ()) < 2.0D) {
			return false;
		} else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) {
			return false;
		} else {
			mob.asMob().moveTo((double) pX + 0.5D, (double) pY, (double) pZ + 0.5D, mob.asMob().getYRot(),
					mob.asMob().getXRot());
			return true;
		}
	}

	protected boolean canTeleportTo(BlockPos pos) {
		if (!allowTeleport())
			return false;		
		BlockPos blockpos = pos.subtract(mob.asMob().blockPosition());
		return this.mob.asMob().level.noCollision(mob.asMob(), mob.asMob().getBoundingBox().move(blockpos));
	}

	protected boolean allowTeleport()
	{
		return true;
	}
	
	protected int randomIntInclusive(int pMin, int pMax) {
		return mob.asMob().getRandom().nextInt(pMax - pMin + 1) + pMin;
	}
}
