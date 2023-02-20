package com.sodium.dwmg.util;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

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

	// param receiver should be player
	public static void printToScreen(String text, Entity receiver, Entity sender)
	{
		if(receiver instanceof Player player)
			player.sendMessage(new TextComponent(text), sender.getUUID());
	}
}
