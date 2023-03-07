package com.sodium.dwmg.befriendmobsapi.entitiy.befriending.registry;

import net.minecraft.world.entity.EntityType;

public class BefriendingTypeMapRegistryEntry {

	public EntityType<?> beforeType = null;
	public EntityType<?> afterType = null;
	
	public BefriendingTypeMapRegistryEntry(EntityType<?> before, EntityType<?> after)
	{
		this.beforeType = before;
		this.afterType = after;
	}

}
