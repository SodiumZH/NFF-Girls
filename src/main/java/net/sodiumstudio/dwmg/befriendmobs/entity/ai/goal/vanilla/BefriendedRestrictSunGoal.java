package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;

// Adjusted from vanilla RestrictSunGoal
public class BefriendedRestrictSunGoal extends BefriendedGoal {

	// If true, the mob will restrict sun although having a helmet.
	public boolean ignoreHelmet = false;

	public BefriendedRestrictSunGoal(IBefriendedMob pMob) {
		mob = pMob;
		allowAllStates();
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this handler as well.
	 */
	public boolean canUse() {
		if (isDisabled())
			return false;
		else if (!getPathfinder().level.isDay())
			return false;
		else if (!ignoreHelmet && !getPathfinder().getItemBySlot(EquipmentSlot.HEAD).isEmpty())
			return false;
		else if (GoalUtils.hasGroundPathNavigation(getPathfinder()))
			return false;
		else return true;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	public void start() {
		if (GoalUtils.hasGroundPathNavigation(getPathfinder()))
			((GroundPathNavigation) getPathfinder().getNavigation()).setAvoidSun(true);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	public void stop() {
		if (GoalUtils.hasGroundPathNavigation(getPathfinder())) {
			((GroundPathNavigation) getPathfinder().getNavigation()).setAvoidSun(false);
		}
	}
}
