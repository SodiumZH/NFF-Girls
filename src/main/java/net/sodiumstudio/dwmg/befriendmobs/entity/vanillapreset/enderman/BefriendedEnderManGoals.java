package net.sodiumstudio.dwmg.befriendmobs.entity.vanillapreset.enderman;

import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;

public class BefriendedEnderManGoals
{

	public static class LeaveBlockGoal extends BefriendedGoal
	{
		protected final AbstractBefriendedEnderMan enderman;

		public LeaveBlockGoal(AbstractBefriendedEnderMan enderman)
		{
			super(enderman);
			this.mob = enderman.self();
			this.enderman = enderman;
			this.allowState(WANDER);
		}

		/**
		 * Returns whether execution should begin. You can also read and cache any state
		 * necessary for execution in this method as well.
		 */
		@Override
		public boolean canUse() {
			if (isDisabled())
				return false;
			if (!enderman.canAutoPlaceBlocks)
				return false;
			if (this.enderman.getCarriedBlock() == null)
			{
				return false;
			} else if (!net.minecraftforge.event.ForgeEventFactory
					.getMobGriefingEvent(this.enderman.level, this.enderman))
			{
				return false;
			} else
			{
				return this.enderman.getRandom().nextInt(reducedTickDelay(2000)) == 0;
			}
		}

		/**
		 * Keep ticking a continuous task that has already been started
		 */
		@Override
		public void tick() {
			RandomSource random = this.enderman.getRandom();
			Level level = this.enderman.level;
			int i = Mth.floor(this.enderman.getX() - 1.0D + random.nextDouble() * 2.0D);
			int j = Mth.floor(this.enderman.getY() + random.nextDouble() * 2.0D);
			int k = Mth.floor(this.enderman.getZ() - 1.0D + random.nextDouble() * 2.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState blockstate = level.getBlockState(blockpos);
			BlockPos blockpos1 = blockpos.below();
			BlockState blockstate1 = level.getBlockState(blockpos1);
			BlockState blockstate2 = this.enderman.getCarriedBlock();
			if (blockstate2 != null)
			{
				blockstate2 = Block.updateFromNeighbourShapes(blockstate2, this.enderman.level,
						blockpos);
				if (this.canPlaceBlock(level, blockpos, blockstate2, blockstate, blockstate1, blockpos1)
						&& !net.minecraftforge.event.ForgeEventFactory.onBlockPlace(enderman,
								net.minecraftforge.common.util.BlockSnapshot.create(level.dimension(), level,
										blockpos1),
								net.minecraft.core.Direction.UP))
				{
					level.setBlock(blockpos, blockstate2, 3);
					level.gameEvent(this.enderman, GameEvent.BLOCK_PLACE, blockpos);
					this.enderman.setCarriedBlock((BlockState) null);
				}

			}
		}

		protected boolean canPlaceBlock(Level pLevel, BlockPos pDestinationPos, BlockState pCarriedState,
				BlockState pDestinationState, BlockState pBelowDestinationState, BlockPos pBelowDestinationPos) {
			return pDestinationState.isAir() && !pBelowDestinationState.isAir()
					&& !pBelowDestinationState.is(Blocks.BEDROCK)
					&& !pBelowDestinationState.is(net.minecraftforge.common.Tags.Blocks.ENDERMAN_PLACE_ON_BLACKLIST)
					&& pBelowDestinationState.isCollisionShapeFullBlock(pLevel, pBelowDestinationPos)
					&& pCarriedState.canSurvive(pLevel, pDestinationPos)
					&& pLevel.getEntities(this.enderman,
							AABB.unitCubeFromLowerCorner(Vec3.atLowerCornerOf(pDestinationPos))).isEmpty();
		}
	}
/*
	public static class LookForPlayerGoal<T extends LivingEntity> 
		extends NearestAttackableTargetGoal<T>
	{
		@Nullable
		protected T pendingTarget;
		protected int aggroTime;
		protected int teleportTime;
		protected final TargetingConditions startAggroTargetConditions;
		protected final TargetingConditions continueAggroTargetConditions = TargetingConditions.forCombat()
				.ignoreLineOfSight();

		public LookForPlayerGoal(AbstractBefriendedEnderMan enderman, Class<T> type,
				@Nullable Predicate<LivingEntity> pSelectionPredicate)
		{
			super(enderman, type, 10, false, false, pSelectionPredicate);
			this.enderman = enderman;
			this.startAggroTargetConditions = TargetingConditions.forCombat().range(this.getFollowDistance())
					.selector((p_32578_) ->
					{
						return enderman.isLookingAtMe((Player) p_32578_);
					});
		}


		public boolean canUse() {
			this.pendingTarget = this.enderman.level.getNearestPlayer(this.startAggroTargetConditions,
					this.enderman);
			return this.pendingTarget != null;
		}

		public void start() {
			this.aggroTime = this.adjustedTickDelay(5);
			this.teleportTime = 0;
			this.enderman.setBeingStaredAt();
		}

		public void stop() {
			this.pendingTarget = null;
			super.stop();
		}

		public boolean canContinueToUse() {
			if (this.pendingTarget != null)
			{
				if (!this.enderman.isLookingAtMe(this.pendingTarget))
				{
					return false;
				} else
				{
					this.enderman.lookAt(this.pendingTarget, 10.0F, 10.0F);
					return true;
				}
			} else
			{
				return this.target != null
						&& this.continueAggroTargetConditions.test(this.enderman, this.target) ? true
								: super.canContinueToUse();
			}
		}

		public void tick() {
			if (this.enderman.getTarget() == null)
			{
				super.setTarget((LivingEntity) null);
			}

			if (this.pendingTarget != null)
			{
				if (--this.aggroTime <= 0)
				{
					this.target = this.pendingTarget;
					this.pendingTarget = null;
					super.start();
				}
			} else
			{
				if (this.target != null && !this.enderman.isPassenger())
				{
					if (this.enderman.isLookingAtMe((Player) this.target))
					{
						if (this.target.distanceToSqr(this.enderman) < 16.0D)
						{
							this.enderman.teleport();
						}

						this.teleportTime = 0;
					} else if (this.target.distanceToSqr(this.enderman) > 256.0D
							&& this.teleportTime++ >= this.adjustedTickDelay(30)
							&& this.enderman.teleportTowards(this.target))
					{
						this.teleportTime = 0;
					}
				}

				super.tick();
			}

		}
	}
*/
	public static class TakeBlockGoal extends BefriendedGoal
	{
		protected final AbstractBefriendedEnderMan enderman;

