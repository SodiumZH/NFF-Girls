package net.sodiumstudio.befriendmobs.bmevents.entity.ai;

import java.util.Optional;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedTargetGoal;

public class BefriendedGoalCheckCanUseEvent extends Event
{
	/**
	 * Either {@link BefriendedGoal} or {@link BefriendedTargetGoal}. Try cast to determine which it is.
	 */
	public final Goal goal;
	/**
	 * Where this event is fired, either {@code canUse} or {@code canContinueToUse}. 
	 */
	public final Phase phase;
	protected boolean isManualSet = false;
	protected boolean setValue = false;
	
	public BefriendedGoalCheckCanUseEvent(Goal goal, Phase phase)
	{
		this.goal = goal;
		this.phase = phase;
	}
		
	/**
	 * Manually set the canUse/canContinueToUse result.
	 * This set value will override all other checks including BefriendedGoal/BefriendedTargetGoal common checks.
	 */
	public void manualSetValue(boolean value)
	{
		isManualSet = true;
		setValue = value;
	}
	
	/**
	 * @return Manual-set value, or empty if not manually set.
	 */
	public Optional<Boolean> getManualSetValue()
	{
		if (isManualSet)
		{
			return Optional.of(setValue);
		}
		else return Optional.empty();
	}
	
	public static enum Phase
	{
		CAN_USE,
		CAN_CONTINUE_TO_USE
	}
}
