package net.sodiumstudio.dwmg.entities.handlers.hmag;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.nautils.math.RandomSelection;
import net.sodiumstudio.nautils.math.RndUtil;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class HandlerWitherSkeletonGirl extends HandlerZombieGirl
{	
	@Override
	public boolean isItemAcceptable(Item item)
	{
		return super.isItemAcceptable(item) || item == Items.NETHER_STAR;
	}
	
	@Override
	protected double getProcValueToAdd(ItemStack item) {
		if (item.is(DwmgItems.SOUL_CAKE_SLICE.get()))
			return RandomSelection.create(0.15d)
					.add(0.30d, 0.15d)
					.add(0.45d, 0.04d)
					.add(0.60d, 0.01d)
					.getDouble();
		else if (item.is(Items.NETHER_STAR))
			return RandomSelection.create(0.50d)
					.add(1.00d, 0.20d)
					.getDouble();
		else if (item.is(ModItems.SOUL_POWDER.get()))
			return RndUtil.rndRangedDouble(0.005d, 0.01d);
		else if (item.is(ModItems.SOUL_APPLE.get()))
			return RndUtil.rndRangedDouble(0.01d, 0.02d);
		else return 0;
	}
}
