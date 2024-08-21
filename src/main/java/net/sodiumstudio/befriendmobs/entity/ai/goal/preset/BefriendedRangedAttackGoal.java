package net.sodiumstudio.befriendmobs.entity.ai.goal.preset;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

public class BefriendedRangedAttackGoal extends BefriendedShootProjectileGoal
{

	protected RangedAttackMob rangedAttackMob;
	
	public BefriendedRangedAttackGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackInterval,
			float pAttackRadius)
	{
		super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
		if (mob instanceof RangedAttackMob ram)
			this.rangedAttackMob = ram;
		else throw new UnsupportedOperationException("BefriendedRangedAttackGoal requires mob to implement RangedAttackMob interface.");
	}

	public BefriendedRangedAttackGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackIntervalMin,
			int pAttackIntervalMax, float pAttackRadius)
	{
		super(mob, pSpeedModifier, pAttackIntervalMin, pAttackIntervalMax, pAttackRadius);
		if (mob instanceof RangedAttackMob ram)
			this.rangedAttackMob = ram;
		else throw new UnsupportedOperationException("BefriendedRangedAttackGoal requires mob to implement RangedAttackMob interface.");
	}

	@Override
	protected void performShooting(LivingEntity target, float velocity) {
		rangedAttackMob.performRangedAttack(target, velocity);
	}

	@Override
	protected LivingEntity updateTarget() {
		return mob.asMob().getTarget();
	}

}
