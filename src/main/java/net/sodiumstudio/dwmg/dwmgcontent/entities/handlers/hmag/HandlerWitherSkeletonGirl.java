package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;
import net.sodiumstudio.dwmg.befriendmobs.util.math.RandomSelection;
import net.sodiumstudio.dwmg.befriendmobs.util.math.RndUtil;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class HandlerWitherSkeletonGirl extends HandlerSkeletonGirl
{
	
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
	
	@Override
	public boolean isItemAcceptable(Item item) {
		Item[] items = {
				DwmgItems.SOUL_CAKE_SLICE.get(),
				ModItems.SOUL_POWDER.get(),
				ModItems.SOUL_APPLE.get(),
				Items.NETHER_STAR
				};
		return MiscUtil.isIn(item, items, Items.AIR);
	}
}
