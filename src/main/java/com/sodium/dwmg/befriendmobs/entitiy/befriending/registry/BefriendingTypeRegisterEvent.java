package com.sodium.dwmg.befriendmobs.entitiy.befriending.registry;

import com.sodium.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class BefriendingTypeRegisterEvent extends Event implements IModBusEvent {
	
	public void register(EntityType<?> befriendable, EntityType<?> convertTo, AbstractBefriendingHandler handler, boolean force)
	{
		BefriendingTypeRegistry.register(befriendable, convertTo, handler, force);
	}
	
	public void register(EntityType<?> befriendable, EntityType<?> convertTo, AbstractBefriendingHandler handler)
	{
		BefriendingTypeRegistry.register(befriendable, convertTo, handler);
	}
	
}
