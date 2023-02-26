package com.sodium.dwmg.util;

import com.sodium.dwmg.Dwmg;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

// All debug output should work only in development.
public class Debug {
	
	// param receiver should be player
	public static void printToScreen(String text, Entity receiver, Entity sender)
	{
		if(receiver instanceof Player player && Dwmg.IS_DEBUG)
			
			player.sendMessage(new TextComponent(text), sender.getUUID());
	}
	
}
