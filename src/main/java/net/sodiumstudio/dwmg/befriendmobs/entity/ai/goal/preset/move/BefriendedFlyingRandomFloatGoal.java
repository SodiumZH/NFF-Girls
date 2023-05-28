package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import java.util.EnumSet;
import java.util.Random;

import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;


// Ported from Ghast
public class BefriendedFlyingRandomFloatGoal extends BefriendedFlyingMoveGoal
{

	public float maxStepLength = 8f;
	
    public BefriendedFlyingRandomFloatGoal(IBefriendedMob mob) {
    	super(mob);
       this.setFlags(EnumSet.of(Goal.Flag.MOVE));
       this.allowState(WANDER);
    }

    /**
     * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
     * method as well.
     */
    @Override
	public boolean canUse() {
       MoveControl movecontrol = this.mob.asMob().getMoveControl();
       if (!movecontrol.hasWanted()) {
          return true;
       } else {
          double d0 = movecontrol.getWantedX() - this.mob.asMob().getX();
          double d1 = movecontrol.getWantedY() - this.mob.asMob().getY();
          double d2 = movecontrol.getWantedZ() - this.mob.asMob().getZ();
          double d3 = d0 * d0 + d1 * d1 + d2 * d2;
          return d3 < 1.0D || d3 > 3600.0D;
       }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
	public boolean canContinueToUse() {
       return false;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void start() {
       Random randomsource = this.mob.asMob().getRandom();
       double d0 = this.mob.asMob().getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * maxStepLength);
       double d1 = this.mob.asMob().getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * maxStepLength);
       double d2 = this.mob.asMob().getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * maxStepLength);
       this.mob.asMob().getMoveControl().setWantedPosition(d0, d1, d2, 1.0D);
    }
}
