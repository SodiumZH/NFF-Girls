package net.sodiumstudio.befriendmobs.entity.capability.wrapper;

import java.util.function.Consumer;

import net.minecraft.world.entity.LivingEntity;
import net.sodiumstudio.befriendmobs.entity.capability.CLivingEntityDelayedActionHandler;
import net.sodiumstudio.befriendmobs.registry.BMCaps;

/**
 * A wrapper interface of {@link CLivingEntityDelayedActionHandler}. Living Entities implementing this interface will be automatically 
 * attached {@link CLivingEntityDelayedActionHandler} capability.
 */
public interface ILivingDelayedActions
{
	
	public default LivingEntity getEntity()
	{
		return (LivingEntity)this;
	}
	
	/**
	 * Add a delayed action.
	 */
	public default void addDelayedAction(int delayTicks, Runnable action)
	{
		getEntity().getCapability(BMCaps.CAP_DELAYED_ACTION_HANDLER).ifPresent(cap -> cap.addDelayedAction(delayTicks, action));
	}
	
	/**
	 * Add a single action to multiple time points. The action will be done once on each time point.
	 */
	public default void addMultipleDelayedActions(Runnable action, int... timePoints)
	{
		getEntity().getCapability(BMCaps.CAP_DELAYED_ACTION_HANDLER).ifPresent(cap -> cap.addMultipleDelayedActions(action, timePoints));
	}
	
	/**
	 * Add a delayed action with a parameter.
	 * <p> Note: The parameter value will be determined NOW, not on running the action.
	 */
	public default <T> void addDelayedActionWithParam (int delayTicks, Consumer<T> action, T param)
	{
		getEntity().getCapability(BMCaps.CAP_DELAYED_ACTION_HANDLER).ifPresent(cap -> cap.addDelayedActionWithParam(delayTicks, action, param));
	}
	
	/**
	 * Add a single action with a parameter to multiple time points.
	 * <p> Note: The parameter value will be determined NOW, not on running the actions.
	 */
	public default <T> void addMultipleDelayedActionsWithParam (Consumer<T> action, T param, int... timePoints)
	{
		getEntity().getCapability(BMCaps.CAP_DELAYED_ACTION_HANDLER).ifPresent(cap -> cap.addMultipleDelayedActionsWithParam(action, param, timePoints));
	}
	
}
