package com.sodium.dwmg.befriendmobsapi.entitiy.befriending.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class BefriendingTypeMapRegisterEvent extends Event implements IModBusEvent {
	
	public void register(EntityType<?> befriendable, EntityType<?> convertTo, boolean force)
	{
		BefriendingTypeMapRegistry.add(befriendable, convertTo, force);
	}
	
	public void register(EntityType<?> befriendable, EntityType<?> convertTo)
	{
		BefriendingTypeMapRegistry.add(befriendable, convertTo);
	}
	
}
