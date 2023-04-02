package net.sodiumstudio.dwmg.befriendmobs.util.debug;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;

// All debug output should work only in development.
public class Debug {
	
	@Deprecated
	public static void printToScreen(String text, Player receiver, Entity sender)
	{
		printToScreen(text, receiver);
	}
	
	public static void printToScreen(String text, Player receiver)
	{		
		if(BefriendMobs.IS_DEBUG_MODE)			
			MiscUtil.printToScreen(text, receiver);
	}
	
}
