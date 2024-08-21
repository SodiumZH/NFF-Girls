package net.sodiumstudio.befriendmobs.entity.ai.goal.preset;

import java.util.EnumSet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.LevelReader;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedMoveGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public abstract class BefriendedMoveToBlockGoal extends BefriendedMoveGoal
{
	/*private static final int GIVE_UP_TICKS = 1200;
	private static final int STAY_TICKS = 1200;
	private static final int INTERVAL_TICKS = 200;*/
	protected final PathfinderMob mobPathfinder;
	/** Controls task execution delay */
	protected int nextStartTick;
	protected int tryTicks;
	protected int maxStayTicks;
	/** Block to move to */
	protected BlockPos blockPos = BlockPos.ZERO;
	protected boolean reachedTarget;
	protected final int searchRange;
	protected final int verticalSearchRange;
	protected int verticalSearchStart;

	public BefriendedMoveToBlockGoal(IBefriendedMob pMob, double pSpeedModifier, int pSearchRange)
	{
		this(pMob, pSpeedModifier, pSearchRange, 1);
	}

	public BefriendedMoveToBlockGoal(IBefriendedMob pMob, double pSpeedModifier, int pSearchRange,
			int pVerticalSearchRange)
	{
		super(pMob, pSpeedModifier);
		if (!(pMob instanceof PathfinderMob))
			throw new UnsupportedOperationException("BefriendedMoveToBlockGoal supports only PathfinderMob.");
		this.mobPathfinder = (PathfinderMob) this.mob;
		this.searchRange = pSearchRange;
		this.verticalSearchStart = 0;
		this.verticalSearchRange = pVerticalSearchRange;
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	@Override
	public boolean checkCanUse() {
		if (this.nextStartTick > 0)
		{
			--this.nextStartTick;
			return false;
		} else
		{
			this.nextStartTick = this.nextStartTick((PathfinderMob) this.mob);
			return this.findNearestBlock();
		}
	}

	protected int nextStartTick(PathfinderMob pCreature) {
		return reducedTickDelay(200 + pCreature.getRandom().nextInt(200));
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean checkCanContinueToUse() {
		return this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200
				&& this.isValidTarget(this.mobPathfinder.level, this.blockPos);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void onStart() {
		this.moveMobToBlock();
		this.tryTicks = 0;
		this.maxStayTicks = this.mob.asMob().getRandom().nextInt(this.mob.asMob().getRandom().nextInt(1200) + 1200) + 1200;
	}

	protected void moveMobToBlock() {
		this.mobPathfinder.getNavigation().moveTo((this.blockPos.getX()) + 0.5D,
				this.blockPos.getY() + 1, (this.blockPos.getZ()) + 0.5D,
				this.speedModifier);
	}

	public double acceptedDistance() {
		return 1.0D;
	}

	protected BlockPos getMoveToTarget() {
		return this.blockPos.above();
	}

	@Override
	public boolean requiresUpdateEveryTick() {
		return true;
	}

	/**
	 * Keep ticking a continuous task that has already been started
	 */
	@Override
	public void onTick() {
		BlockPos blockpos = this.getMoveToTarget();
		if (!blockpos.closerToCenterThan(this.mobPathfinder.position(), this.acceptedDistance()))
		{
			this.reachedTarget = false;
			++this.tryTicks;
			if (this.shouldRecalculatePath())
			{
				this.mobPathfinder.getNavigation().moveTo((blockpos.getX()) + 0.5D, blockpos.getY(),
						(blockpos.getZ()) + 0.5D, this.speedModifier);
			}
		} else
		{
			this.reachedTarget = true;
			--this.tryTicks;
		}

	}

	public boolean shouldRecalculatePath() {
		return this.tryTicks % 40 == 0;
	}

	protected boolean isReachedTarget() {
		return this.reachedTarget;
	}

	/**
	 * Searches and sets new destination block and returns true if a suitable block
	 * (specified in
	 * {@link net.minecraft.entity.ai.EntityAIMoveToBlock#shouldMoveTo(World, BlockPos)
	 * EntityAIMoveToBlock#shouldMoveTo(World, BlockPos)}) can be found.
	 */
	protected boolean findNearestBlock() {
		int i = this.searchRange;
		int j = this.verticalSearchRange;
		BlockPos blockpos = this.mobPathfinder.blockPosition();
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

		for (int k = this.verticalSearchStart; k <= j; k = k > 0 ? -k : 1 - k)
		{
			for (int l = 0; l < i; ++l)
			{
				for (int i1 = 0; i1 <= l; i1 = i1 > 0 ? -i1 : 1 - i1)
				{
					for (int j1 = i1 < l && i1 > -l ? l : 0; j1 <= l; j1 = j1 > 0 ? -j1 : 1 - j1)
					{
						blockpos$mutableblockpos.setWithOffset(blockpos, i1, k - 1, j1);
						if (this.mobPathfinder.isWithinRestriction(blockpos$mutableblockpos)
								&& this.isValidTarget(this.mobPathfinder.level, blockpos$mutableblockpos))
						{
							this.blockPos = blockpos$mutableblockpos;
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	/**
	 * Return true to set given position as destination
	 */
	protected abstract boolean isValidTarget(LevelReader pLevel, BlockPos pPos);
}
