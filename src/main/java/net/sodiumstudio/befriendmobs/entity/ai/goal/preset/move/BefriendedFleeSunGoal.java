package net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move;

import java.util.EnumSet;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedMoveGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedSunSensitiveMob;

// Adjusted from vanilla FleeSunGoal
public class BefriendedFleeSunGoal extends BefriendedMoveGoal {

	protected double wantedX;
	protected double wantedY;
	protected double wantedZ;
	protected final Level level;
	public boolean ignoreHelmet = false;
	
	public BefriendedFleeSunGoal(IBefriendedMob pMob, double pSpeedModifier) {
	      super(pMob, pSpeedModifier);
	      this.level = getPathfinder().level;
	      this.setFlags(EnumSet.of(Goal.Flag.MOVE));
	      allowAllStates();
	   }

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
	@Override
	public boolean checkCanUse() 
	{
		if (!this.level.isDay())
			return false;
		else if (!this.level.canSeeSky(getPathfinder().blockPosition())) 
			return false;
		else if (!ignoreHelmet && !getPathfinder().getItemBySlot(EquipmentSlot.HEAD).isEmpty())
			return false;
		else if (mob instanceof IBefriendedSunSensitiveMob bssm && bssm.isSunImmune())
			return false;
		else if	(!this.setWantedPos())
			return false;
		else if (mob.asMob().isInWater())
			return false;
		else return true;
	}

	protected boolean setWantedPos() {
		Vec3 vec3 = this.getHidePos();
		if (vec3 == null) {
			return false;
		} else {
			this.wantedX = vec3.x;
			this.wantedY = vec3.y;
			this.wantedZ = vec3.z;
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean checkCanContinueToUse() {
		return !getPathfinder().getNavigation().isDone();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void onStart() {
		super.onStart();
		getPathfinder().getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
	}

	@Nullable
	protected Vec3 getHidePos() {
		RandomSource random = getPathfinder().getRandom();
		BlockPos blockpos = getPathfinder().blockPosition();

		for (int i = 0; i < 10; ++i) {
			BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3,
					random.nextInt(20) - 10);
			if (!this.level.canSeeSky(blockpos1) && getPathfinder().getWalkTargetValue(blockpos1) < 0.0F) {
				return Vec3.atBottomCenterOf(blockpos1);
			}
		}

		return null;
	}

}
