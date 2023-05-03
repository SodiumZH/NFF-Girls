package net.sodiumstudio.dwmg.entities.ai.goals;

import javax.annotation.Nonnull;

import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFollowOwnerGoal;
import net.sodiumstudio.befriendmobs.entity.vanillapreset.creeper.AbstractBefriendedCreeper;

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
		if (creeper.getSwellDir() > 0)
			return false;
		return super.canUse();
	}
	
	
}
