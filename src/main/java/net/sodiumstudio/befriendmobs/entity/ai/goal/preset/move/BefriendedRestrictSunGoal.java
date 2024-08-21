package net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.util.GoalUtils;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedMoveGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedAmphibious;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedSunSensitiveMob;

// Adjusted from vanilla RestrictSunGoal
public class BefriendedRestrictSunGoal extends BefriendedMoveGoal {

	// If true, the mob will restrict sun although having a helmet.
	public boolean ignoreHelmet = false;

	public BefriendedRestrictSunGoal(IBefriendedMob mob) {
		super(mob, 1d);
		allowAllStates();
	}

	@Override
	public boolean checkCanUse() {
		if (isDisabled())
			return false;
		else if (!getPathfinder().level.isDay())
			return false;
		else if (!ignoreHelmet && !getPathfinder().getItemBySlot(EquipmentSlot.HEAD).isEmpty())
			return false;
		else if (mob instanceof IBefriendedSunSensitiveMob bssm && bssm.isSunImmune())
			return false;
		else if (this.mob.asMob().isInWater())
			return false;
		else if (!GoalUtils.hasGroundPathNavigation(getPathfinder()))
			return false;
		else return true;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void onStart() {
		super.onStart();
		if (isAmphibious)
		{
			((IBefriendedAmphibious)mob).switchNav(false);			
		}
		if (GoalUtils.hasGroundPathNavigation(getPathfinder()))
			((GroundPathNavigation) getPathfinder().getNavigation()).setAvoidSun(true);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void onStop() {
		if (GoalUtils.hasGroundPathNavigation(getPathfinder())) {
			((GroundPathNavigation) getPathfinder().getNavigation()).setAvoidSun(false);
		}
	}
}
