package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla;

import java.util.EnumSet;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;

/* Adjusted from vanilla FollowOwnerGoal for TameableAnimal */
public class BefriendedFollowOwnerGoal extends BefriendedGoal {

	public static final int TELEPORT_WHEN_DISTANCE_IS = 12;
	protected static final int MIN_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 2;
	protected static final int MAX_HORIZONTAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 3;
	protected static final int MAX_VERTICAL_DISTANCE_FROM_PLAYER_WHEN_TELEPORTING = 1;
	protected final LevelReader level;
	protected final double speedModifier;
	protected final PathNavigation navigation;
	protected int timeToRecalcPath;
	protected final float stopDistance;
	protected final float startDistance;
	protected float oldWaterCost;
	protected final boolean canFly;

	public BefriendedFollowOwnerGoal(@Nonnull IBefriendedMob inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly) {
		mob = inMob;
		this.level = mob.asMob().level;
		this.speedModifier = pSpeedModifier;
		this.navigation = getPathfinder().getNavigation();
		this.startDistance = pStartDistance;
		this.stopDistance = pStopDistance;
		this.canFly = pCanFly;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));

		allowState(FOLLOW);
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
	public boolean canUse() {
		if (isDisabled())
			return false;
		if (!(getPathfinder().getNavigation() instanceof GroundPathNavigation)
				&& !(getPathfinder().getNavigation() instanceof FlyingPathNavigation))
			return false;
		LivingEntity livingentity = mob.getOwner();
		if (livingentity == null)
			return false;
		if (livingentity.isSpectator()) {
			return false;
		}  else if (mob.asMob().distanceToSqr(livingentity) < (double) (this.startDistance * this.startDistance)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	public boolean canContinueToUse() {
		if (this.navigation.isDone()) {
			return false;
		} else {
			return !(mob.asMob().distanceToSqr(mob.getOwner()) <= (double) (this.stopDistance * this.stopDistance));
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		this.timeToRecalcPath = 0;
		this.oldWaterCost = getPathfinder().getPathfindingMalus(BlockPathTypes.WATER);
		getPathfinder().setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void stop() {
		this.navigation.stop();
		getPathfinder().setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	public void tick() {
		getPathfinder().getLookControl().setLookAt(mob.getOwner(), 10.0F, (float) getPathfinder().getMaxHeadXRot());
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			if (!getPathfinder().isLeashed() && !getPathfinder().isPassenger()) {
				if (getPathfinder().distanceToSqr(mob.getOwner()) >= 144.0D) {
					this.teleportToOwner();
				} else {
					this.navigation.moveTo(mob.getOwner(), this.speedModifier);
				}

			}
		}
	}

	protected void teleportToOwner() {
		BlockPos blockpos = mob.getOwner().blockPosition();

		for (int i = 0; i < 10; ++i) {
			int j = this.randomIntInclusive(-3, 3);
			int k = this.randomIntInclusive(-1, 1);
			int l = this.randomIntInclusive(-3, 3);
			boolean flag = this.maybeTeleportTo(blockpos.getX() + j, blockpos.getY() + k, blockpos.getZ() + l);
			if (flag) {
				return;
			}
		}

	}

	protected boolean maybeTeleportTo(int pX, int pY, int pZ) {
		if (Math.abs((double) pX - mob.getOwner().getX()) < 2.0D
				&& Math.abs((double) pZ - mob.getOwner().getZ()) < 2.0D) {
			return false;
		} else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) {
			return false;
		} else {
			getPathfinder().moveTo((double) pX + 0.5D, (double) pY, (double) pZ + 0.5D, getPathfinder().getYRot(),
					getPathfinder().getXRot());
			this.navigation.stop();
			return true;
		}
	}

	protected boolean canTeleportTo(BlockPos pPos) {
		BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, pPos.mutable());
		if (blockpathtypes != BlockPathTypes.WALKABLE) {
			return false;
		} else {
			BlockState blockstate = this.level.getBlockState(pPos.below());
			if (!this.canFly && blockstate.getBlock() instanceof LeavesBlock) {
				return false;
			} else {
				BlockPos blockpos = pPos.subtract(getPathfinder().blockPosition());
				return this.level.noCollision(getPathfinder(), getPathfinder().getBoundingBox().move(blockpos));
			}
		}
	}

	protected int randomIntInclusive(int pMin, int pMax) {
		return getPathfinder().getRandom().nextInt(pMax - pMin + 1) + pMin;
	}
}
