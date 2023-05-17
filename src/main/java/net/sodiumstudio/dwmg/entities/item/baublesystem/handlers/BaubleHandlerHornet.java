package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Predicate;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedHornet;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerHornet extends BaubleHandlerGeneral
{
	
	@Override
	public HashMap<Item, Predicate<IBaubleHolder>> getItemsAccepted(String key, IBaubleHolder mob) {
		HashMap<Item, Predicate<IBaubleHolder>> map = super.getItemsAccepted(key, mob);
		map.put(DwmgItems.POISONOUS_THORN.get(), null);
		return map;
	}
	
	@Override
	public void postTick(IBaubleHolder owner)
	{
		super.postTick(owner);
		if (owner instanceof EntityBefriendedHornet b)
		{
			if (owner.hasBaubleItem(DwmgItems.POISONOUS_THORN.get()))
			{
				b.addPoisonLevel = 2;
			}
			else
			{
				b.addPoisonLevel = 1;
			}
		}
	}
	
}
