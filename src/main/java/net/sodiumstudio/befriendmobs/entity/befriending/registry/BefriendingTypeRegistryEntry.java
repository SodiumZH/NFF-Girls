package net.sodiumstudio.befriendmobs.entity.befriending.registry;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.EntityType;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;

@Deprecated
public class BefriendingTypeRegistryEntry {

	public EntityType<?> fromType = null;
	public EntityType<?> convertToType = null;
	public Class<? extends BefriendingHandler> handlerClass = null;
	
	public BefriendingTypeRegistryEntry(EntityType<?> before, EntityType<?> after, Class<? extends BefriendingHandler> handlerClass)
	{
		this.fromType = before;
		this.convertToType = after;
		this.handlerClass = handlerClass;
	}

}
