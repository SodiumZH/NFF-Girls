package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

/**
 * Arguments for bauble processing methods for handling bauble behaviors.
 * @param baubleItemStack The specific ItemStack in process.
 * @param user The mob using this processing method.
 * @param slotKey Current bauble slot key of the ItemStack being processed.
 */
public record BaubleProcessingArgs(ItemStack baubleItemStack, Mob user, String slotKey)
{
	/**
	 * Identical to constructor for simplification.
	 */
	public BaubleProcessingArgs(ItemStack baubleItemStack, CBaubleEquippableMob user, String slotKey)
	{
		this(baubleItemStack, user.getMob(), slotKey);
	}
	
	CBaubleEquippableMob getCapability()
	{
		return CBaubleEquippableMob.getCapability(user);
	}
}
