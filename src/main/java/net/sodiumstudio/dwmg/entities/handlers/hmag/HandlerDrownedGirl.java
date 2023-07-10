package net.sodiumstudio.dwmg.entities.handlers.hmag;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.nautils.math.RndUtil;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class HandlerDrownedGirl extends HandlerZombieGirl
{
	@Override
	protected double getProcValueToAdd(ItemStack item) {
		double rnd = this.rnd.nextDouble();
		if (item.is(DwmgItems.SOUL_CAKE_SLICE.get()))
			return rnd < 0.05 ? 1.00 : (rnd < 0.2 ? 0.666667 : 0.333334);
		else if (item.is(ModItems.SOUL_POWDER.get()))
			return RndUtil.rndRangedDouble(0.02, 0.04);
		else if (item.is(ModItems.SOUL_APPLE.get()))
			return RndUtil.rndRangedDouble(0.04, 0.08);
		else return 0;
	}
}
