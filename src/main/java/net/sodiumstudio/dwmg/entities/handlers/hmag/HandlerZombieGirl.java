package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.nautils.MiscUtil;
import net.sodiumstudio.nautils.math.RndUtil;
import net.sodiumstudio.dwmg.registries.DwmgEffects;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class HandlerZombieGirl extends HandlerItemGivingProgress
{

	@Override
	protected double getProcValueToAdd(ItemStack item, Player player, Mob mob, double lastProc) {
		double rnd = this.rnd.nextDouble();
		if (item.is(DwmgItems.SOUL_CAKE_SLICE.get()))
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
		return player.hasEffect(DwmgEffects.UNDEAD_AFFINITY.get());
	}

	@Override
	public boolean isItemAcceptable(ItemStack item) {
		return item.is(DwmgItems.SOUL_CAKE_SLICE.get())
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
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
		set.add(BefriendableAddHatredReason.ATTACKING);
		set.add(BefriendableAddHatredReason.HIT);
		return set;
	}
	
	@Override
	public int getHatredDurationTicks(BefriendableAddHatredReason reason)
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
