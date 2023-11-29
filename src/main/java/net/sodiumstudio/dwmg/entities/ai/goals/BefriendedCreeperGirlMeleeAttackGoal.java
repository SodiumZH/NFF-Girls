package net.sodiumstudio.dwmg.entities.ai.goals;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.TieredItem;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;
import net.sodiumstudio.dwmg.entities.hmag.HmagCreeperGirlEntity;

public class BefriendedCreeperGirlMeleeAttackGoal extends BefriendedMeleeAttackGoal
{

	protected HmagCreeperGirlEntity creeper;
	
	public BefriendedCreeperGirlMeleeAttackGoal(IBefriendedMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen)
	{
		super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
		creeper = (HmagCreeperGirlEntity)pMob;
	}

	@Override
	public boolean checkCanUse()
	{
		if (creeper.hasEnoughAmmoToExplode() && creeper.blowEnemyCooldown == 0)
			return false;
		if (creeper.getSwellDir() > 0)
			return false;
		// Creeper girls perform melee attack only when having a weapon on the main hand
		if (creeper.getAdditionalInventory().getItem(4).isEmpty() 
				|| !(creeper.getAdditionalInventory().getItem(4).getItem() instanceof TieredItem))
			return false;
		return super.checkCanUse();
	}

	@Override
	public boolean checkCanContinueToUse()
	{
		if (creeper.getAdditionalInventory().getItem(4).isEmpty() 
				|| !(creeper.getAdditionalInventory().getItem(4).getItem() instanceof TieredItem))
			return false;
		else return super.checkCanContinueToUse();
	}
	
	@Override
	public void onStop() {
		LivingEntity livingentity = getPathfinder().getTarget();
		if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity))
		{
			getPathfinder().setTarget((LivingEntity) null);
		}

		getPathfinder().setAggressive(false);
		getPathfinder().getNavigation().stop();
	}
	
}
