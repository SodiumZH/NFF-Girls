package com.sodium.dwmg.befriendmobsapi.entitiy.befriending.registry;

import javax.annotation.Nonnull;

import com.sodium.dwmg.befriendmobsapi.entitiy.befriending.AbstractBefriendingHandler;

import net.minecraft.world.entity.EntityType;

@Deprecated
public class BefriendingTypeRegistryEntry {

	public EntityType<?> fromType = null;
	public EntityType<?> convertToType = null;
	public Class<? extends AbstractBefriendingHandler> handlerClass = null;
	
	public BefriendingTypeRegistryEntry(EntityType<?> before, EntityType<?> after, Class<? extends AbstractBefriendingHandler> handlerClass)
	{
		this.fromType = before;
		this.convertToType = after;
		this.handlerClass = handlerClass;
	}

}
