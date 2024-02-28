package net.sodiumstudio.dwmg.entities.ai.goals.target;

import java.util.function.Predicate;

import net.minecraft.world.entity.Mob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.target.BefriendedNearestUnfriendlyMobTargetGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

public class DwmgNearestHostileToSelfTargetGoal extends BefriendedNearestUnfriendlyMobTargetGoal
{

	public DwmgNearestHostileToSelfTargetGoal(IDwmgBefriendedMob pMob, Predicate<Mob> condition)
	{		
		super(pMob, true, true);
		stateConditions(bm -> bm instanceof IDwmgBefriendedMob dbm && dbm.shouldAttackMobsHostileToSelf());
		//targetOfTargetCondition(living -> living instanceof Mob && ((Mob)living).getTarget() == pMob.asMob());
		allowAllStatesExceptWait();
	}
	
	public DwmgNearestHostileToSelfTargetGoal(IDwmgBefriendedMob pMob)
	{
		this(pMob, l -> true);
	}

}
