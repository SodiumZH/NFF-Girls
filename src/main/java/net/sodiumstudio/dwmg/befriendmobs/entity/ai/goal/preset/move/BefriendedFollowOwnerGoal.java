package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import java.util.EnumSet;

import javax.annotation.Nonnull;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.IBefriendedAmphibious;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedMoveGoal;
import net.sodiumstudio.dwmg.befriendmobs.util.LevelHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.exceptions.MissingInterfaceException;

/* Adjusted from vanilla FollowOwnerGoal for TameableAnimal */
public class BefriendedFollowOwnerGoal extends BefriendedMoveGoal {

	protected final LevelReader level;
	protected int timeToRecalcPath;
	public final float stopDistance;
	public final float startDistance;
	protected float oldWaterCost;
	public float teleportDistance = 12f;


	public BefriendedFollowOwnerGoal(@Nonnull IBefriendedMob inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly) {
		super(inMob, pSpeedModifier);
		this.level = mob.asMob().level;
		this.startDistance = pStartDistance;
		this.stopDistance = pStopDistance;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
		allowState(FOLLOW);
	}

	@Override
	public boolean canUse() {
 		if (isDisabled())
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

	@Override
	public boolean canContinueToUse() {
		if (this.getPathfinder().getNavigation().isDone()) {
			return false;
		} else {
 			return mob.asMob().distanceToSqr(mob.getOwner()) > (double) (this.stopDistance * this.stopDistance);
		}
	}

	@Override
	public void start() {
		super.start();
		this.timeToRecalcPath = 0;
		this.oldWaterCost = getPathfinder().getPathfindingMalus(BlockPathTypes.WATER);
		getPathfinder().setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
	}

	@Override
	public void stop() {
		this.getPathfinder().getNavigation().stop();
		getPathfinder().setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void tick() {
		getPathfinder().getLookControl().setLookAt(mob.getOwner(), 10.0F, (float) getPathfinder().getMaxHeadXRot());
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			if (!getPathfinder().isLeashed() && !getPathfinder().isPassenger()) {
				if (getPathfinder().distanceToSqr(mob.getOwner()) >= teleportDistance * teleportDistance && allowTeleport()) 
				{
					this.teleportToOwner();
				} 
				else 
				{
					if (!shouldAvoidSun.test(mob) || !LevelHelper.isEntityUnderSun(mob.getOwner()))
						this.getPathfinder().getNavigation().moveTo(mob.getOwner(), this.speedModifier);
				}
			}
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

	protected boolean tryTeleportTo(int pX, int pY, int pZ) {
		if (Math.abs((double) pX - mob.getOwner().getX()) < 2.0D
				&& Math.abs((double) pZ - mob.getOwner().getZ()) < 2.0D) {
			return false;
		} else if (!this.canTeleportTo(new BlockPos(pX, pY, pZ))) {
			return false;
		} else {
			getPathfinder().moveTo((double) pX + 0.5D, (double) pY, (double) pZ + 0.5D, getPathfinder().getYRot(),
					getPathfinder().getXRot());
			this.getPathfinder().getNavigation().stop();
			return true;
		}
	}

	protected boolean canTeleportTo(BlockPos pos) {
		if (!allowTeleport())
			return false;
		BlockPathTypes blockpathtypes = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, pos.mutable());

		// Onto a standable block
		if (blockpathtypes == BlockPathTypes.WALKABLE)
		{
			BlockState blockstate = this.level.getBlockState(pos.below());
			if (!this.canFly && !this.canStepOntoLeaves && blockstate.getBlock() instanceof LeavesBlock)
			{
				return false;
			} else
			{
				BlockPos blockpos = pos.subtract(getPathfinder().blockPosition());
				return this.level.noCollision(getPathfinder(), getPathfinder().getBoundingBox().move(blockpos));
			}
		}
		// To a water position
		else if (isAmphibious && mob.asMob().level.getBlockState(pos).is(Blocks.WATER))
		{
			BlockPos blockpos = pos.subtract(getPathfinder().blockPosition());
			return this.level.noCollision(getPathfinder(), getPathfinder().getBoundingBox().move(blockpos));
		} 
		else return false;
	}

	protected boolean allowTeleport()
	{
		return true;
	}
	
	protected int randomIntInclusive(int pMin, int pMax) {
		return getPathfinder().getRandom().nextInt(pMax - pMin + 1) + pMin;
	}
}
