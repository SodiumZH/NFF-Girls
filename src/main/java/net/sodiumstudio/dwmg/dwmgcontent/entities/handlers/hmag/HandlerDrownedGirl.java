package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;
import net.sodiumstudio.dwmg.befriendmobs.util.math.RndUtil;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class HandlerDrownedGirl extends HandlerItemGivingProgress
{

	@Override
	protected double getProcValueToAdd(ItemStack item) {
		double rnd = this.rnd.nextDouble();
		if (item.is(DwmgItems.SOUL_CAKE_SLICE.get()))
			return rnd < 0.05 ? 1.001 : (rnd < 0.2 ? 0.667 : 0.334);
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
	public boolean isItemAcceptable(Item item) {
		Item[] items = {
				DwmgItems.SOUL_CAKE_SLICE.get(),
				ModItems.SOUL_POWDER.get(),
				ModItems.SOUL_APPLE.get()
				};
		return MiscUtil.isIn(item, items, Items.AIR);
	}

	@Override
	public int getItemGivingCooldownTicks() {
		// TODO Auto-generated method stub
		return 200;
	}
	
}
