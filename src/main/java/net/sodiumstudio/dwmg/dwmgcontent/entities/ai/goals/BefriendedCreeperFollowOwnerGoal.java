package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import javax.annotation.Nonnull;

import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.creeper.AbstractBefriendedCreeper;

/* Adjusted from vanilla FollowOwnerGoal for TameableAnimal */
public class BefriendedCreeperFollowOwnerGoal extends BefriendedFollowOwnerGoal {

	protected AbstractBefriendedCreeper creeper;
	public BefriendedCreeperFollowOwnerGoal(@Nonnull IBefriendedMob inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly) {
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
		creeper = (AbstractBefriendedCreeper) inMob;
	}

	@Override
	public boolean canUse()
	{
		if (creeper.isSwelling())
			return false;
		return super.canUse();
	}
	
	
}
