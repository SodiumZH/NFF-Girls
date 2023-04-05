package net.sodiumstudio.dwmg.befriendmobs.item.baublesystem;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IBaubleHolder {

	public HashSet<ItemStack> getBaubleStacks();


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
		getBaubleHandler().updateBaubleEffects(this);
	}
	
	/* Dynamic modifier management */
	
	// The modifier map reference for dynamic modifier management.
	// Simply create an empty hash map and link to this function
	public HashMap<AttributeModifier, Attribute> getExistingBaubleModifiers();
	
	// Remove all modifiers
	public default void clearBaubleModifiers()
	{
		for(AttributeModifier mod: getExistingBaubleModifiers().keySet())
		{
			AttributeInstance ins = getliving().getAttribute(getExistingBaubleModifiers().get(mod));
			ins.removeModifier(mod);
		}
		getExistingBaubleModifiers().clear();
	}

	// Add a bauble attribute modifier using existing modifier preset.
	public default void addBaubleModifier(Attribute attribute, double value, AttributeModifier.Operation operation)
	{
		AttributeModifier modifier = new AttributeModifier(UUID.randomUUID(), UUID.randomUUID().toString(), value, operation);
		this.getExistingBaubleModifiers().put(modifier, attribute);
		AttributeInstance ins = getliving().getAttribute(attribute);
		ins.addTransientModifier(modifier);
	}
	
	// Add a bauble attribute modifier using existing modifier preset.
	// Note: the modifier added is freshly created, not the existing preset modifier.
	public default void addBaubleModifier(Attribute attribute, AttributeModifier modifier)
	{
		addBaubleModifier(attribute, modifier.getAmount(), modifier.getOperation());
	}	
	
	
	
	
	/* Misc */	
	public default LivingEntity getliving()
	{
		return (LivingEntity)this;
	}
	
	
	
	
}
