package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.eventbus.api.Event;

// Fired on living start setting listening to attribute changes.
// Add attributes to listen in this event
public class LivingSetupAttributeMonitorEvent extends Event
{
	public final LivingEntity living;
	public final CAttributeMonitorProvider provider;
	public LivingSetupAttributeMonitorEvent(LivingEntity living, CAttributeMonitorProvider provider)
	{
		this.living = living;
		this.provider = provider;
	}
	
	public void addListen(Attribute attr)
	{
		provider.listen(attr);
	}	
}