		public TakeBlockGoal(AbstractBefriendedEnderMan enderman)
		{
			super(enderman);
			this.mob = (IBefriendedMob)enderman;
			this.enderman = enderman;
			this.allowState(WANDER);
		}

		@Override
		public boolean canUse() {
			if (isDisabled())
				return false;
			if (!enderman.canAutoTakeBlocks)
				return false;
			if (this.enderman.getCarriedBlock() != null)
			{
				return false;
			} else if (!net.minecraftforge.event.ForgeEventFactory
					.getMobGriefingEvent(this.enderman.level, this.enderman))
			{
				return false;
			} else
			{
				return this.enderman.getRandom().nextInt(reducedTickDelay(20)) == 0;
			}
		}

		@Override
		public void tick() {
			RandomSource random = this.enderman.getRandom();
			Level level = this.enderman.level;
			int i = Mth.floor(this.enderman.getX() - 2.0D + random.nextDouble() * 4.0D);
			int j = Mth.floor(this.enderman.getY() + random.nextDouble() * 3.0D);
			int k = Mth.floor(this.enderman.getZ() - 2.0D + random.nextDouble() * 4.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			BlockState blockstate = level.getBlockState(blockpos);
			Vec3 vec3 = new Vec3((double) this.enderman.getBlockX() + 0.5D, (double) j + 0.5D,
					(double) this.enderman.getBlockZ() + 0.5D);
			Vec3 vec31 = new Vec3((double) i + 0.5D, (double) j + 0.5D, (double) k + 0.5D);
			BlockHitResult blockhitresult = level.clip(new ClipContext(vec3, vec31, ClipContext.Block.OUTLINE,
					ClipContext.Fluid.NONE, this.enderman));
			boolean flag = blockhitresult.getBlockPos().equals(blockpos);
			if (blockstate.is(BlockTags.ENDERMAN_HOLDABLE) && flag)
			{
				level.removeBlock(blockpos, false);
				level.gameEvent(this.enderman, GameEvent.BLOCK_DESTROY, blockpos);
				this.enderman.setCarriedBlock(blockstate.getBlock().defaultBlockState());
			}

		}
	}
	/*
	public static class FreezeWhenLookedAt extends BefriendedGoal
	{
		private final AbstractBefriendedEnderMan enderman;
		@Nullable
		protected LivingEntity target;

		public FreezeWhenLookedAt(AbstractBefriendedEnderMan enderman)
		{
			this.mob = (IBefriendedMob)enderman;
			this.enderman = enderman;
			this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
			this.allowState(WANDER);
		}

		public boolean canUse() {
			if (isDisabled())
				return false;
			this.target = this.enderman.getTarget();
			if (!(this.target instanceof Player))
			{
				return false;
			}
			if (enderman.getOwnerUUID() != null && enderman.getOwnerUUID().equals(this.target.getUUID()))
				return false;
			double d0 = this.target.distanceToSqr(this.enderman);
			return d0 > 256.0D ? false : this.enderman.isLookingAtMe((Player) this.target);

		}

		public void start() {
			this.enderman.getNavigation().stop();
		}

		public void tick() {
			this.enderman.getLookControl().setLookAt(this.target.getX(), this.target.getEyeY(), this.target.getZ());
		}
	}*/
}
