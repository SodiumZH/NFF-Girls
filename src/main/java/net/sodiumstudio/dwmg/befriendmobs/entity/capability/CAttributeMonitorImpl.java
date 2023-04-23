package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import java.util.HashMap;

import net.minecraft.world.entity.LivingEntity;

public class CAttributeMonitorImpl implements CAttributeMonitor {

	protected LivingEntity owner;
	
	protected HashMap<String, Double> map = new HashMap<String, Double>();
	
	public CAttributeMonitorImpl(LivingEntity owner)
	{
		this.owner = owner;
	}
	
	@Override
	public LivingEntity getOwner() {
		return owner;
	}

	@Override
	public HashMap<String, Double> getListenList() {
		return map;
	}

}
