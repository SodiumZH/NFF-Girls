package net.sodiumzh.nff.girls.entity.handlers.hmag;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.sodiumzh.nautils.math.RndUtil;
import net.sodiumzh.nff.girls.registry.NFFGirlsEffects;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.services.entity.taming.TamableHatredReason;
import net.sodiumzh.nff.services.entity.taming.TamingProcessItemGivingProgress;

public class HmagZombieGirlTamingProcess extends TamingProcessItemGivingProgress
{

	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		double rnd = this.rnd.nextDouble();
		if (item.is(NFFGirlsItems.SOUL_CAKE_SLICE.get()))
			return rnd < 0.05 ? 1.0d : (rnd < 0.2d ? 0.666667d : 0.333334d);
		else if (item.is(ModItems.SOUL_POWDER.get()))
			return RndUtil.rndRangedDouble(0.02, 0.04);
		else if (item.is(ModItems.SOUL_APPLE.get()))
			return RndUtil.rndRangedDouble(0.04, 0.08);
		else return 0;
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob)
	{
		return player.hasEffect(NFFGirlsEffects.UNDEAD_AFFINITY.get());
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(NFFGirlsItems.SOUL_CAKE_SLICE.get())
				|| item.is(ModItems.SOUL_POWDER.get())
				|| item.is(ModItems.SOUL_APPLE.get());
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 200;
	}
	
	@Override
	public void onAttackProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
		//interrupt(player, mob, false);
	}
	
	@Override
	public void onAttackedByProcessingPlayer(Mob mob, Player player, boolean damageGiven)
	{
		if (damageGiven)
			interrupt(player, mob, false);		
	}

	@Override
	public TamableHatredReason[] getAddHatredReasons() {
		return new TamableHatredReason[] {TamableHatredReason.ATTACKED, TamableHatredReason.ATTACKING, TamableHatredReason.HIT};
	}
	
	@Override
	public int getHatredDurationTicks(TamableHatredReason reason)
	{
		switch(reason)
		{
		case ATTACKED:
		{
			return 300 * 20;
		}
		case ATTACKING:
		{
			return 60 * 20;
		}
		case HIT:
		{
			return 30 * 20;
		}
		default:
		{
			return 0;
		}
		}
		
	}
}
