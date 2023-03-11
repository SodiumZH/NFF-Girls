package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;

// All debug output should work only in development.
public class Debug {
	
	// param receiver should be player
	public static void printToScreen(String text, Player receiver, Entity sender)
	{
		if(BefriendMobs.IS_DEBUG_MODE)			
			receiver.sendMessage(new TextComponent(text), sender.getUUID());
	}
	
	public static void printToScreen(String text, Player receiver)
	{		
		printToScreen(text, receiver, receiver);
	}
}
