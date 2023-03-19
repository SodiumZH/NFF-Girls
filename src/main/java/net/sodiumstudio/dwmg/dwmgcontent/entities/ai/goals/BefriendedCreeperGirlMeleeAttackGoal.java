package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import java.util.EnumSet;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.pathfinder.Path;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.BefriendedGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedMeleeAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.creeper.AbstractBefriendedCreeper;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedCreeperGirl;

public class BefriendedCreeperGirlMeleeAttackGoal extends BefriendedMeleeAttackGoal
{

	protected EntityBefriendedCreeperGirl creeper;
	
	public BefriendedCreeperGirlMeleeAttackGoal(IBefriendedMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen)
	{
		super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
		creeper = (EntityBefriendedCreeperGirl)pMob;
	}

	public boolean canUse()
	{
		if (creeper.hasEnoughAmmoToExplode() && creeper.blowEnemyCooldown == 0)
			return false;
		if (creeper.isSwelling())
			return false;
		return super.canUse();
	}
	
}
