package net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.TieredItem;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.hmag.EntityBefriendedCreeperGirl;

public class BefriendedCreeperGirlMeleeAttackGoal extends BefriendedMeleeAttackGoal
{

	protected EntityBefriendedCreeperGirl creeper;
	
	public BefriendedCreeperGirlMeleeAttackGoal(IBefriendedMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen)
	{
		super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
		creeper = (EntityBefriendedCreeperGirl)pMob;
	}

	@Override
	public boolean canUse()
	{
		if (creeper.hasEnoughAmmoToExplode() && creeper.blowEnemyCooldown == 0)
			return false;
		if (creeper.getSwellDir() > 0)
			return false;
		// Creeper girls perform melee attack only when having a weapon on the main hand
		if (creeper.getAdditionalInventory().getItem(4).isEmpty() 
				|| !(creeper.getAdditionalInventory().getItem(4).getItem() instanceof TieredItem))
			return false;
		return super.canUse();
	}

	@Override
	public boolean canContinueToUse()
	{
		if (creeper.getAdditionalInventory().getItem(4).isEmpty() 
				|| !(creeper.getAdditionalInventory().getItem(4).getItem() instanceof TieredItem))
			return false;
		else return super.canContinueToUse();
	}
	
	@Override
	public void stop() {
		LivingEntity livingentity = getPathfinder().getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity))
		{
			getPathfinder().setTarget((LivingEntity) null);
		}

		getPathfinder().setAggressive(false);
		getPathfinder().getNavigation().stop();
	}
	
}
