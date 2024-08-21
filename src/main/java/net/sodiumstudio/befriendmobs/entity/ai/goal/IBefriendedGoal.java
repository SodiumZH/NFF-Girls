package net.sodiumstudio.befriendmobs.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.Goal;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.nautils.annotation.DontOverride;

public interface IBefriendedGoal
{
	public boolean isStateAllowed();
	public IBefriendedGoal allowState(BefriendedAIState state);
	public IBefriendedGoal excludeState(BefriendedAIState state);
	public IBefriendedGoal allowAllStates();
	public IBefriendedGoal allowAllStatesExceptWait();
	public void disallowAllStates();
	public IBefriendedGoal block();	
	public IBefriendedGoal unblock();	
	public IBefriendedMob getMob();
	public boolean isDisabled();
	/**
	 * @return this cast to Goal.
	 */
	@DontOverride
	public default Goal asGoal()
	{
		return (Goal)this;
	}
	
	/**
	 * The alternate of {@code canUse} method for befriend goals.
	 * Override this for {@code canUse} check instead in subclasses. 
	 */
	public boolean checkCanUse();
		
	/**
	 * The alternate of {@code canContinueToUse} method for befriend goals.
	 * Override this for {@code canContinueToUse} check instead in subclasses. 
	 */
	public default boolean checkCanContinueToUse()
	{
		return this.checkCanUse();
	}
	
	/**
	 * The alternate of {@code start} method for befriend goals.
	 * Override this for {@code start} check instead in subclasses. 
	 */
	public default void onStart()
	{
		return;
	}
	
	/**
	 * The alternate of {@code tick} method for befriend goals.
	 * Override this for {@code tick} check instead in subclasses. 
	 */
	public default void onTick()
	{
		return;
	}
	
	/**
	 * The alternate of {@code stop} method for befriend goals.
	 * Override this for {@code stop} check instead in subclasses. 
	 */
	public default void onStop()
	{
		return;
	}
}
