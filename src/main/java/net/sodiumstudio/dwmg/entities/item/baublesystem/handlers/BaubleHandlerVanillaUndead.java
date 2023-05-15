package net.sodiumstudio.dwmg.entities.item.baublesystem.handlers;

import java.util.HashSet;

import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class BaubleHandlerVanillaUndead extends BaubleHandlerGeneral
{
	
	@Override
	public HashSet<Item> getItemsAccepted(String key) {
		HashSet<Item> set = super.getItemsAccepted(key);
		set.add(DwmgItems.SOUL_AMULET.get());
		return set;
	}

	@Override
	public void refreshBaubleEffect(String slotKey, ItemStack bauble, IBaubleHolder owner) {
		super.refreshBaubleEffect(slotKey, bauble, owner);
		if (bauble.is(DwmgItems.SOUL_AMULET.get()))
		{
			owner.addBaubleModifier(slotKey, "sa_health", Attributes.MAX_HEALTH, 10.0d, Operation.ADDITION);
			owner.addBaubleModifier(slotKey, "sa_atk", Attributes.ATTACK_DAMAGE, 3.0d, Operation.ADDITION);
		}
		if (owner instanceof IBefriendedUndeadMob undead)
		{
			if (shouldBeSunImmune(owner))
			{
				undead.setSunSensitive(false);
			}
			else
			{
				undead.setSunSensitive(true);
			}
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
