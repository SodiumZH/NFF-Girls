package net.sodiumstudio.befriendmobs.entity.capability.wrapper;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.sodiumstudio.befriendmobs.entity.capability.CAttributeMonitor;
import net.sodiumstudio.befriendmobs.registry.BMCaps;

/**
 * A wrapper interface of {@link CAttributeMonitor}. Living Entities implementing this interface will be automatically 
 * attached {@link CAttributeMonitor} capability.
 */
public interface IAttributeMonitor
{	
	
	public default void addAttribute(Attribute attr)
	{
		((LivingEntity)this).getCapability(BMCaps.CAP_ATTRIBUTE_MONITOR).ifPresent(cap -> cap.listen(attr));
	}
	
	public void onAttributeChange(Attribute attr, double oldVal, double newVal);
	
}
