package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.Map;
import java.util.function.Predicate;

import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleEquipable;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerUndead extends BaubleHandlerGeneral
{
	
	@Override
	public Map<String, Predicate<IBaubleEquipable>> getItemKeysAccepted(String key, IBaubleEquipable mob) {
		Map<String, Predicate<IBaubleEquipable>> map = super.getItemKeysAccepted(key, mob);
		map.put("dwmg:soul_amulet", null);
		map.put("dwmg:soul_amulet_ii", null);
		return map;
	}

	@Override
	public void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleEquipable owner) {
		super.refreshBaubleEffect(slotKey, bauble, owner);
		if (bauble.is(DwmgItems.SOUL_AMULET.get()))
		{
			owner.addBaubleModifier(slotKey, "sa_health", Attributes.MAX_HEALTH, 10.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "sa_atk", Attributes.ATTACK_DAMAGE, 3.0d, Operation.ADDITION);
		}
		if (bauble.is(DwmgItems.SOUL_AMULET_II.get()))
		{
			owner.addBaubleModifier(slotKey, "sa_health", Attributes.MAX_HEALTH, 15.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "sa_atk", Attributes.ATTACK_DAMAGE, 5.0d, Operation.ADDITION);
		}
	}
}
