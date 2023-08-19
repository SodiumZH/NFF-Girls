package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.Map;
import java.util.function.Predicate;

import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.entities.hmag.HmagHornetEntity;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerHornet extends BaubleHandlerGeneral
{
	
	@Override
	public Map<String, Predicate<IBaubleHolder>> getItemKeysAccepted(String key, IBaubleHolder mob) {
		Map<String, Predicate<IBaubleHolder>> map = super.getItemKeysAccepted(key, mob);
		map.put("dwmg:poisonous_thorn", null);
		return map;
	}
	
	@Override
	public void postTick(IBaubleHolder owner)
	{
		super.postTick(owner);
		if (owner instanceof HmagHornetEntity b)
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
