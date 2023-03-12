package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import net.minecraft.world.item.BowItem;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedRangedBowAttackGoal;

// Only for skeleton, stray and wither skeleton
// They consume arrows which locate at inventory 8 position
public class BefriendedSkeletonRangedBowAttackGoal extends BefriendedRangedBowAttackGoal {

	public BefriendedSkeletonRangedBowAttackGoal(IBefriendedMob pMob, double pSpeedModifier, int pAttackIntervalMin,
			float pAttackRadius) {
		super(pMob, pSpeedModifier, pAttackIntervalMin, pAttackRadius);
		allowAllStatesExceptWait();
	}

	@Override
	public boolean canUse() {
		if (!mob.asMob().isHolding(is -> is.getItem() instanceof BowItem))
			return false;
		else if (mob.getInventoryTag().get(8).isEmpty())
			return false;
		else
			return super.canUse();
	}
	
	@Override
	public void onArrowShot()
	{
		mob.getInventoryTag().consumeItem(8);
	}
}
