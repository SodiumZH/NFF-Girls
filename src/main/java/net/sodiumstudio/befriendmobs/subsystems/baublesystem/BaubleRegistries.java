package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.Wrapped;
import net.sodiumstudio.nautils.containers.Tuple3;
import net.sodiumstudio.nautils.containers.Tuple4;
import net.sodiumstudio.nautils.exceptions.DuplicatedRegistryEntryException;
import net.sodiumstudio.nautils.exceptions.UnimplementedException;

class BaubleRegistries
{
	// ========== Bauble Registry =================
	
	/**
	 * Registry in which entries are directly registered. 
	 * After registering, the raw registry will be cleared into Simple Bauble Registry
	 * and Multiple Bauble Registry, which are accessed by the Bauble System in mechanisms.
	 * <p>The key is the entry key and the value is a tuple with the entry instance and an instance of equipping condition
	 * accessed from the corresponding {@link IBaubleRegistryEntry#getEquippingCondition()}.
	 * Since the overriding condition supplier (i.e. {@link IBaubleRegistryEntry#getEquippingCondition()}) may have different
	 * forms, including providing static references or creating new instances,
	 * it will be instantiated here and won't be modified to prevent accident modification of static stuffs,
	 * and when clearing the registry, new depending {@link BaubleEquippingCondition}s will be created to allow modifications.
	 */
	static final HashMap<ResourceLocation, Tuple<IBaubleRegistryEntry, BaubleEquippingCondition>> 
		RAW_REGISTRY = new HashMap<>();
	
	/**
	 * Register an entry into the direct registry. Only called in {@link RegisterBaubleEvent}.
	 */
	static void registerBaubleRaw(IBaubleRegistryEntry entry)
	{
		if (RAW_REGISTRY.containsKey(entry.getBaubleRegistryKey()))
			throw new DuplicatedRegistryEntryException("Registering bauble: duplicated entry \"" + entry.getBaubleRegistryKey().toString() + "\"");
		RAW_REGISTRY.put(entry.getBaubleRegistryKey(), new Tuple<>(entry, entry.getEquippingCondition()));
	}
	
	// ========== Cleared bauble registries (read by bauble system mechanisms) ===================
	
	/**
	 * Cleared registry for specific bauble items, 
	 * i.e. {@link IBaubleRegistry}s of which {@code getItem} returns non-null. 
	 * <p>Values: priority, key, entry, equipping condition (can be modified).
	 */
	static final HashMap<Item, 
		ArrayList<Tuple4<Double, ResourceLocation, IBaubleRegistryEntry, BaubleEquippingCondition>>> SINGLE_REGISTRY = new HashMap<>();
	
	/**
	 * Bauble effects for multiple bauble items that are defined by predicates.
	 * <p>Values: definition condition, key, entry, equipping condition (can be modified).
	 */
	static final HashSet<Tuple4<BiPredicate<Item, ItemStack>, ResourceLocation, IBaubleRegistryEntry, BaubleEquippingCondition>> 
		MULTIPLE_REGISTRY = new HashSet<>();
	
	/**
	 * Clear and fill registries from the raw registry. 
	 */
	static void clearAndFillRegistries()
	{
		// Merge raw registry into simple and multiple registries
		for (ResourceLocation key: RAW_REGISTRY.keySet())
		{
			IBaubleRegistryEntry entry = RAW_REGISTRY.get(key).getA();
			if (RAW_REGISTRY.get(key).getA().getItem() != null)
			{
				Item item = entry.getItem();
				if (!SINGLE_REGISTRY.containsKey(item))
					SINGLE_REGISTRY.put(item, new ArrayList<>());
				SINGLE_REGISTRY.get(item).add(new Tuple4<>(entry.getPriority(), key, entry, RAW_REGISTRY.get(key).getB().createDependent()));
			}
			else
			{
				MULTIPLE_REGISTRY.add(new Tuple4<>(RAW_REGISTRY.get(key).getA().getMultiItemCondition(), key, entry, RAW_REGISTRY.get(key).getB().createDependent()));
			}
		}
		sortSingleRegistry();
	}
	
	/**
	 * Sort the simple registry by priority.
	 */
	static void sortSingleRegistry()
	{
		for (Item item: SINGLE_REGISTRY.keySet())
		{
			ToDoubleFunction<Tuple4<Double, ResourceLocation, IBaubleRegistryEntry, BaubleEquippingCondition>> func = t -> t.a;
			var sorted = SINGLE_REGISTRY.get(item).stream().sorted(Comparator.comparingDouble(func).reversed()).toList();
			SINGLE_REGISTRY.put(item, NaContainerUtils.toArrayList(sorted));
		}
	}
	
	// ======= Registry browsing =================================

