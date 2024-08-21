package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.nautils.annotation.DontOverride;

/**
 * {@code IBaubleRegistryEntry} is the common interface of all classes that can be used to register a bauble.
 */
public interface IBaubleRegistryEntry
{

	/**
	 * ResourceLocation as a unique identifier of this entry.
	 */
	public ResourceLocation getBaubleRegistryKey();
	
	/**
	 * The item used as the bauble.
	 * <p> If using {@code getMultiItemCondition}, set this null, otherwise it won't work.
	 */
	@Nullable
	public Item getItem();

	/**
	 * Modify this <b>ONLY</b> when trying to add a series of items (defined by predicate, e.g. all pickaxes) as bauble.
	 * <p>Every such entry will increase the resource cost <b>on tick</b> because the condition check must be performed every tick on every item,
	 * so keep it null unless necessary.
	 */
	@Nullable
	public BiPredicate<Item, ItemStack> getMultiItemCondition();
	
	/**
	 * Get the priority of applying this effect. Only for single-item entries.
	 * <p>If this entry is for single item (i.e. {@code getItem} returns non-null), it will
	 * try to apply the effect from the highest priority to the lowest. If the priorities collide,
	 * the applying order will be random.
	 * <p>If this entry is for multiple item (i.e. {@code getItem} returns null and {@code getMultiItemCondition} returns non-null),
	 * it will be ignored.
	 */
	public default double getPriority()
	{
		return 1d;
	}
	
	/**
	 * If true, this entry will block the effects of entries of lower priority for the same item.
	 * The priority is defined in {@code getPriority}.
	 * <p>Note: This is only for single-item entries. If this entry is for multiple item (i.e. {@code getItem} returns null 
	 * and {@code getMultiItemCondition} returns non-null), it will be ignored.
	 */
	public default boolean shouldBlockLowerPriorities()
	{
		return false;
	}
	
	/**
	 * The overall condition if this bauble should be equippable and apply effect.
	 */
	@Nonnull
	public BaubleEquippingCondition getEquippingCondition();
	
	/**
	 * Invoked on bauble equipped.
	 */
	public void onEquipped(BaubleProcessingArgs args);
	
	// Tick related
	
	/**
	 * Invoked once before the slots start ticking if this bauble is present, despite the amount.
	 * Note: ItemStack in args is invalid and attempt to access it will cause exception.
	 */
	public void preSlotTick(BaubleProcessingArgs args);
	
	/**
	 * Invoked once before the slots end ticking if this bauble is present, despite the amount.
	 * Note: ItemStack in args is invalid and attempt to access it will cause exception.
	 */
	public void postSlotTick(BaubleProcessingArgs args);
	
	/**
	 * Invoked each time for each slot containing this bauble.
	 */
	public void slotTick(BaubleProcessingArgs args);
	
	//**
	// * If true, it will invoke {@code preSlotTick} and {@code postSlotTick} for each different ItemStack for the same item.
	// */
	//public boolean shouldPreAndPostTickForDifferentStacks();
	
	
	// Modifier management
	
	/**
	 * Getter of {@link BaubleAttributeModifier}s that should be added once no matter how many this bauble is equipped.
	 */
	@Nullable
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob);
	
	/**
	 * Getter of {@link BaubleAttributeModifier}s that should be added each this bauble is added.
	 */
	@Nullable
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args);
	
	// Utilities
	
	/**
	 * Check if this entry is for multi-item.
	 */
	@DontOverride
	public default boolean isMulti()
	{
		if (this.getItem() == null && this.getMultiItemCondition() == null || this.getItem() != null && this.getMultiItemCondition() != null)
			throw new IllegalStateException("IBaubleRegistryEntry: illegal entry \"" + getBaubleRegistryKey().toString()
					+ " found. For a valid entry, getItem() and getMultiItemCondition() must be either but not both non-null.");
		return this.getItem() == null && this.getMultiItemCondition() != null;
	}
}
