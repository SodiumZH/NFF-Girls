package net.sodiumstudio.befriendmobs.entity.capability;

import java.util.HashMap;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.capability.wrapper.IAttributeMonitor;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.Wrapped;
import net.sodiumstudio.nautils.annotation.DontCallManually;
import net.sodiumstudio.nautils.annotation.DontOverride;
// A capability which posts LivingAttributeValueChangeEvent when the given attribute value changes.
public interface CAttributeMonitor {

	public LivingEntity getOwner();
	
	/**
	 * Get the listened attribute list
	 * Key: attribute
	 * Value: current attribute value
	 */
	public HashMap<Attribute, Double> getListenList();
	
	/** Add an attribute to the listen list.
	 * It still works if the attribute isn't available yet (e.g. on capability attachment).
	*/
	@SuppressWarnings("deprecation")
	public default CAttributeMonitor listen(Attribute attribute)
	{
		// Use NaN to label an attribute position before entity attributes creation
		double val = getOwner().getAttributes() == null ? Double.NaN : getOwner().getAttributeValue(attribute);
		getListenList().put(attribute, val);
		return this;
	}
	
	// Update and detect change on tick
	@SuppressWarnings("deprecation")
	@DontOverride
	@DontCallManually
	public default void tick()
	{
		for (Attribute attr: getListenList().keySet())
		{
			double oldVal = getListenList().get(attr);
			double newVal;
			if (attr == null)
				newVal = Double.NaN;
			else newVal	= getOwner().getAttributeValue(attr);	
			// NaN indicates the value is not available yet, so don't post event but still update value
			// After attribute is created the value will update to non-NaN
			if (!Double.isNaN(oldVal)
				&& !Double.isNaN(newVal)
				&& (oldVal - newVal > 0.0000001 || oldVal - newVal < -0.0000001))
			{			
				MinecraftForge.EVENT_BUS.post(new ChangeEvent(
						getOwner(), attr, oldVal, newVal));
				if (getOwner() instanceof IAttributeMonitor am)
				{
					am.onAttributeChange(attr, oldVal, newVal);
				}
			}
			getListenList().put(attr, newVal);
		}
	}
	
	/**
	* Add listened attribute to a living entity. 
	* The living entity must have CAttributeMonitor capability attached.
	*/
	@DontOverride
	public static CAttributeMonitor listen(LivingEntity living, Attribute attr)
	{
		Wrapped<CAttributeMonitor> cap = new Wrapped<CAttributeMonitor>(null);
		living.getCapability(BMCaps.CAP_ATTRIBUTE_MONITOR).ifPresent((c) -> 
		{
			cap.set(c);
			c.listen(attr);
		});
		if (cap.get() != null)
			return cap.get();
		else throw new IllegalStateException("Living entity missing attribute monitor capability.");
	}
	
	public static class SetupEvent extends Event
	{
		public final LivingEntity living;
		public final CAttributeMonitor monitor;
		public SetupEvent(LivingEntity living, CAttributeMonitor cap)
		{
			this.living = living;
			this.monitor = cap;
		}
		
		public void addListen(Attribute attr)
		{
			monitor.listen(attr);
		}	
	}
	
	public class ChangeEvent extends Event {

		public final LivingEntity entity;
		public final Attribute attribute;
		public final double oldValue;
		public final double newValue;
		
		public ChangeEvent(LivingEntity entity, Attribute attribute,
				double oldValue, double newValue)
		{
			this.entity = entity;
			this.attribute = attribute;
			this.oldValue = oldValue;
			this.newValue = newValue;
		}
		
	}
}
