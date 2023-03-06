package com.sodium.dwmg.befriendmobsapi.entitiy.ai.goal.vanilla;

import java.util.EnumSet;

import javax.annotation.Nullable;

import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.entitiy.ai.goal.BefriendedGoal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.phys.Vec3;

public class BefriendedRandomStrollGoal extends BefriendedGoal {

	public static final int DEFAULT_INTERVAL = 120;
	protected double wantedX;
	protected double wantedY;
	protected double wantedZ;
	protected final double speedModifier;
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
	      mob = pMob;
	      this.speedModifier = pSpeedModifier;
	      this.interval = pInterval;
	      this.checkNoActionTime = pCheckNoActionTime;
	      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	      allowedStates.add(WANDER);
	   }

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
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
			} else {
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
		return DefaultRandomPos.getPos(getPathfinder(), 10, 7);
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		return !getPathfinder().getNavigation().isDone() && !getPathfinder().isVehicle();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		getPathfinder().getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
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
