package net.sodiumstudio.befriendmobs.bmevents.setup;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendingHandler;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;

public class RegisterBefriendingTypeEvent extends Event implements IModBusEvent {
	
	public void register(@Nonnull EntityType<? extends Mob> from, @Nonnull EntityType<? extends Mob> convertTo, @Nonnull BefriendingHandler handler, boolean override)
	{
		BefriendingTypeRegistry.register(from, convertTo, handler, override);
	}
	
	public void register(@Nonnull EntityType<? extends Mob> fromType, @Nonnull EntityType<? extends Mob> convertToType, @Nonnull BefriendingHandler handler)
	{
		BefriendingTypeRegistry.register(fromType, convertToType, handler);
	}
	
}
