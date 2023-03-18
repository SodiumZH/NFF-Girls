package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import java.util.ArrayList;
import java.util.UUID;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

public class BaubleEffectManager
{
	protected BaubleEffectTable table;
	protected String identifier;
	protected ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	protected ArrayList<AttributeModifier> modifiers = new ArrayList<AttributeModifier>();
	
	public BaubleEffectManager(BaubleEffectTable table)
	{
		this.table = table;
		this.identifier = UUID.randomUUID().toString();
	}
	
	
	public BaubleEffectManager apply(LivingEntity living, ItemStack bauble)
	{
		if (living.level != null && !living.level.isClientSide)
		{
			AttributeModifier modifier = new AttributeModifier(
					UUID.randomUUID(),
					identifier + Integer.toString(modifiers.size()),
					table.getValue(bauble.getItem()),
					table.getOperation(bauble.getItem()));
			modifiers.add(modifier);
			attributes.add(table.getAttribute(bauble.getItem()));
			AttributeInstance instance = living.getAttributes().getInstance(table.getAttribute(bauble.getItem()));
			instance.addTransientModifier(modifier);
		}
		return this;
	}
	
	public BaubleEffectManager clear(LivingEntity living)
	{
		if (living.level != null && !living.level.isClientSide)
		{
			if (attributes.size() != modifiers.size())
				throw new RuntimeException();
			for (int i = 0; i < attributes.size(); ++i) {
				AttributeInstance instance = living.getAttributes().getInstance(attributes.get(i));
				instance.removeModifier(modifiers.get(i));
			}
		}
		attributes.clear();
		modifiers.clear();
		return this;
	}
}
