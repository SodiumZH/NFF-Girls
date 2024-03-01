package net.sodiumstudio.dwmg.entities.ai.goals;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TridentItem;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedRangedAttackGoal;

public class BefriendedDrownedTridentAttackGoal extends BefriendedRangedAttackGoal {

	public BefriendedDrownedTridentAttackGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
		super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	@Override
	public boolean checkCanUse() {
		return super.checkCanUse() 
				&& this.mob.asMob().getMainHandItem().getItem() != null 
				&& this.mob.asMob().getMainHandItem().getItem() instanceof TridentItem
				&& this.mob.asMob().getTarget() != null
				&& this.mob.asMob().hasLineOfSight(this.mob.asMob().getTarget())
				&& this.mob.asMob().distanceToSqr(this.mob.asMob().getTarget()) > 9.0d;
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void onStart() {
		super.onStart();
		this.mob.asMob().setAggressive(true);
		this.mob.asMob().startUsingItem(InteractionHand.MAIN_HAND);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void onStop() {
		super.onStop();
		this.mob.asMob().stopUsingItem();
		this.mob.asMob().setAggressive(false);
	}
}
