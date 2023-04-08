package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.BefriendedRangedAttackGoal;

public class BefriendedDrownedTridentAttackGoal extends BefriendedRangedAttackGoal {

	public BefriendedDrownedTridentAttackGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius) {
		super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
	}

	/**
	 * Returns whether execution should begin. You can also read and cache any state
	 * necessary for execution in this method as well.
	 */
	@Override
	public boolean canUse() {
		return super.canUse() && this.mob.asMob().getMainHandItem().is(Items.TRIDENT);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void start() {
		super.start();
		this.mob.asMob().setAggressive(true);
		this.mob.asMob().startUsingItem(InteractionHand.MAIN_HAND);
	}

	/**
	 * Reset the task's internal state. Called when this task is interrupted by
	 * another one
	 */
	@Override
	public void stop() {
		super.stop();
		this.mob.asMob().stopUsingItem();
		this.mob.asMob().setAggressive(false);
	}
}
