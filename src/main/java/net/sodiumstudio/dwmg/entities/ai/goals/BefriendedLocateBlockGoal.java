package net.sodiumstudio.dwmg.entities.ai.goals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NaParticleUtils;
import net.sodiumstudio.nautils.Wrapped;

/**
 * Goal for locating certain type of blocks nearby
 * Used together with IBlockLocator
 */
public class BefriendedLocateBlockGoal extends BefriendedGoal
{
	protected final Random rnd = new Random();
	protected IBlockLocator loc;
	protected double searchRange = 6;
	protected boolean sphericalSearchRange = false;
	@Nullable
	protected BlockPos targetPos = null;
	protected int maxDuration = 10 * 20;
	protected int cooldown = 30 * 20;
	protected int endTimestamp = -1;
	protected int restartTimestamp = -1;
	
	public BefriendedLocateBlockGoal(IBefriendedMob mob)
	{
		super(mob);
		if (mob instanceof IBlockLocator bl)
			loc = bl;
		else throw new UnsupportedOperationException("BefriendedLocateBlockGoal requires mobs with IBlockLocator interface.");
		this.allowState(FOLLOW);
		this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
	}

	public BefriendedLocateBlockGoal(IBefriendedMob mob, double searchRange)
	{
		this(mob);
		this.searchRange = searchRange;
	}
	
	public BefriendedLocateBlockGoal sphericalSearch()
	{
		sphericalSearchRange = true;
		return this;
	}
	
	@Override
	public boolean checkCanUse() {
		// Frequency check
		if (rnd.nextInt(loc.getFrequency()) == 0)
			return false;
		// Cooldown check
		if (mob.asMob().tickCount > endTimestamp && mob.asMob().tickCount <= restartTimestamp)
			return false;
		// Can locate check
		Collection<Block> blocks = loc.getLocatingBlocks();
		if (blocks.size() == 0)
			return false;
		// Existance check
		AABB range = EntityHelper.getNeighboringArea(mob.asMob(), searchRange);
		Wrapped<ArrayList<BlockPos>> acceptedPosWrapper = new Wrapped<>(new ArrayList<BlockPos>(50));		
		BlockPos.betweenClosedStream(range).forEach((BlockPos pos) -> {
			if (loc.getLocatingBlocks().contains(mob.asMob().level.getBlockState(pos).getBlock()))
				acceptedPosWrapper.get().add(new BlockPos(pos.getX(), pos.getY(), pos.getZ()));
			});
		List<BlockPos> acceptedPos = acceptedPosWrapper.get().stream()
			.filter((BlockPos bp) -> blocks.contains(mob.asMob().level.getBlockState(bp).getBlock()))
			.filter((BlockPos bp) -> !sphericalSearchRange || bp.distSqr(mob.asMob().blockPosition()) <= (searchRange * searchRange))
			.sorted(Comparator.comparingDouble((BlockPos bp) -> bp.distSqr(mob.asMob().blockPosition())))
			.toList();
		if (acceptedPos.size() == 0)
			return false;
		else
		{
			targetPos = acceptedPos.get(0);
			return true;
		}
	}
	
	@Override
	public boolean checkCanContinueToUse()
	{
		return mob.asMob().tickCount <= endTimestamp;
	}
			
	
	@Override
	public void onStart()
	{
		NaParticleUtils.sendGlintParticlesToEntityDefault(mob.asMob());
		endTimestamp = mob.asMob().tickCount + maxDuration;
		restartTimestamp = endTimestamp + cooldown;
		loc.onStartLocating();
	}
	
	@Override
	public void onTick()
	{
		if (targetPos != null && loc.getLocatingBlocks().contains(mob.asMob().level.getBlockState(targetPos).getBlock()))
		{
			mob.asMob().getNavigation().moveTo(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1);
			mob.asMob().getMoveControl().setWantedPosition(targetPos.getX(), targetPos.getY(), targetPos.getZ(), 1);
			mob.asMob().getLookControl().setLookAt(targetPos.getX(), targetPos.getY(), targetPos.getZ());
			if (mob.asMob().tickCount % 5 == 0 && mob.asMob().distanceToSqr(targetPos.getX(), targetPos.getY(), targetPos.getZ()) < 6.25d)
			{
				NaParticleUtils.sendGlintParticlesToEntityDefault(mob.asMob());
			}
		}
	}
	
	@Override
	public void onStop()
	{
		targetPos = null;
		mob.asMob().getNavigation().stop();
		mob.asMob().getMoveControl().setWantedPosition(mob.asMob().position().x, mob.asMob().position().y, mob.asMob().position().z, 1);
	}

}
