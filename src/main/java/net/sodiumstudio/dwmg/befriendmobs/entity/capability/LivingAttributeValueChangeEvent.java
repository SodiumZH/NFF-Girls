package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.Event;

public class LivingAttributeValueChangeEvent extends Event {

	public final LivingEntity entity;
	public final Attribute attribute;
	public final double oldValue;
	public final double newValue;
	
	public LivingAttributeValueChangeEvent(LivingEntity entity, Attribute attribute,
			double oldValue, double newValue)
	{
		this.entity = entity;
		this.attribute = attribute;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
}
