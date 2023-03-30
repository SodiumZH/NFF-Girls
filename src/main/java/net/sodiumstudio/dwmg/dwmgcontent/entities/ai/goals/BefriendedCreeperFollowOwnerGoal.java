package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import javax.annotation.Nonnull;

import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.vanillapreset.creeper.AbstractBefriendedCreeper;

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
