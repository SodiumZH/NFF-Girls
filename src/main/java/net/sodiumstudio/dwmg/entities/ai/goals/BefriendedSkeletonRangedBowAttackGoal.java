package net.sodiumstudio.dwmg.entities.ai.goals;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedRangedBowAttackGoal;

// Only for skeleton, stray and wither skeleton
// They consume arrows which locate at inventory 8 position
public class BefriendedSkeletonRangedBowAttackGoal extends BefriendedRangedBowAttackGoal {

	public BefriendedSkeletonRangedBowAttackGoal(IBefriendedMob pMob, double pSpeedModifier, int pAttackIntervalMin,
			float pAttackRadius) {
		super(pMob, pSpeedModifier, pAttackIntervalMin, pAttackRadius);
		allowAllStatesExceptWait();
	}
	
	@Override
	public boolean checkCanUse() {
		if (mob.getAdditionalInventory().getItem(4).isEmpty())
			return false;
		else if (mob.getAdditionalInventory().getItem(8).isEmpty())
			return false;
		else if (!(mob.getAdditionalInventory().getItem(4).getItem() instanceof BowItem))
			return false;
		else
			return super.checkCanUse();
	}
	
}
