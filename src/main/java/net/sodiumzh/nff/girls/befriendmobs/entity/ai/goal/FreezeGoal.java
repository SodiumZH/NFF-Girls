package net.sodiumzh.nff.girls.befriendmobs.entity.ai.goal;

import java.util.EnumSet;

import com.google.common.base.Predicate;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * A goal indicating the mob will do nothing but only standing there.
 */
public class FreezeGoal extends Goal
{

	protected Mob mob;
	protected Predicate<Mob> condition;
	
	public FreezeGoal(Mob mob, Predicate<Mob> condition)
	{
		this.mob = mob;
		this.condition = condition;
		this.setFlags(EnumSet.of(Flag.LOOK, Flag.JUMP, Flag.MOVE));
	}
	
	@Override
	public boolean canUse() {
		return condition.test(mob);
	}

}
