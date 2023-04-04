package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.handlers;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgItems;

public class BaubleHandlerZombies extends BaubleHandler
{

	@Override
	public HashSet<Item> getItemsAccepted() {
		HashSet<Item> set = new HashSet<Item>();
		set.add(DwmgItems.DEATH_CRYSTAL.get());
		set.add(ModItems.BURNING_CORE.get());
		return set;
	}

	@Override
	public void updateBaubleEffects(IBaubleHolder owner) {
		if (owner.containsBauble(DwmgItems.DEATH_CRYSTAL.get()))
			
	}

}
