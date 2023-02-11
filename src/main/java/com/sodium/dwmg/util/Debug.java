package com.sodium.dwmg.util;

import net.minecraft.world.entity.LivingEntity;

public class Debug {
	
	public static String getMobNameString(LivingEntity inMob)
	{
		return inMob != null ? inMob.getName().getString() : "null";
	}
}
