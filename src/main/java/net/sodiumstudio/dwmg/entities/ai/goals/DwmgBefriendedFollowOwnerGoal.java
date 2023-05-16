/**
 * 
 */
package net.sodiumstudio.dwmg.entities.ai.goals;

import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;

/**
 * @author SodiumZH
 *
 */
public class DwmgBefriendedFollowOwnerGoal extends BefriendedFollowOwnerGoal
{

	public DwmgBefriendedFollowOwnerGoal(IBefriendedMob inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly)
	{
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
	}
	
	@Override
	public boolean canUse()
	{
		return super.canUse() && !CFavorabilityHandler.isLowFavorability(mob.asMob());
	}

}
