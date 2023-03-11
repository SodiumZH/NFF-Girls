package net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.EntityType;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;

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
