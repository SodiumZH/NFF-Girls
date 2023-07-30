package net.sodiumstudio.dwmg.util;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.nautils.ReflectHelper;

public class DwmgEntityHelper
{
	
	/**
	 * Check if a befriended mob is not waiting.
	 * If it's not a befriended mob, return always true.
	 */
	public static boolean isNotWaiting(LivingEntity living)
	{
		return living instanceof IBefriendedMob bm && bm.getAIState() != BefriendedAIState.WAIT;
	}
	
	/**
	 * Check if a mob isn't wearing/holding any golden items
	 * For piglin hostility
	 */
	public static boolean isNotWearingGold(LivingEntity living)
	{
		Item[] golds = {Items.GOLD_BLOCK, Items.GOLD_INGOT, Items.GOLD_NUGGET, ModItems.GOLDEN_FORK.get()};
		for (EquipmentSlot slot: EquipmentSlot.values())
		{
			Item item = living.getItemBySlot(slot).getItem();
			if (item instanceof ArmorItem armor)
			{
				if (armor.getMaterial().equals(ArmorMaterials.GOLD))
					return false;
			}
			else if (item instanceof TieredItem tiered)
			{
				if (tiered.getTier() == Tiers.GOLD)
					return false;
			}
			else for (int i = 0; i < golds.length; ++i)
			{
				if (item == golds[i])
					return false;
			}
		}
		return true;
	}
	
	public static boolean isOnEitherHand(LivingEntity living, Item item)
	{
		return living.getMainHandItem().is(item) || living.getOffhandItem().is(item);
	}
	
	/**
	 * Force set equipment item without any other operation.
	 */
	@SuppressWarnings("unchecked")
	public static void setMobEquipmentWithoutSideEffect(Mob mob, EquipmentSlot slot, ItemStack item)
	{
		NonNullList<ItemStack> slotList = null;
		switch (slot.getType())
		{
		case HAND:
			slotList = (NonNullList<ItemStack>) ReflectHelper.forceGet(mob, Mob.class, "f_21350_");	// Mob.handItems
			break;
		case ARMOR:
			slotList = (NonNullList<ItemStack>) ReflectHelper.forceGet(mob, Mob.class, "f_21351_");	// Mob.armorItems
		}
		if (slotList != null)
			slotList.set(slot.getIndex(), item);
	}
}
