package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

/**
 * BefriendMobs - BaubleSystem is a system for equipping "bauble" items on <b>mobs</b> just like for players in Curios API.
 * <p>This class involves some common static methods.
 */
public class BaubleSystem
{
	/**
	 * Get the Bauble-handling capability of a mob.
	 * @return Bauble-handling capability ({@link CBaubleEquippableMob}).
	 * If the mob isn't bauble-equippable (not registered or is pending removal, etc.), return an empty instance that won't do anything. 
	 */
	@Nonnull
	static CBaubleEquippableMob getCapability(Mob mob)
	{
		return CBaubleEquippableMob.getCapability(mob);
	}
	
	/**
	 * Check if a mob has the Bauble-handling capability ({@link CBaubleEquippableMob}).
	 */
	public static boolean isCapabilityPresent(Mob mob)
	{
		return getCapability(mob).isValid();
	}
	
	/**
	 * Do something if a mob has the Bauble-handling capability ({@link CBaubleEquippableMob}).
	 */
	public static void ifCapabilityPresent(Mob mob, Runnable action)
	{
		if (isCapabilityPresent(mob))
		{
			action.run();
		}
	}
	
	/**
	 * Get the current ItemStack equipped on the given slot key <b>as a copy</b>. Empty if the slot isn't present.
	 */
	@Nonnull
	public static ItemStack getSlotItem(Mob mob, String slotKey)
	{
		CBaubleEquippableMob cap = getCapability(mob);
		return cap.getBaubleSlotAccessor().hasSlot(slotKey) ? cap.getBaubleSlotAccessor().getItemStack(slotKey) : ItemStack.EMPTY;
	}
	
	/**
	 * Get all slot keys and equipping items.
	 */
	@Nullable
	public static Map<String, ItemStack> getAllSlotItems(Mob mob)
	{
		//Map<String, ItemStack> map = new HashMap<>();
		return getCapability(mob).getBaubleSlotAccessor().getItemStacks();
	}
	
	/**
	 * Do an operation to all bauble registry entries that should take effect to a given slot of a given mob.
	 */
	public static void forEachMatchedEntry(CBaubleEquippableMob mob, String slot, Consumer<IBaubleRegistryEntry> operationForEach)
	{
		BaubleRegistries.forEachMatchedEntry(mob, slot, operationForEach);
	}
	
	/**
	 * Get all entries which the input ItemStack is in the definitions, despite the mob and the slot.
	 */
	public static Set<IBaubleRegistryEntry> getAllRelatedEntries(ItemStack itemstack)
	{
		return BaubleRegistries.getAllRelatedEntries(itemstack);
	}
	
	/**
	 * Check if an ItemStack can be equipped on a given mob into a given slot.
	 */
	public static boolean canEquipOn(ItemStack itemstack, Mob mob, String slot)
	{
		return BaubleRegistries.canEquipOn(itemstack, mob, slot);
	}
}
