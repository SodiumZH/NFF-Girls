package com.sodium.dwmg.util;

import java.util.UUID;

import net.minecraft.world.entity.Entity;

public class Util {

	public static final UUID UUID_NULL = new UUID((long)0, (long)0);
	
	// Check if two objects are equal using UUID.
	public static boolean objEqual(UUID a, UUID b)
	{
		return a != UUID_NULL && a == b ;
	}
	
	public static UUID getUUIDIfExists(Entity entity)
	{
		return entity == null ? UUID_NULL : entity.getUUID();
	}
	
	
}
