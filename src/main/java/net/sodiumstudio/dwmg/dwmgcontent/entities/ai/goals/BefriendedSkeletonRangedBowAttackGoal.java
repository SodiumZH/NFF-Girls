package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
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
		if (!mob.getAdditionalInventory().getItem(4).is(Items.BOW))
			return false;
		else if (mob.getAdditionalInventory().getItem(8).isEmpty())
			return false;
		else
			return super.canUse();
	}
	
	@Override
	public void onArrowShot()
	{
		// consume arrow when the bow don't have infinity enchantment
		if (!(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, mob.getAdditionalInventory().getItem(4)) > 0))
			mob.getAdditionalInventory().consumeItem(8);
	}
}
