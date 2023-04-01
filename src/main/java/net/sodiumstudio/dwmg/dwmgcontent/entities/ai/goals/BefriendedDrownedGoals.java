package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.EnumSet;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedRangedAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedZombieAttackGoal;

public abstract class BefriendedDrownedGoals {

	public static class GoToWaterGoal extends BefriendedGoal {
		private double wantedX;
		private double wantedY;
		private double wantedZ;
		private final double speedModifier;
		private final Level level;

		public GoToWaterGoal(IBefriendedMob pMob, double pSpeedModifier) {
			this.mob = pMob;
			this.speedModifier = pSpeedModifier;
			this.level = pMob.asMob().level;
			this.setFlags(EnumSet.of(Goal.Flag.MOVE));
			allowAllStates();
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean canUse() {
			if (isDisabled())
				return false;
			if (!this.level.isDay()) {
				return false;
			} else if (this.mob.asMob().isInWater()) {
				return false;
			} else {
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
		public boolean canContinueToUse() {
			return !this.mob.asMob().getNavigation().isDone();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			this.mob.asMob().getNavigation().moveTo(this.wantedX, this.wantedY, this.wantedZ, this.speedModifier);
		}

		@Nullable
		private Vec3 getWaterPos() {
			RandomSource random = this.mob.asMob().getRandom();
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

	public static class TridentAttackGoal extends BefriendedRangedAttackGoal {

		public TridentAttackGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
			super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean canUse() {
			return super.canUse() && this.mob.asMob().getMainHandItem().is(Items.TRIDENT);
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			super.start();
			this.mob.asMob().setAggressive(true);
			this.mob.asMob().startUsingItem(InteractionHand.MAIN_HAND);
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by
		 * another one
		 */
		public void stop() {
			super.stop();
			this.mob.asMob().stopUsingItem();
			this.mob.asMob().setAggressive(false);
		}
	}

	public static class AttackGoal extends BefriendedZombieAttackGoal {

		public AttackGoal(IBefriendedMob mob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
			super(mob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean canUse() {
			return super.canUse() && this.okTarget(this.mob.asMob().getTarget());
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean canContinueToUse() {
			return super.canContinueToUse() && this.okTarget(this.mob.asMob().getTarget());
		}

		// Ported from vanilla drowned class
		public boolean okTarget(@Nullable LivingEntity target) {
			if (target != null) {
				return !mob.asMob().level.isDay() || target.isInWater();
			} else {
				return false;
			}
		}
	}

	public static class GoToBeachGoal extends MoveToBlockGoal {
		private final Drowned drowned;

		public GoToBeachGoal(IBefriendedMob mob, double pSpeedModifier) {
			super((Drowned) mob, pSpeedModifier, 8, 2);
			this.drowned = (Drowned) mob;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean canUse() {
			return super.canUse() && !this.drowned.level.isDay() && this.drowned.isInWaterOrBubble()
					&& this.drowned.getY() >= (double) (this.drowned.level.getSeaLevel() - 3);
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean canContinueToUse() {
			return super.canContinueToUse();
		}

		/**
		 * Return true to set given position as destination
		 */
		protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
			BlockPos blockpos = pPos.above();
			return pLevel.isEmptyBlock(blockpos) && pLevel.isEmptyBlock(blockpos.above())
					? pLevel.getBlockState(pPos).entityCanStandOn(pLevel, pPos, this.drowned)
					: false;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {

			// Force set drowned navigation to ground navigation
			try {
				Field navFld = drowned.getClass().getField("navigation");
				Field gnavFld = drowned.getClass().getField("groundNavigation");
				navFld.setAccessible(true);
				gnavFld.setAccessible(true);
				navFld.set(drowned, gnavFld.get(drowned));
				navFld.setAccessible(false);
				gnavFld.setAccessible(false);
			} catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			super.start();
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by
		 * another one
		 */
		public void stop() {
			super.stop();
		}
	}

	public static class SwimUpGoal extends BefriendedGoal {
		private final Drowned drowned;
		private final double speedModifier;
		private final int seaLevel;
		private boolean stuck;

		public SwimUpGoal(IBefriendedMob mob, double pSpeedModifier, int pSeaLevel) {
			if (!(mob instanceof Drowned))
				throw new UnsupportedOperationException();
			this.drowned = (Drowned)mob;
			this.speedModifier = pSpeedModifier;
			this.seaLevel = pSeaLevel;
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		public boolean canUse() {
			return this.drowned.isInWater()
					&& this.drowned.getY() < (double) (this.seaLevel - 2);
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean canContinueToUse() {
			return this.canUse() && !this.stuck;
		}

		public void tick() {

			boolean closeToNextPosRes = false;
			
			// Force execute Drowned.closeToNextPosRes()
			try {
				Method closeToNextPosMtd = this.drowned.getClass().getDeclaredMethod("closeToNextPos");
				closeToNextPosMtd.setAccessible(true);
				closeToNextPosRes = (boolean) closeToNextPosMtd.invoke(drowned);
				closeToNextPosMtd.setAccessible(false);
			} 
			catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}

			if (this.drowned.getY() < (double) (this.seaLevel - 1)
					&& (this.drowned.getNavigation().isDone() || closeToNextPosRes)) {
				Vec3 vec3 = DefaultRandomPos.getPosTowards(this.drowned, 4, 8,
						new Vec3(this.drowned.getX(), (double) (this.seaLevel - 1), this.drowned.getZ()),
						(double) ((float) Math.PI / 2F));
				if (vec3 == null) {
					this.stuck = true;
					return;
				}

				this.drowned.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
			}

		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void start() {
			this.stuck = false;
		}

		/**
		 * Reset the task's internal state. Called when this task is interrupted by
		 * another one
		 */
		public void stop() {			
		}
	}

}
