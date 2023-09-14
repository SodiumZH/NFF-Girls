package net.sodiumstudio.dwmg.util;

import com.mojang.logging.LogUtils;

import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.ReflectHelper;
import net.sodiumstudio.nautils.TagHelper;

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
		for (EquipmentSlot slot: EquipmentSlot.values())
		{
			if (TagHelper.hasTag(living.getItemBySlot(slot).getItem(), "minecraft:piglin_loved"))
			{
				return false;
			}
		}
		return true;
	}
	
	public static boolean isWearingGold(LivingEntity living)
	{
		return !isNotWearingGold(living);
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
	
	/**
	 * Check if a befriended undead mob will burn under sun.
	 * @param mobUndead Mob to test.
	 * @return Whether this mob is sun-sensitive, or always true if it doesn't implement {@link IBefriendedUndeadMob}/
	 */
	public static boolean isSunSensitive(IBefriendedMob mobUndead)
	{
		if (mobUndead instanceof IBefriendedUndeadMob u)
		{
			return !u.isSunImmune() && !mobUndead.asMob().hasEffect(MobEffects.FIRE_RESISTANCE);
		}
		else
		{
			LogUtils.getLogger().error("DwmgEntityHelper#isSunSafe: mob to test must implement IBefriendedUndeadMob.");
			return false;
		}
	}

	public static void sendCriticalParticlesToLivingDefault(LivingEntity entity, float heightOffset, int amount)
	{
		EntityHelper.sendParticlesToEntity(entity, ParticleTypes.CRIT, entity.getBbHeight() - 0.2 + heightOffset, 0.5d, amount, 0.1d);
	}
	
}
