package net.sodiumstudio.dwmg.befriendmobs.util;

import java.util.UUID;

import net.minecraft.world.entity.Entity;

public class Util {
	
	public static UUID getUUIDIfExists(Entity entity)
	{
		return entity == null ? null : entity.getUUID();
	}
	
	public static String getNameString(Entity target)
	{
			return target != null ? target.getName().getString() : "null";
	}

	
}