	/**
	 * Do an operation to all bauble registry entries that should take effect to a given slot of a given mob.
	 */
	static void forEachMatchedEntry(CBaubleEquippableMob mob, String slot, Consumer<IBaubleRegistryEntry> operationForEach)
	{
		ItemStack stack = mob.getBaubleSlotAccessor().getItemStack(slot);
		if (stack.getItem() == null)
			return;
		// For a special slot, just get all its accepting entries, test and invoke operation
		if (BaubleEquippableMobRegistries.isSlotSpecial(mob.getMob(), slot))
		{
			for (IBaubleRegistryEntry entry: BaubleEquippableMobRegistries.getSpecialSlotAcceptedEntries(mob.getMob(), slot))
			{
				if (!entry.isMulti())
				{
					if (stack.getItem().equals(entry.getItem()) && getSingleRegistryRecord(entry).d.test(new BaubleProcessingArgs(stack, mob, slot)))
						operationForEach.accept(entry);
				}
				else
				{
					if (entry.getMultiItemCondition().test(stack.getItem(), stack) && getMultiRegistryRecord(entry).d.test(new BaubleProcessingArgs(stack, mob, slot)))
						operationForEach.accept(entry);
				}
			}
		}
		// For a general slot, browse all entries from registries, test the item and invoke
		else
		{
			var singleEntries = SINGLE_REGISTRY.get(stack.getItem());
			if (singleEntries != null)
			{
				for (int i = 0; i < singleEntries.size(); ++i)
				{
					if (singleEntries.get(i).d.test(new BaubleProcessingArgs(stack, mob, slot)))
					{
						operationForEach.accept(singleEntries.get(i).c);
						if (singleEntries.get(i).c.shouldBlockLowerPriorities())
							break;
					}
				}
			}
			for (var entry: MULTIPLE_REGISTRY)
			{
				if (entry.a.test(stack.getItem(), stack) && entry.d.test(new BaubleProcessingArgs(stack, mob, slot)))
				{
					operationForEach.accept(entry.c);
				}
			}
		}
	}
	
	/**
	 * Get all entries which the input ItemStack is in the definitions.
	 */
	static Set<IBaubleRegistryEntry> getAllRelatedEntries(ItemStack itemstack)
	{
		Set<IBaubleRegistryEntry> res = new HashSet<>();
		if (itemstack.getItem() == null) return res;
		if (SINGLE_REGISTRY.containsKey(itemstack.getItem()))
		{
			for (var entry: SINGLE_REGISTRY.get(itemstack.getItem()))
			{
				res.add(entry.c);
			}
		}
		for (var entry: MULTIPLE_REGISTRY)
		{
			if (entry.a.test(itemstack.getItem(), itemstack))
				res.add(entry.c);
		}
		return res;
	}
	
	/**
	 * Test if an itemstack is in the definition of the entry.
	 */
	static boolean isRelatedEntry(ItemStack stack, IBaubleRegistryEntry entry)
	{
		if (stack.getItem() == null) return false;
		if (entry.getItem() != null)
			return stack.getItem().equals(entry.getItem());
		else return entry.getMultiItemCondition().test(stack.getItem(), stack);
	}

	/**
	 * Check if an ItemStack can be equipped on a given mob into a given slot.
	 */
	static boolean canEquipOn(ItemStack itemstack, Mob mob, String slot)
	{
		if (itemstack.getItem() == null) return false;
		Wrapped<Boolean> res = new Wrapped<>(false);
		mob.getCapability(BaubleSystemCapabilities.CAP_BAUBLE_EQUIPPABLE_MOB).ifPresent(cap -> {
			if (cap.isValid())
			{
				if (!cap.getBaubleSlotAccessor().getAccessors().containsKey(slot)) return;
				// For a special slot, just get all entries it accepts and if the itemstack matches one it's equippable
				if (BaubleEquippableMobRegistries.isSlotSpecial(mob, slot))
				{
					for (IBaubleRegistryEntry entry: BaubleEquippableMobRegistries.getSpecialSlotAcceptedEntries(mob, slot))
					{
						if (isRelatedEntry(itemstack, entry) && entry.getEquippingCondition().test(new BaubleProcessingArgs(itemstack, cap, slot)))
						{
							res.set(true);
							return;
						}
					}
				}
				// For non-special slot, browse the registry to get related entries and test if it can equip
				else
				{
					for (IBaubleRegistryEntry entry: getAllRelatedEntries(itemstack))
					{
						if (entry.getEquippingCondition().test(new BaubleProcessingArgs(itemstack, cap, slot)))
						{
							res.set(true);
							return;
						}
					}
				}
			}
		});
		return res.get();
	}
	
	/**
	 * Get an entry from key. Null if not found.
	 */
	@Nullable
	static IBaubleRegistryEntry getEntryByKey(ResourceLocation key)
	{
		if (RAW_REGISTRY.containsKey(key))
			return RAW_REGISTRY.get(key).getA();
		else return null;
	}
	
	/**
	 * Get an entry from key (using ResourceLocation format). Null if not found.
	 */
	@Nullable
	static IBaubleRegistryEntry getEntryByKey(String key)
	{
		ResourceLocation loc = new ResourceLocation(key);
		if (RAW_REGISTRY.containsKey(loc))
			return RAW_REGISTRY.get(loc).getA();
		else return null;
	}
	
	/**
	 * Get the whole registry record (i.e. registry value tuple) from single-item registry for a given IBaubleRegistryEntry object.
	 */
	static Tuple4<Double, ResourceLocation, IBaubleRegistryEntry, BaubleEquippingCondition> getSingleRegistryRecord(IBaubleRegistryEntry entry)
	{
		if (entry.isMulti())
			throw new IllegalArgumentException("The entry isn't a single-item entry.");
		for (var rec: SINGLE_REGISTRY.get(entry.getItem()))
		{
			if (rec.c == entry)
				return rec;
		}
		throw new IllegalArgumentException("The entry record doesn't exist.");
	}
	
	/**
	 * Get the whole registry record (i.e. registry value tuple) from multi-item registry for a given IBaubleRegistryEntry object.
	 */
	static Tuple4<BiPredicate<Item, ItemStack>, ResourceLocation, IBaubleRegistryEntry, BaubleEquippingCondition> getMultiRegistryRecord(IBaubleRegistryEntry entry)
	{
		if (!entry.isMulti())
			throw new IllegalArgumentException("The entry isn't a multi-item entry.");
		for (var rec: MULTIPLE_REGISTRY)
		{
			if (rec.c == entry)
				return rec;
		}
		throw new IllegalArgumentException("The entry record doesn't exist.");
	}
}
