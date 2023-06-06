package net.sodiumstudio.dwmg.entities.ai.goals;

import javax.annotation.Nonnull;

import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFollowOwnerGoal;
import net.sodiumstudio.befriendmobs.entity.vanillapreset.creeper.AbstractBefriendedCreeper;

/* Adjusted from vanilla FollowOwnerGoal for TameableAnimal */
public class DwmgBefriendedCreeperFollowOwnerGoal extends DwmgBefriendedFollowOwnerGoal {

	protected AbstractBefriendedCreeper creeper;
	public DwmgBefriendedCreeperFollowOwnerGoal(@Nonnull IBefriendedMob inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly) {
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
		creeper = (AbstractBefriendedCreeper) inMob;
	}

	@Override
	public boolean checkCanUse()
	{
		if (creeper.getSwellDir() > 0)
			return false;
		return super.checkCanUse();
	}
	
	
}
