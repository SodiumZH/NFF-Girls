package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedFollowOwnerGoal;

// For skeletons, strays and wither skeletons only
// They consume arrows
public class BefriendedSunAvoidingFollowOwnerGoal extends BefriendedFollowOwnerGoal {

	// If true, the mob will avoid sun even if having a helmet
	public boolean ignoreHelmet = false;
	
	public BefriendedSunAvoidingFollowOwnerGoal(IBefriendedMob inMob, double pSpeedModifier, float pStartDistance,
			float pStopDistance, boolean pCanFly) 
	{
		super(inMob, pSpeedModifier, pStartDistance, pStopDistance, pCanFly);
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
	
	@Override
	protected boolean canTeleportTo(BlockPos pPos) {
		if (!level.canSeeSky(pPos) && !level.isWaterAt(pPos))
			return false;
		return super.canTeleportTo(pPos);
	}
	
}
