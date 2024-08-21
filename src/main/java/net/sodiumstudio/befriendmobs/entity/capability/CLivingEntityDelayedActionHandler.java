package net.sodiumstudio.befriendmobs.entity.capability;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.sodiumstudio.befriendmobs.registry.BMCaps;

public interface CLivingEntityDelayedActionHandler
{
	
	public HashSet<DelayedActionEntry> getActions();
	
	public void tick();
	
	public LivingEntity getEntity();
	
	/**
	 * Add a delayed action.
	 * @param delayTicks Ticks to delay before doing the action.
	 * @param action The action to do after delaying.
	 */
	public void addDelayedAction(int delayTicks, Runnable action);
	
	/**
	 * Add a single action to multiple time points. The action will be done once on each time point.
	 */
	public void addMultipleDelayedActions(Runnable action, int... timePoints);
	
	/**
	 * Add a delayed action with a parameter.
	 * <p> Note: The parameter value will be determined NOW, not on running the action.
	 */
	public <T> void addDelayedActionWithParam (int delayTicks, Consumer<T> action, T param);
	
	/**
	 * Add a single action with a parameter to multiple time points.
	 * <p> Note: The parameter value will be determined NOW, not on running the actions.
	 */
	public <T> void addMultipleDelayedActionsWithParam (Consumer<T> action, T param, int... timePoints);
	
	public static class DelayedActionEntry
	{
		protected int countdownTicks;
		protected final Runnable action;
		public DelayedActionEntry(int delayTicks, Runnable action)
		{
			this.countdownTicks = delayTicks;
			this.action = action;
		}
	}
	
	public static class Impl implements CLivingEntityDelayedActionHandler
	{
		protected final LivingEntity living;
		protected final HashSet<DelayedActionEntry> actions;
		
		public Impl(LivingEntity living)
		{
			this.living = living;
			this.actions = new HashSet<>();
		}

		@Override
		public HashSet<DelayedActionEntry> getActions() {
			return actions;
		}

		@Override
		public void tick() {
			ArrayList<DelayedActionEntry> toRemove = new ArrayList<>();
			for (var entry: actions)
			{
				if (entry.countdownTicks <= 0)
				{
					entry.action.run();
					toRemove.add(entry);
				}
				else entry.countdownTicks--;
			}
			for (var entry: toRemove)
			{
				actions.remove(entry);
			}
		}

		@Override
		public LivingEntity getEntity() {
			return living;
		}

		@Override
		public void addDelayedAction(int delayTicks, Runnable action) {
			if (delayTicks <= 0)
				action.run();
			else
				actions.add(new DelayedActionEntry(delayTicks, action));
		}

		@Override
		public void addMultipleDelayedActions(Runnable action, int... timePoints) {
			for (int i = 0; i < timePoints.length; ++i)
			{
				this.addDelayedAction(timePoints[i], action);
			}
		}
		
		@Override
		public <T> void addDelayedActionWithParam (int delayTicks, Consumer<T> action, T param)
		{
			addDelayedAction(delayTicks, () -> action.accept(param));
		}

		@Override
		public <T> void addMultipleDelayedActionsWithParam(Consumer<T> action, T param, int... timePoints) {
			for (int i = 0; i < timePoints.length; ++i)
			{
				this.addDelayedActionWithParam(timePoints[i], action, param);
			}
		}
	}
	
	public static class Prvd implements ICapabilityProvider
	{

		protected CLivingEntityDelayedActionHandler cap;
		
		public Prvd(LivingEntity living)
		{
			cap = new CLivingEntityDelayedActionHandler.Impl(living);
		}
		
		@Override
		public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
			if(cap == BMCaps.CAP_DELAYED_ACTION_HANDLER && cap != null)
				return LazyOptional.of(() -> {return this.cap;}).cast();
			else
				return LazyOptional.empty();
		}
		
	}
}
