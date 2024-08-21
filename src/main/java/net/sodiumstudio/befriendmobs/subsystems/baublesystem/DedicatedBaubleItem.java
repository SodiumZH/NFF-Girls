package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.nautils.item.NaUtilsItem;

/**
 * Base class for dedicated items as baubles. It's behaviors can be defined in subclasses and can be directly registered to BaubleSystem.
 */
public abstract class DedicatedBaubleItem extends NaUtilsItem implements IBaubleRegistryEntry
{

	public DedicatedBaubleItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public Item getItem()
	{
		return this;
	}
	
	@Override
	public BiPredicate<Item, ItemStack> getMultiItemCondition() {
		return null;
	}
	
	@Override
	@Nonnull
	public BaubleEquippingCondition getEquippingCondition()
	{
		return BaubleEquippingCondition.always();
	}

	@Override
	public void onEquipped(BaubleProcessingArgs args) {}

	@Override
	public void preSlotTick(BaubleProcessingArgs args) {}

	@Override
	public void postSlotTick(BaubleProcessingArgs args) {}

	@Override
	@Nullable
	public BaubleAttributeModifier[] getNonDuplicatableModifiers(Mob mob) {return null;}
	
}
