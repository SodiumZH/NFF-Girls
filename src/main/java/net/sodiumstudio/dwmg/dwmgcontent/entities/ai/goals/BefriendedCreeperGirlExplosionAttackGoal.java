package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import java.util.EnumSet;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.BefriendedGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedMeleeAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.creeper.AbstractBefriendedCreeper;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedCreeperGirl;

public class BefriendedCreeperGirlExplosionAttackGoal extends BefriendedMeleeAttackGoal
{

	EntityBefriendedCreeperGirl creeper;

	public boolean canUse()
	{
		if (!creeper.canAutoBlowEnemy)
			return false;
		if (creeper.isSwelling())
			return false;
		if (creeper.blowEnemyCooldown > 0)
			return false;
		if (!creeper.hasEnoughAmmoToExplode())
			return false;
		return super.canUse();
	}
	
	public BefriendedCreeperGirlExplosionAttackGoal(IBefriendedMob mob, double speedModifier, boolean followingTargetEvenIfNotSeen)
	{
		super(mob, speedModifier, followingTargetEvenIfNotSeen);
		this.creeper = (EntityBefriendedCreeperGirl)mob;
	}

	protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
		if (distToEnemySqr < 9.0d && creeper.blowEnemyCooldown == 0)
		{
			this.resetAttackCooldown();
			creeper.setSwelling(true);
			creeper.blowEnemyCooldown = 300;	// 15s cooldown per explosion
		}
	}
	
}
