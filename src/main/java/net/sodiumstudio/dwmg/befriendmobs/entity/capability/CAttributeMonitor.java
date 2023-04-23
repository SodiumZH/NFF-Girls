package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import java.util.HashMap;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;

public interface CAttributeMonitor {

	public LivingEntity getOwner();
	
	public HashMap<String, Double> getListenList();
	
	public default CAttributeMonitor listen(Attribute attribute)
	{
		getListenList().put(Registry.ATTRIBUTE.getKey(attribute).toString(), getOwner().getAttributeValue(attribute));
		return this;
	}
	
	public default void update()
	{
		for (String key: getListenList().keySet())
		{
			Attribute attr = Registry.ATTRIBUTE.get(new ResourceLocation(key));
			if (attr == null)
				continue;
			double oldVal = getListenList().get(key);
			double newVal = getOwner().getAttributeValue(attr);			
			if (oldVal - newVal > 0.0000001 || oldVal - newVal < -0.0000001)
			{
				MinecraftForge.EVENT_BUS.post(new LivingAttributeValueChangeEvent(
						getOwner(), attr, oldVal, newVal));
			}
			getListenList().put(key, newVal);
		}
	}
	
	public static void listen(LivingEntity living, Attribute attr)
	{
		if (!living.getCapability(BefMobCapabilities.CAP_ATTRIBUTE_MONITOR).isPresent())
			throw new IllegalStateException("Living entity missing attribute monitor capability.");
		living.getCapability(BefMobCapabilities.CAP_ATTRIBUTE_MONITOR).ifPresent((cap) -> 
		{
			cap.listen(attr);
		});
	}
}
