package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset;

import java.util.EnumSet;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.IBefriendedAmphibious;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;

public abstract class BefriendedAmphibiousGoals {

	public static class GoToWaterGoal extends BefriendedGoal {
		private double wantedX;
		private double wantedY;
		private double wantedZ;
		private final double speedModifier;
		private final Level level;

		public GoToWaterGoal(IBefriendedMob pMob, double pSpeedModifier) {
			super(pMob);
			this.speedModifier = pSpeedModifier;
			this.level = pMob.asMob().level;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
			allowAllStates();
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		@Override
		public boolean canUse() {
			if (isDisabled())
				return false;
			if (!this.level.isDay())
				return false;
			else if (this.mob.asMob().isInWater()) 
				return false;
			else
			{
				Vec3 vec3 = this.getWaterPos();
				if (vec3 == null) {
					return false;
				} else {
					this.wantedX = vec3.x;
					this.wantedY = vec3.y;
					this.wantedZ = vec3.z;
					return true;
				}
			}
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean canContinueToUse() {
			return !this.mob.asMob().getNavigation().isDone();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void start() {
			this.mob.asMob().getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
		}

		@Nullable
		private Vec3 getWaterPos() {
			Random random = this.mob.asMob().getRandom();
			BlockPos blockpos = this.mob.asMob().blockPosition();

			for (int i = 0; i < 10; ++i) {
				BlockPos blockpos1 = blockpos.offset(random.nextInt(20) - 10, 2 - random.nextInt(8),
						random.nextInt(20) - 10);
				if (this.level.getBlockState(blockpos1).is(Blocks.WATER)) {
					return Vec3.atBottomCenterOf(blockpos1);
				}
			}
			return null;
		}
	}

	public static class GoToBeachGoal extends MoveToBlockGoal {
		protected final IBefriendedAmphibious amph;
		protected final PathfinderMob pathfinder;

		public GoToBeachGoal(IBefriendedMob mob, double pSpeedModifier) {
			super((PathfinderMob) mob, pSpeedModifier, 8, 2);
			this.amph = (IBefriendedAmphibious) mob;
			this.pathfinder = (PathfinderMob) mob;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		@Override
		public boolean canUse() {
			return super.canUse() 
					&& !((IBefriendedMob) this.mob).asMob().level.isDay()
					&& pathfinder.isInWaterOrBubble()
					&& pathfinder.getY() >= (double) (pathfinder.level.getSeaLevel() - 3);
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean canContinueToUse() {
			return super.canContinueToUse();
		}

		/**
		 * Return true to set given position as destination
		 */
		@Override
		protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
			BlockPos blockpos = pPos.above();
			return pLevel.isEmptyBlock(blockpos) && pLevel.isEmptyBlock(blockpos.above())
					? pLevel.getBlockState(pPos).entityCanStandOn(pLevel, pPos, pathfinder)
					: false;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void start() {

			amph.switchNav(false);

			super.start();
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by
		 * another one
		 */
		@Override
		public void stop() {
			super.stop();
		}
	}

	public static class SwimUpGoal extends BefriendedGoal {
		protected final IBefriendedAmphibious amph;
		protected final double speedModifier;
		protected final int seaLevel;
		protected boolean stuck;

		public SwimUpGoal(IBefriendedMob mob, double speedModifier, int seaLevel) {
			super(mob);
			this.amph = (IBefriendedAmphibious)mob;
			this.speedModifier = speedModifier;
			this.seaLevel = seaLevel;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		@Override
		public boolean canUse() {
			return !this.isDisabled()
					&& this.getPathfinder().isInWater();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean canContinueToUse() {
			return this.canUse() && !this.stuck;
		}

		@Override
		public void tick() {

			if (this.getPathfinder().getY() < (double) (this.seaLevel - 1)
					&& (this.getPathfinder().getNavigation().isDone() || closeToNextPos(4.0d))) {
				Vec3 vec3 = DefaultRandomPos.getPosTowards(this.getPathfinder(), 4, 8,
						new Vec3(this.getPathfinder().getX(), (double) (this.seaLevel - 1), this.getPathfinder().getZ()),
						(double) ((float) Math.PI / 2F));
				if (vec3 == null) {
					this.stuck = true;
					return;
				}

				this.getPathfinder().getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
			}

		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void start() {
			this.stuck = false;
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by
		 * another one
		 */
		@Override
		public void stop() {			
		}

		 protected boolean closeToNextPos(double threshold) {
		      Path path = this.getPathfinder().getNavigation().getPath();
		      if (path != null) {
		         BlockPos blockpos = path.getTarget();
		         if (blockpos != null) {
		            double d0 = this.getPathfinder().distanceToSqr((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());
		            if (d0 < threshold) {
		               return true;
		            }
		         }
		      }
		      return false;
		   }
		 
	}

}
