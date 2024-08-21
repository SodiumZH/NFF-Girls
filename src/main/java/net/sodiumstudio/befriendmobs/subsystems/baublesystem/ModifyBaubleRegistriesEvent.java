package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;

import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.sodiumstudio.nautils.containers.Tuple4;
/**
 * Fired after {@link RegisterBaublesEvent} and bauble registry clear-up, allowing to modify bauble behaviors after registration.
 */
public class ModifyBaubleRegistriesEvent extends Event implements IModBusEvent
{
	/** Get entry from the Single Bauble Registry (i.e. baubles for specific items). Null if not found. */
	@Nullable
	private static Tuple4<Double, ResourceLocation, IBaubleRegistryEntry, BaubleEquippingCondition> getSingleEntry(ResourceLocation key)
	{
		for (Item item: BaubleRegistries.SINGLE_REGISTRY.keySet())
		{
			for (var entry: BaubleRegistries.SINGLE_REGISTRY.get(item))
			{
				if (entry.b.equals(key))
					return entry;
			}
		}
		return null;
	}
	
	/** Get entry from the Multiple Bauble Registry (i.e. baubles defined by predicates). Null if not found. */
	@Nullable
	private static Tuple4<BiPredicate<Item, ItemStack>, ResourceLocation, IBaubleRegistryEntry, BaubleEquippingCondition> getMultipleEntry(ResourceLocation key)
	{
		for (var entry: BaubleRegistries.MULTIPLE_REGISTRY)
		{
			if (entry.b.equals(key))
				return entry;
		}
		return null;
	}
	
	/**
	 * Modify a single-item bauble registry entry's priority by operator (current value dependent).
	 */
	public void modifyPriority(ResourceLocation entryKey, UnaryOperator<Double> operator)
	{
		var entry = getSingleEntry(entryKey);
		if (entry == null)
		{
			LogUtils.getLogger().warn("BM Bauble System: Single Bauble Entry \"" + entryKey.toString() + "\" not found. Modification is skipped.");
			return;
		}
		entry.a = operator.apply(entry.a);
	}
	
	/**
	 * Modify a single-item bauble registry entry's priority to a fixed value.
	 */
	public void modifyPriority(ResourceLocation entryKey, double value)
	{
		this.modifyPriority(entryKey, v -> value);
	}
	
	/**
	 * Modify a bauble registry entry's equipping condition.
	 */
	public void modifyEquippingCondition(ResourceLocation entryKey, Consumer<BaubleEquippingCondition> modification)
	{
		var singleEntry = getSingleEntry(entryKey);
		if (singleEntry != null)
		{
			modification.accept(singleEntry.d);
			return;
		}
		var multipleEntry = getMultipleEntry(entryKey);
		if (multipleEntry != null)
		{
			modification.accept(multipleEntry.d);
			return;
		}
		LogUtils.getLogger().warn("BM Bauble System: Bauble Entry \"" + entryKey.toString() + "\" not found. Modification is skipped.");
	}
	
	/**
	 * Extend a new definition to a multiple-item bauble registry entry. An item will satisfy <b>either</b> the old or new conditions to be defined as this bauble.
	 */
	public void extendMultipleItemDefinition(ResourceLocation entryKey, BiPredicate<Item, ItemStack> extendedDefinition)
	{
		var multipleEntry = getMultipleEntry(entryKey);
		if (multipleEntry != null)
		{
			var old = multipleEntry.a;
			multipleEntry.a = old.or(extendedDefinition);
		}
		else
		{
			LogUtils.getLogger().warn("BM Bauble System: Multiple Bauble Entry \"" + entryKey.toString() + "\" not found. Modification is skipped.");
		}
	}
	
	/**
	 * Shrink the definition of a multiple-item bauble registry entry. An item will satisfy <b>both</b> the old and new conditions to be defined as this bauble.
	 */
	public void shrinkMultipleItemDefinition(ResourceLocation entryKey, BiPredicate<Item, ItemStack> additionalDefinitionCheck)
	{
		var multipleEntry = getMultipleEntry(entryKey);
		if (multipleEntry != null)
		{
			var old = multipleEntry.a;
			multipleEntry.a = old.and(additionalDefinitionCheck);
		}
		else
		{
			LogUtils.getLogger().warn("BM Bauble System: Multiple Bauble Entry \"" + entryKey.toString() + "\" not found. Modification is skipped.");
		}
	}
}
