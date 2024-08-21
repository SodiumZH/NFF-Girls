package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A {@code BaubleBehavior} is an {@link IBaubleRegsitryEntry} implementation that's not defined together with specific {@link Item} class.
 * It's used to define behaviors as baubles for existing items, or add other effects to existing {@link DedicatedBaubleItem}s.
 */
public abstract class BaubleBehavior implements IBaubleRegistryEntry
{

	protected final ResourceLocation key;
	@Nullable protected final Item item;
	@Nullable protected final BiPredicate<Item, ItemStack> multiItemCondition;
	protected final BaubleEquippingCondition equippingCondition;
	protected double priority = 1d;
	protected boolean blockLowerPriorities = false;
	
	
	protected BaubleBehavior(ResourceLocation key, 
			@Nullable Item item, @Nullable BiPredicate<Item, ItemStack> multiItemCondition, 
			BaubleEquippingCondition equippingCondition)
	{
		if ((item == null && multiItemCondition == null) || (item != null && multiItemCondition != null))
			throw new IllegalArgumentException("BaubleBehavior: item and multi-item condition must be either but not both non-null.");
		this.item = item;
		this.multiItemCondition = multiItemCondition;
		this.key = key;
		this.equippingCondition = equippingCondition;
	}
	
	/**
	 * @param Item Item type taking this BaubleBehavior.
	 * @param key Key in registry. Must be unique.
	 * @param equippingCondition Condition if a slot for a mob can equip this bauble.
	 */
	public BaubleBehavior(@Nonnull Item item, ResourceLocation key, 
			BaubleEquippingCondition equippingCondition)
	{
		this(key, item, null, equippingCondition);
	}
	
	/**
	 * @param condition Condition to check if an ItemStack should take this BaubleBehavior.
	 * @param key Key in registry. Must be unique.
	 * @param equippingCondition Condition if a slot for a mob can equip this bauble.
	 */
	public BaubleBehavior(@Nonnull BiPredicate<Item, ItemStack> condition, 
			ResourceLocation key, BaubleEquippingCondition equippingCondition)
	{
		this(key, null, condition, equippingCondition);
	}
	
	/**
	 * Set the bauble effect priority.
	 * @see {@link IBaubleRegistryEntry#getPriority}
	 * @return this.
	 */
	public BaubleBehavior setPriority(double value)
	{
		if (this.getItem() == null)
			throw new UnsupportedOperationException("BaubleBehavior: priority is applicable only on item-specific entries.");
		this.priority = value;
		return this;
	}
	
	/**
	 * Set if the bauble effect should block effects of lower priorities.
	 * @see {@link IBaubleRegistryEntry#shouldBlockLowerPriorities()}
	 * @return this.
	 */
	public BaubleBehavior setBlockLowerPriorities(boolean value)
	{
		if (this.getItem() == null)
			throw new UnsupportedOperationException("BaubleBehavior: priority is applicable only on item-specific entries.");
		this.blockLowerPriorities = value;
		return this;
	}
	
	@Override
	public final double getPriority()
	{
		return priority;
	}

	@Override
	public final boolean shouldBlockLowerPriorities()
	{
		return blockLowerPriorities;
	}
	
	@Override
	public final ResourceLocation getBaubleRegistryKey() {
		return key;
	}

	@Override
	public final Item getItem() {
		return item;
	}

	@Override
	public final BiPredicate<Item, ItemStack> getMultiItemCondition() {
		return multiItemCondition;
	}

	@Override
	public final BaubleEquippingCondition getEquippingCondition() {
		return equippingCondition;
	}

	
}
