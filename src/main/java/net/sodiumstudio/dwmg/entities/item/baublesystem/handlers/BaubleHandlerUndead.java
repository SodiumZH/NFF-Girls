package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashMap;
import java.util.function.Predicate;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerUndead extends BaubleHandlerGeneral
{
	
	@Override
	public HashMap<Item, Predicate<IBaubleHolder>> getItemsAccepted(String key, IBaubleHolder mob) {
		HashMap<Item, Predicate<IBaubleHolder>> map = super.getItemsAccepted(key, mob);
		map.put(DwmgItems.SOUL_AMULET.get(), null);
		return map;
	}

	@Override
	public void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleHolder owner) {
		super.refreshBaubleEffect(slotKey, bauble, owner);
		if (bauble.is(DwmgItems.SOUL_AMULET.get()))
		{
			owner.addBaubleModifier(slotKey, "sa_health", Attributes.MAX_HEALTH, 10.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "sa_atk", Attributes.ATTACK_DAMAGE, 3.0d, Operation.ADDITION);
		}
	}
	
	/**
	 * Condition that the mob should be sun-immune (for {@code IBefriendedUndeadMob})
	 */
	protected boolean shouldBeSunImmune(IBaubleHolder mob)
	{
		return (mob.hasBaubleItem(DwmgItems.SOUL_AMULET.get()) 
				|| mob.hasBaubleItem(DwmgItems.RESISTANCE_AMULET.get())
				|| mob.getLiving().getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.SUNHAT.get()));
	}
	
}
