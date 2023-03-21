package net.sodiumstudio.dwmg.befriendmobs.util;

import java.util.UUID;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class Util {
	
	public static UUID getUUIDIfExists(Entity entity)
	{
		return entity == null ? null : entity.getUUID();
	}
	
	public static String getNameString(Entity target)
	{
		return target != null ? target.getName().getString() : "null";
	}

	public static void printToScreen(Component text, Player receiver, Entity sender)
	{
		receiver.sendMessage(text, sender.getUUID());
	}
	
	public static void printToScreen(String text, Player receiver, Entity sender)
	{
		receiver.sendMessage(new TextComponent(text), sender.getUUID());
	}
	
	public static void printToScreen(Component text, Player receiver)
	{
		Util.printToScreen(text, receiver, receiver);
	}
	
	public static void printToScreen(String text, Player receiver)
	{
		Util.printToScreen(new TextComponent(text), receiver, receiver);
	}
}
