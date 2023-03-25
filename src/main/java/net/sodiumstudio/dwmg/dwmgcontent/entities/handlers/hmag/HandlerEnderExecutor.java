package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import java.util.HashSet;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.handlerpreset.HandlerItemGivingProcess;

public class HandlerEnderExecutor extends HandlerItemGivingProcess
{

	@Override
	protected HashSet<Item> givableItems() {
//		HashSet<Item> items = new HashSet<Item>();
		
		return null;
	}

	@Override
	protected double getProcValue(ItemStack item) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected int cooldownTicks() {
		// TODO Auto-generated method stub
		return 0;
	}

}
