package net.sodiumstudio.dwmg.entities.ai.goals;

import net.minecraft.world.entity.EquipmentSlot;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;

public class BefriendedAvoidSunOnSleepGoal extends BefriendedFleeSunGoal
{

	public BefriendedAvoidSunOnSleepGoal(IBefriendedMob pMob, double pSpeedModifier)
	{
		super(pMob, pSpeedModifier);
	}

	@Override
	public boolean checkCanUse()
	{
		if (isDisabled())
			return false;
		if (!mob.getOwner().isSleeping())
			return false;
		// Not taking effect when player is > 16 blocks away
		else if (mob.asMob().distanceToSqr(mob.getOwner()) > 256f)
			return false;
		else if (!this.level.canSeeSky(getPathfinder().blockPosition())) 
			return false;
		else if (!ignoreHelmet && !getPathfinder().getItemBySlot(EquipmentSlot.HEAD).isEmpty())
			return false;
		else if	(!this.setWantedPos())
			return false;
		else if (mob.asMob().isInWater())
			return false;
		else return true;
	}
}
