package net.sodiumstudio.dwmg.entities.ai.goals.target;

import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.target.BefriendedNearestUnfriendlyMobTargetGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

public class DwmgNearestHostileToOwnerTargetGoal extends BefriendedNearestUnfriendlyMobTargetGoal
{
	public DwmgNearestHostileToOwnerTargetGoal(IDwmgBefriendedMob mob, Predicate<Mob> condition)
	{		
		super(mob, true, true);
		stateConditions(bm -> bm instanceof IDwmgBefriendedMob dbm && dbm.hasDwmgBaubleWithMinLevel("courage_amulet", 2));
		targetOfTargetConditions(living -> mob.isOwnerPresent() && living == mob.getOwner());
		allowAllStatesExceptWait();
	}
	
	public DwmgNearestHostileToOwnerTargetGoal(IDwmgBefriendedMob pMob)
	{
		this(pMob, l -> true);
	}
}
