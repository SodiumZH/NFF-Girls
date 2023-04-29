package net.sodiumstudio.dwmg.befriendmobs.item.baublesystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.ContainerHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;

public interface IBaubleHolder {
	
	public HashMap<String, ItemStack> getBaubleSlots();

	public BaubleHandler getBaubleHandler();
	
	/* Bauble existance & counting */
		
	public default boolean isBauble(Item item){
		return getBaubleHandler().isAccepted(item);
	}
	
	public default boolean isBauble(ItemStack stack){
		return getBaubleHandler().isAccepted(stack);
	}

	/* Apply effects */
	public default void updateBaubleEffects()
	{
		getBaubleHandler().tick(this);
	}
	
	/* Dynamic modifier management */

	public static class TransientModifierInfo
	{
		public final String slotKey;
		public final String modifierKey;
		public final Attribute attribute;
		public final AttributeModifier modifier;
		public boolean alwaysUpdate = false;
		
		public TransientModifierInfo(String slotKey, String modifierKey, Attribute attribute, AttributeModifier modifier)
		{
			this.slotKey = slotKey;
			this.modifierKey = modifierKey;
			this.attribute = attribute;
			this.modifier = modifier;
		}		
		
		public TransientModifierInfo alwaysUpdate()
		{
			this.alwaysUpdate = true;
			return this;
		}
		
	}
	
	// The modifier set reference for dynamic modifier management.
	public default HashSet<TransientModifierInfo> transientModifiers()
	{
		return getDataCache().transientModifiers();
	}
	
	// Remove all modifiers
	public default void clearBaubleModifiers()
	{
		for (TransientModifierInfo tmi: transientModifiers())
		{
			AttributeInstance ins = getLiving().getAttribute(tmi.attribute);
			ins.removeModifier(tmi.modifier);
		}
		transientModifiers().clear();
	}

	/** Add a bauble attribute modifier using existing modifier preset.
	* @param slotKey String key for the bauble slot (item stack).
	* @param modifierKey String key for the specific attribute modifier under the slot.
	*/
	public default void addBaubleModifier(String slotKey, String modifierKey, Attribute attribute, double value, AttributeModifier.Operation operation)
	{
		if (containsModifier(slotKey, modifierKey))
			throw new IllegalArgumentException("Add Bauble Modifier: duplicate modifier key.");
		AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), UUID.randomUUID().toString(), value, operation);
		this.transientModifiers().add(new TransientModifierInfo(slotKey, modifierKey, attribute, modifier));
		AttributeInstance ins = getLiving().getAttribute(attribute);
		ins.addTransientModifier(modifier);
	}
	
	/** Add a bauble attribute modifier using existing modifier preset.
	* @param slotKey String key for the bauble slot (item stack).
	* @param modifierKey String key for the specific attribute modifier under the slot.
	*/
	public default void addBaubleModifier(String slotKey, String modifierKey, Attribute attribute, AttributeModifier modifier)
	{
		addBaubleModifier(slotKey, modifierKey, attribute, modifier.getAmount(), modifier.getOperation());
	}	
	
	/** 
	 * Remove all modifiers under a slot of given key.
	 */
	public default void removeBaubleModifiers(String slotKey)
	{
		HashSet<TransientModifierInfo> toRemove = new HashSet<TransientModifierInfo>();
		for (TransientModifierInfo tmi: transientModifiers())
		{
			if (tmi.slotKey.equals(slotKey))
			{
				AttributeInstance ins = getLiving().getAttribute(tmi.attribute);
				ins.removeModifier(tmi.modifier);
				toRemove.add(tmi);
			}
		}
		for (TransientModifierInfo tmi: toRemove)
		{
			transientModifiers().remove(tmi);
		}
	}
	
	/**
	 * Remove 
	 */
	public default void removeBaubleModifier(String slotKey, String modifierKey)
	{
		HashSet<TransientModifierInfo> toRemove = new HashSet<TransientModifierInfo>();
		for (TransientModifierInfo tmi: transientModifiers())
		{
			if (tmi.slotKey.equals(slotKey) && tmi.modifierKey.equals(modifierKey))
			{
				AttributeInstance ins = getLiving().getAttribute(tmi.attribute);
				ins.removeModifier(tmi.modifier);
				toRemove.add(tmi);
			}
		}
		for (TransientModifierInfo tmi: toRemove)
		{
			transientModifiers().remove(tmi);
		}
	}
	
	/* Data Cache and Change Detection */
	
	public default CBaubleDataCache getDataCache()
	{
		Wrapped<CBaubleDataCache> cache = new Wrapped<CBaubleDataCache>(null);
		getLiving().getCapability(BefMobCapabilities.CAP_BAUBLE_DATA_CACHE).ifPresent((cap) -> 
		{
			cache.set(cap);
		});
		if (cache.get() == null)
			throw new IllegalStateException("IBaubleHolder missing capability CBaubleDataCache.");
		return cache.get();
	}
	
	public default boolean hasSlotChanged(String slotKey)
	{
		return getDataCache().hasSlotChanged(slotKey);
	}
	
	public default void saveDataCache()
	{
		getDataCache().write();
	}
	
	
	
	/* Misc */	
	public default LivingEntity getLiving()
	{
		return (LivingEntity)this;
	}

	/* Util */
	
	/**
	 * Check if there's at least one slot containing the given item.
	 * 
	 */
	public default boolean hasBaubleItem(Item inItem)
	{
		for (String key: getBaubleSlots().keySet())
		{
			if (getBaubleSlots().get(key).is(inItem))
				return true;
		}
		return false;
	}
	
	/**
	 * Check if it contains a modifier with given slot key and given modifier key.
	 */
	public default boolean containsModifier(String slotKey, String modifierKey)
	{
		for (TransientModifierInfo tmi: transientModifiers())
		{
			if (tmi.slotKey.equals(slotKey) && tmi.modifierKey.equals(modifierKey))
				return true;
		}
		return false;
	}
	
	/**
	 * Check if it contains a modifier with given slot key.
	 */
	public default boolean containsModifier(String slotKey)
	{
		for (TransientModifierInfo tmi: transientModifiers())
		{
			if (tmi.slotKey.equals(slotKey))
				return true;
		}
		return false;
	}
	
}
