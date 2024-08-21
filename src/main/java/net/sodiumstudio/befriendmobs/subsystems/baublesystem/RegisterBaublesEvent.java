package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * Fired on registering bauble items. Bauble items are only allowed to register here.
 */
public class RegisterBaublesEvent extends Event implements IModBusEvent
{
	public void register(IBaubleRegistryEntry entry)
	{
		BaubleRegistries.registerBaubleRaw(entry);
	}
}
