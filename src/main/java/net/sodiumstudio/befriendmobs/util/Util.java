package net.sodiumstudio.befriendmobs.util;

import java.util.UUID;

import net.minecraft.world.entity.Entity;

public class Util {
	
	// Check if two objects are equal using UUID.
	public static boolean uuidEqual(UUID a, UUID b)
	{
		return a != null && a.getLeastSignificantBits() == b.getLeastSignificantBits() && a.getMostSignificantBits() == b.getLeastSignificantBits() ;
	}
	
	public static UUID getUUIDIfExists(Entity entity)
	{
		return entity == null ? null : entity.getUUID();
	}
	
	public static String getNameString(Entity target)
	{
			return target != null ? target.getName().getString() : "null";
	}
	
	/** A wrapper of boolean to provide an exterior value that can be changed inside a lambda function
	 * e.g. in a provider or consumer
	 * This is not a true "global" variable. "Global" refers to that the value is outside the lambda function
	 */
	public static class GlobalBoolean
	{
		private boolean value;
		
		public GlobalBoolean(boolean value)
		{
			this.value = value;
		}
		
		public boolean get()
		{
			return value;
		}
		
		public boolean set(boolean value)
		{
			this.value = value;
			return value;
		}
	}
	
	public static Util.GlobalBoolean createGB(boolean value)
	{
		return new Util.GlobalBoolean(value);
	}
	
}
