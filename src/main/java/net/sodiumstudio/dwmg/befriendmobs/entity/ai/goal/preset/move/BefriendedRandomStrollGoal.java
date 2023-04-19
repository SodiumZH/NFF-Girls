package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedMoveGoal;

public class BefriendedRandomStrollGoal extends BefriendedMoveGoal {

	public static final int DEFAULT_INTERVAL = 120;
	protected double wantedX;
	protected double wantedY;
	protected double wantedZ;
	protected int interval;
	protected boolean forceTrigger;
	private final boolean checkNoActionTime;

	public BefriendedRandomStrollGoal(IBefriendedMob pMob, double pSpeedModifier) {
	      this(pMob, pSpeedModifier, 120);
	   }

	public BefriendedRandomStrollGoal(IBefriendedMob pMob, double pSpeedModifier, int pInterval) {
	      this(pMob, pSpeedModifier, pInterval, true);
	   }

	public BefriendedRandomStrollGoal(IBefriendedMob pMob, double pSpeedModifier, int pInterval, boolean pCheckNoActionTime) {
		  super(pMob, pSpeedModifier);
	      this.interval = pInterval;
	      this.checkNoActionTime = pCheckNoActionTime;
	      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	      allowState(WANDER);
	   }

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
	@Override
	public boolean canUse() {
		if (isDisabled())
			return false;
		if (getPathfinder().isVehicle()) {
			return false;
		} else {
			if (!this.forceTrigger) {
				if (this.checkNoActionTime && getPathfinder().getNoActionTime() >= 100) {
					return false;
				}

				if (getPathfinder().getRandom().nextInt(reducedTickDelay(this.interval)) != 0) {
					return false;
				}
			}

			Vec3 vec3 = this.getPosition();
			if (vec3 == null) {
				return false;
			} 
			else if (vec3.y < mob.asMob().level.getMinBuildHeight() - 1)	// Too far from anchor
				return false;
			else {
				this.wantedX = vec3.x;
				this.wantedY = vec3.y;
				this.wantedZ = vec3.z;
				this.forceTrigger = false;
				return true;
			}
		}
	}

	@Nullable
	protected Vec3 getPosition() {
		Vec3 v = DefaultRandomPos.getPos(getPathfinder(), 10, 7);
		if (v == null)
			return null;
		// Don't move too far away
		int i = 0;
		for (i = 0; i < 32; ++i)
		{
			if (v != null && !mob.isTooFarFromAnchor(v))
				break;
			v = DefaultRandomPos.getPos(getPathfinder(), 10, 7);
		}
		if (i >= 32)
			return new Vec3(0, mob.asMob().level.getMinBuildHeight() - 100, 0);	// Label failed
		else return v;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean canContinueToUse() {
		return !getPathfinder().getNavigation().isDone() && !getPathfinder().isVehicle();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		super.start();
		BlockPos wanted = new BlockPos(wantedX, wantedY, wantedZ);
		if (shouldAvoidSun && mob.asMob().level.canSeeSky(wanted) && mob.asMob().level.isDay())
			return;
		getPathfinder().getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void stop() {
		getPathfinder().getNavigation().stop();
		super.stop();
	}

	/**
	 * Makes task to bypass chance
	 */
	public void trigger() {
		this.forceTrigger = true;
	}

	/**
	 * Changes task random possibility for execution
	 */
	public void setInterval(int pNewchance) {
		this.interval = pNewchance;
	}

}
