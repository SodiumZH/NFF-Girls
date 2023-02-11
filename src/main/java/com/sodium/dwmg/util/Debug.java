package com.sodium.dwmg.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class Debug {
	
	@Deprecated
	public static String getMobNameString(LivingEntity inMob)
	{
		return inMob != null ? inMob.getName().getString() : "null";
	}
	
	public static String getNameString(Entity target)
	{
		return target != null ? target.getName().getString() : "null";
	}
	
}
