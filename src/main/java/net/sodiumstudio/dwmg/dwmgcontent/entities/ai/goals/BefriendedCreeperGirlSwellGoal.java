package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import net.sodiumstudio.dwmg.befriendmobs.entitiy.creeper.BefriendedCreeperSwellGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedCreeperGirl;

public class BefriendedCreeperGirlSwellGoal extends BefriendedCreeperSwellGoal
{
	
	protected EntityBefriendedCreeperGirl cg;
	
	public BefriendedCreeperGirlSwellGoal(EntityBefriendedCreeperGirl creeper)
	{
		super(creeper);
		this.cg = (EntityBefriendedCreeperGirl)creeper;
		targetedSwelling = false;
	}
	
	@Override
	public boolean canUse()
	{
		return cg.swell > 0;
	}
	
	private boolean stoppedFlag = false;
	@Override
	public void tick()
	{
		if (cg.getOwner() != null && cg.distanceToSqr(cg.getOwner()) <= cg.explodeSafeDistance * cg.explodeSafeDistance)
		{
			cg.setSwelling(false);
			stoppedFlag = true;
		}
		else if (stoppedFlag)
		{
			cg.setSwelling(true);
			stoppedFlag = false;
		}
	}
	
	
}
