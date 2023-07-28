package net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move;

import net.minecraft.world.entity.LivingEntity;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;

public class BefriendedLeapAtTargetGoal extends BefriendedLeapAtGoal
{

	protected LivingEntity target = null;
	
	public BefriendedLeapAtTargetGoal(IBefriendedMob mob, float yd, float xzd, float maxAttackDistance, int chance)
	{
		super(mob, yd, xzd, maxAttackDistance, chance);
	}

	@Override
	public boolean checkCanUse()
	{
		this.target = this.mob.asMob().getTarget();
		this.setTargetPos(target);
		return super.checkCanUse();
	}

}