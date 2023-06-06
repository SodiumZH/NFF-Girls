package net.sodiumstudio.dwmg.entities.ai.goals;

import net.sodiumstudio.befriendmobs.entity.vanillapreset.creeper.BefriendedCreeperSwellGoal;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedCreeperGirl;

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
	public boolean checkCanUse()
	{
		if (isDisabled())
			return false;
		return cg.getSwell() > 0;
	}
	
	private boolean stoppedFlag = false;
	@Override
	public void tick()
	{
		if (cg.getOwner() != null && cg.distanceToSqr(cg.getOwner()) <= cg.explodeSafeDistance * cg.explodeSafeDistance)
		{
			cg.setSwellDir(-1);
			stoppedFlag = true;
		}
		else if (stoppedFlag)
		{
			cg.setSwellDir(1);
			stoppedFlag = false;
		}
	}
	
	
}
