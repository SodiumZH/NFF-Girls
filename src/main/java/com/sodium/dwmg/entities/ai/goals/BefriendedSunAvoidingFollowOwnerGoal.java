package com.sodium.dwmg.entities.ai.goals;

import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.entitiy.ai.goal.vanilla.BefriendedFollowOwnerGoal;

import net.minecraft.world.entity.EquipmentSlot;

public class BefriendedSunAvoidingFollowOwnerGoal extends BefriendedFollowOwnerGoal {

	// If true, the mob will avoid sun even if having a helmet
	public boolean ignoreHelmet = false;
	
	public BefriendedSunAvoidingFollowOwnerGoal(IBefriendedMob inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly) 
	{
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() 
	{
		getPathfinder().getLookControl().setLookAt(mob.getOwner(), 10.0F, (float) getPathfinder().getMaxHeadXRot());
		if (--this.timeToRecalcPath <= 0) {
			this.timeToRecalcPath = this.adjustedTickDelay(10);
			if (!getPathfinder().isLeashed() && !getPathfinder().isPassenger()) {
				if (getPathfinder().distanceToSqr(mob.getOwner()) >= 144.0D) {
					// Do not teleport when player is under sun
					if (!getPathfinder().level.isDay() || !level.canSeeSky(mob.getOwner().blockPosition()) || (!ignoreHelmet && !getPathfinder().getItemBySlot(EquipmentSlot.HEAD).isEmpty()))
						this.teleportToOwner();
				} else {
					this.navigation.moveTo(mob.getOwner(), this.speedModifier);
				}
			}
		}
	}
	
	
}
