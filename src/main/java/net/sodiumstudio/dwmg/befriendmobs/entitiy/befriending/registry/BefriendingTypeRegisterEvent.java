package net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;

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
