package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import java.util.HashMap;
import java.util.UUID;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;

public class BaubleEffectTable
{
	protected HashMap<Item, Properties> map = new HashMap<Item, Properties>();
	
	
	
	public BaubleEffectTable add(Item item, Attribute attribute, double value, AttributeModifier.Operation operation)
	{
		map.put(item, Properties.of(attribute, value, operation));
		return this;
	}
	
	public BaubleEffectTable add(Item item, Attribute attribute, double value, AttributeModifier.Operation operation, boolean canDuplicate)
	{
		Properties p = Properties.of(attribute, value, operation);
		p.canDuplicate = canDuplicate;
		map.put(item, p);
		return this;
	}

	public double getValue(Item item)
	{
		return map.get(item).value;
	}
	
	public Attribute getAttribute(Item item)
	{
		return map.get(item).attribute;
	}
	
	public AttributeModifier.Operation getOperation(Item item)
	{
		return map.get(item).operation;
	}
	
	public boolean canDuplicate(Item item)
	{
		return map.get(item).canDuplicate;
	}
	
	public AttributeModifier makeModifier(Item item, UUID uuid, String key)
	{
		return new AttributeModifier(uuid, key, map.get(item).value, map.get(item).operation);
	}
	
	public static class Properties{
		public double value;
		public AttributeModifier.Operation operation;
		public Attribute attribute;
		public boolean canDuplicate = false;
		private Properties(Attribute attribute, double value, AttributeModifier.Operation operation)
		{
			this.attribute = attribute;
			this.value = value;
			this.operation = operation;
		}
		public static Properties of(Attribute attribute, double value, AttributeModifier.Operation operation)
		{
			return new Properties(attribute, value, operation);
		}
		
		public Properties allowDuplication()
		{
			canDuplicate = true;
			return this;
		}
	}
}
