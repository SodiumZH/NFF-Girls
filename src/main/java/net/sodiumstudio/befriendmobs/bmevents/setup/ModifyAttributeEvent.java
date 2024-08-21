package net.sodiumstudio.befriendmobs.bmevents.setup;

import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.sodiumstudio.nautils.NaReflectionUtils;

/**
 * Event to modify existing attributes, e.g. change the range, syncability, etc.
 */
public class ModifyAttributeEvent extends Event implements IModBusEvent
{
	
	/** Set the range limit of an attribute.
	 * This works only on {@link RangedAttribute}. If it's not, the method won't do anything.
	 * <p><b> Warning: Take care calling this because force-changing vanilla attribute range may cause unexpected behaviors!
	 */
	public void setAttributeRange(Attribute attr, double min, double max)
	{
		if (attr instanceof RangedAttribute ra)
		{
			NaReflectionUtils.forceSet(ra, RangedAttribute.class, "f_22307_", min);		// RangedAttribute#minValue
			NaReflectionUtils.forceSet(ra, RangedAttribute.class, "f_22308_", max);		// RangedAttribute#maxValue
			verifyAttribute(ra);
		}
	}
	
	/** Set the minimum limit of an attribute.
	 * This works only on {@link RangedAttribute}. If it's not, the method won't do anything.
	 * <p><b> Warning: Take care calling this because force-changing vanilla attribute range may cause unexpected behaviors!
	 */
	public void setAttributeMinValue(Attribute attr, double min)
	{
		if (attr instanceof RangedAttribute ra)
		{
			NaReflectionUtils.forceSet(ra, RangedAttribute.class, "f_22307_", min);		// RangedAttribute#minValue
			verifyAttribute(ra);
		}
	}
	
	/** Set the maximum limit of an attribute.
	 * This works only on {@link RangedAttribute}. If it's not, the method won't do anything.
	 * <p><b> Warning: Take care calling this because force-changing vanilla attribute range may cause unexpected behaviors!
	 */
	public void setAttributeMaxValue(Attribute attr, double max)
	{
		if (attr instanceof RangedAttribute ra)
		{
			NaReflectionUtils.forceSet(ra, RangedAttribute.class, "f_22308_", max);		// RangedAttribute#maxValue
			verifyAttribute(ra);
		}
	}
	
	/** Set the default value of an attribute.
	 * <p><b> Warning: this will affect ALL mobs and players, including vanilla ones!
	 */
	public void setAttributeDefaultValue(Attribute attr, double defaultVal)
	{
		NaReflectionUtils.forceSet(attr, Attribute.class, "f_22076", defaultVal);		// Attribute#defaultValue
		if (attr instanceof RangedAttribute ra)
		{
			verifyAttribute(ra);
		}
	}
	
	/** 
	 * Set the value unlimited of an attribute.
	 * This works only on {@link RangedAttribute}. If it's not, the method won't do anything.
	 * <p><b> Warning: Take care calling this because force-changing vanilla attribute range may cause unexpected behaviors!
	 */
	public void setAttributeUnranged(Attribute attr)
	{
		this.setAttributeRange(attr, -Double.MAX_VALUE, Double.MAX_VALUE);
	}
	
	/** 
	 * Set the value unlimited but non-negative of an attribute.
	 * This works only on {@link RangedAttribute}. If it's not, the method won't do anything.
	 * <p><b> Warning: Take care calling this because force-changing vanilla attribute range may cause unexpected behaviors!
	 */
	public void setAttributeUnrangedNonNegative(Attribute attr)
	{
		this.setAttributeRange(attr, 0d, Double.MAX_VALUE);
	}
	
	public void setSyncable(Attribute attr, boolean value)
	{
		attr.setSyncable(value);
	}
	
	/** Check if a RangedAttribute is legal in range */
	protected void verifyAttribute(RangedAttribute attr)
	{
		if (attr.getMinValue() > attr.getMaxValue())
		{
			throw new IllegalArgumentException("Minimum value cannot be bigger than maximum value!");
		} else if (attr.getDefaultValue() < attr.getMinValue())
		{
			throw new IllegalArgumentException("Default value cannot be lower than minimum value!");
		} else if (attr.getDefaultValue() > attr.getMaxValue())
		{
			throw new IllegalArgumentException("Default value cannot be bigger than maximum value!");
		}
	}
	
}
