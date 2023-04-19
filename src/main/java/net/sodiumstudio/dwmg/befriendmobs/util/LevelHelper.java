package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class LevelHelper
{
	
	// Check if a block position is right under sun, i.e. can see sky, not raining, and is day
	public static boolean isUnderSun(BlockPos pos, Entity levelContext)
	{
		return levelContext.level.canSeeSky(pos) && levelContext.level.isDay() && !levelContext.level.isRaining();
	}
	
	public static boolean isEntityUnderSun(Entity test)
	{
		return isUnderSun(new BlockPos(test.position()), test);
	}
	
	public static boolean isAboveWater(BlockPos pos, Entity levelContext)
	{
		Level level = levelContext.level;
		for (int y = pos.getY(); y > -70; --y)
		{
			BlockPos currentPos = new BlockPos(pos.getX(), y, pos.getZ());
			if (level.getBlockState(currentPos).is(Blocks.WATER))
				return true;
			else if (!level.getBlockState(currentPos).isAir())
				return false;
		}
		return false;
	}
	
	public static boolean isEntityAboveWater(Entity test)
	{
		return isAboveWater(new BlockPos(test.position()), test);
	}
	
	public static boolean isAboveVoid(BlockPos pos, Entity levelContext)
	{
		Level level = levelContext.level;
		for (int y = pos.getY(); y > -70; --y)
		{
			BlockPos currentPos = new BlockPos(pos.getX(), y, pos.getZ());
			if (!level.getBlockState(currentPos).isAir())
				return false;
		}
		return true;
	}
	
	public static boolean isEntityAboveVoid(Entity test)
	{
		return isAboveVoid(new BlockPos(test.position()), test);
	}
	
	/** Get the height of a position to the standable block or liquid below.
	 * if the position is above the void, return -1.
	* @param context Entity for getting level and checking if can stand on.
	*/
	public static int getHeightToGround(BlockPos pos, Entity context)
	{
		Level level = context.level;
		BlockPos pos1 = new BlockPos(pos);
		if (level.getBlockState(pos1).entityCanStandOn(level, pos1, context)
				|| level.getBlockState(pos1).getMaterial().isLiquid())
			return 0;
		else 
		{
			int i = 0;
			while (!level.getBlockState(pos1).entityCanStandOn(level, pos1, context)
				&& !level.getBlockState(pos1).getMaterial().isLiquid())
			{
				i++;
				pos1 = pos1.below();
				if (pos1.getY() < level.getMinBuildHeight() - 1)
					return -1;
			}
			return i;
		}
	}
	
	/** Get the height of a position to the standable block or liquid below. (Vec3 version)
	 * if the position is above the void, return -1.
	* @param context Entity for getting level and checking if can stand on.
	*/
	public static int getHeightToGround(Vec3 v, Entity context)
	{
		return getHeightToGround(new BlockPos(v), context);
	}
}
