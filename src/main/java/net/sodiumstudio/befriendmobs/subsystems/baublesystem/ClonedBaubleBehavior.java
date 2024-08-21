package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * A {@code ClonedBaubleBehavior} is a {@link BaubleBehavior} of which all behaviors are copied from another {@link IBaubleRegistryEntry},
 * but having its own {@code Item} target, equipping condition and registry key.
 * It's used to copy a registry entry behavior to other baubles, or extend a registry entry for other type of equippers that cannot 
 * be defined by extending its {@code BaubleEquippingCondition}.
 */
public class ClonedBaubleBehavior extends BaubleBehavior
{

	private final IBaubleRegistryEntry source;
	
	private ClonedBaubleBehavior(IBaubleRegistryEntry source, ResourceLocation key, 
			@Nullable Item item, @Nullable BiPredicate<Item, ItemStack> multiItemCondition, 
			BaubleEquippingCondition equippingCondition)
	{
		super(key, item, multiItemCondition, equippingCondition);
		this.source = source;
	}

	public static ClonedBaubleBehavior clone(IBaubleRegistryEntry source, 
			@Nonnull Item item, ResourceLocation key, BaubleEquippingCondition equippingCondition)
	{
		return new ClonedBaubleBehavior(source, key, item, null, equippingCondition);
	}
	
	public static ClonedBaubleBehavior clone(IBaubleRegistryEntry source, 
			@Nonnull BiPredicate<Item, ItemStack> condition, ResourceLocation key,
			BaubleEquippingCondition equippingCondition)
	{
		return new ClonedBaubleBehavior(source, key, null, condition, equippingCondition);
	}

	@Override
	public ClonedBaubleBehavior setPriority(double value)
	{
		super.setPriority(value);
		return this;
	}

	@Override
	public ClonedBaubleBehavior setBlockLowerPriorities(boolean value)
	{
		super.setBlockLowerPriorities(value);
		return this;
	}
	
	// All behaviors cloned from source
	
	@Override
	public void onEquipped(BaubleProcessingArgs args) {
		source.onEquipped(args);
	}
	
	@Override
	public void preSlotTick(BaubleProcessingArgs args) {
		source.preSlotTick(args);
	}

	@Override
	public void postSlotTick(BaubleProcessingArgs args) {
		source.postSlotTick(args);
	}

	@Override
	public void slotTick(BaubleProcessingArgs args) {
		source.slotTick(args);
	}

	@Override
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob)
	{
		return source.getNonDuplicatableModifiers(mob);
	}
	
	@Override
	public BaubleAttributeModifier[] getDuplicatableModifiers(BaubleProcessingArgs args) {
		return source.getDuplicatableModifiers(args);
	}


}
