package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.nautils.containers.NaUtilsImmutableMap;

/**
 * An accessor for dynamically getting current bauble ItemStacks from slots.
 * Each mob has one instance.
 * <b>Any API-calling will trigger refresh, so don't worry if it's up-to-date.
 */
public class BaubleSlotAccessor
{
	private final CBaubleEquippableMob owner;
	private Map<String, Function<Mob, ItemStack>> accessorsCache = new HashMap<>();
	private Map<String, ItemStack> itemsCache = new HashMap<>();
	
	BaubleSlotAccessor(CBaubleEquippableMob owner)
	{
		this.owner = owner;
		this.accessorsCache = BaubleEquippableMobRegistries.getAllAccessors(owner.getMob());
	}
	
	/** Returns an empty accessor not doing anything. */
	static BaubleSlotAccessor empty(CBaubleEquippableMob owner)
	{
		BaubleSlotAccessor empty = new BaubleSlotAccessor(owner)
				{
					@Override
					void refresh() {}
				};
		empty.accessorsCache.clear();
		return empty;
	}
	
	void refresh()
	{
		this.accessorsCache = BaubleEquippableMobRegistries.getAllAccessors(owner.getMob());
		this.itemsCache.clear();
		for (var entry: accessorsCache.entrySet())
		{
			itemsCache.put(entry.getKey(), entry.getValue().apply(owner.getMob()));
		}
	}
	
	/**
	 * Get a copy of accessors. The output map is immutable.
	 * @return
	 */
	public Map<String, Function<Mob, ItemStack>> getAccessors()
	{
		refresh();
		return new NaUtilsImmutableMap<>(accessorsCache);
	}
	
	/**
	 * Get a copy of current ItemStacks. The output map is immutable.
	 * <p>Note: output ItemStacks are copies. Modifying them won't do anything to actual ItemStacks.
	 * @return
	 */
	public Map<String, ItemStack> getItemStacks()
	{
		refresh();
		HashMap<String, ItemStack> map = new HashMap<>();
		for (var entry: itemsCache.entrySet())
			map.put(entry.getKey(), entry.getValue());
		return new NaUtilsImmutableMap<>(map);
	}
	
	/**
	 * Get a copy of ItemStack on given slot.
	 * <p>Note: output is a copy. Modifying it won't do anything to the actual ItemStack.
	 */
	public ItemStack getItemStack(String key)
	{
		refresh();
		return itemsCache.containsKey(key) ? itemsCache.get(key).copy() : ItemStack.EMPTY;
	}
	
	/**
	 * Check if the mob has the given bauble slot now.
	 */
	public boolean hasSlot(String key)
	{
		refresh();
		return accessorsCache.containsKey(key);
	}
}
