package net.sodiumstudio.dwmg.entities.ai.goals.target;

import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleEquipable;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.target.BefriendedNearestUnfriendlyMobTargetGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.registries.DwmgItems;

/**
 * @deprecated Only for old bauble system.
 */
public class DwmgNearestHostileToSelfTargetGoalLegacy extends BefriendedNearestUnfriendlyMobTargetGoal
{

	public DwmgNearestHostileToSelfTargetGoalLegacy(IDwmgBefriendedMob pMob, Predicate<Mob> condition)
	{		
		super(pMob, true, true);
		stateConditions(bm -> bm instanceof IDwmgBefriendedMob dbm && dbm.hasDwmgBauble("courage_amulet"));
		//targetOfTargetCondition(living -> living instanceof Mob && ((Mob)living).getTarget() == pMob.asMob());
		allowAllStatesExceptWait();
	}
	
	public DwmgNearestHostileToSelfTargetGoalLegacy(IDwmgBefriendedMob pMob)
	{
		this(pMob, l -> true);
	}

}
