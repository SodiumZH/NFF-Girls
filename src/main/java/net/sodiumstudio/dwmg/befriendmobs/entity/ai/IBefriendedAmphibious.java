package net.sodiumstudio.dwmg.befriendmobs.entity.ai;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;

public interface IBefriendedAmphibious
{
	public WaterBoundPathNavigation getWaterNav();
	public GroundPathNavigation getGroundNav();
	public PathNavigation getAppliedNav();
	public void switchNav(boolean isWaterNav);
	
	public default IBefriendedMob asBef()
	{
		return (IBefriendedMob)this;
	}
	public default PathfinderMob asPathfinder()
	{
		return (PathfinderMob)this;
	}
}
