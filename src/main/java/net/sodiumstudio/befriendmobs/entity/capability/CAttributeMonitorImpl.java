package net.sodiumstudio.befriendmobs.entity.capability;

import java.util.HashMap;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class CAttributeMonitorImpl implements CAttributeMonitor {

	protected LivingEntity owner;
	
	protected HashMap<Attribute, Double> map = new HashMap<Attribute, Double>();
	
	public CAttributeMonitorImpl(LivingEntity owner)
	{
		this.owner = owner;
	}
	
	@Override
	public LivingEntity getOwner() {
		return owner;
	}

	@Override
	public HashMap<Attribute, Double> getListenList() {
		return map;
	}

}
