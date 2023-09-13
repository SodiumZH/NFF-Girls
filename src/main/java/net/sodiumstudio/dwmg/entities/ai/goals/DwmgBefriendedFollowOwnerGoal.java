/**
 * 
 */
package net.sodiumstudio.dwmg.entities.ai.goals;

import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
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
	public boolean checkCanUse()
	{
		return super.checkCanUse() && !CFavorabilityHandler.isLowFavorability(mob.asMob());
	}

}
