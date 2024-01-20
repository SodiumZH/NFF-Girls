package net.sodiumstudio.dwmg.entities.ai.goals.target;

import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.target.BefriendedNearestUnfriendlyMobTargetGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

/**
 * @deprecated Only for old bauble system.
 */
@Deprecated
public class DwmgNearestHostileToOwnerTargetGoalLegacy extends BefriendedNearestUnfriendlyMobTargetGoal
{
	public DwmgNearestHostileToOwnerTargetGoalLegacy(IDwmgBefriendedMob mob, Predicate<Mob> condition)
	{		
		super(mob, true, true);
		stateConditions(bm -> bm instanceof IDwmgBefriendedMob dbm && dbm.hasDwmgBaubleWithMinLevel("courage_amulet", 2));
		targetOfTargetConditions(living -> mob.isOwnerPresent() && living == mob.getOwner());
		allowAllStatesExceptWait();
	}
	
	public DwmgNearestHostileToOwnerTargetGoalLegacy(IDwmgBefriendedMob pMob)
	{
		this(pMob, l -> true);
	}
}
