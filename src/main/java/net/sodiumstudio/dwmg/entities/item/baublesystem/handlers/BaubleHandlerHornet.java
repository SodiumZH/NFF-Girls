package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.Map;
import java.util.function.Predicate;

import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleEquipable;
import net.sodiumstudio.dwmg.entities.hmag.HmagHornetEntity;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerHornet extends BaubleHandlerGeneral
{
	
	@Override
	public Map<String, Predicate<IBaubleEquipable>> getItemKeysAccepted(String key, IBaubleEquipable mob) {
		Map<String, Predicate<IBaubleEquipable>> map = super.getItemKeysAccepted(key, mob);
		map.put("dwmg:poisonous_thorn", null);
		return map;
	}
	
	@Override
	public void postTick(IBaubleEquipable owner)
	{
		super.postTick(owner);
		if (owner instanceof HmagHornetEntity b)
		{
			int baublePoisonLevel = owner.countBaubleItem(DwmgItems.POISONOUS_THORN.get());
			b.addPoisonLevel = 1 + baublePoisonLevel * 1;
			b.addPoisonTicks = 10 * 20 + 5 * baublePoisonLevel * 20;
		}
	}
}
