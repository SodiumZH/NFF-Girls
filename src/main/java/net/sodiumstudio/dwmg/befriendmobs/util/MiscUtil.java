package net.sodiumstudio.dwmg.befriendmobs.util;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public class MiscUtil {
	
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
		MiscUtil.printToScreen(text, receiver, receiver);
	}
	
	public static void printToScreen(String text, Player receiver)
	{
		MiscUtil.printToScreen(new TextComponent(text), receiver, receiver);
	}
	
	public static <T> boolean sameObject(T a, T b)
	{
		return a != null && a == b;
	}
	
	public static <T> boolean isIn(T test, T[] set)
	{	
		if (test == null)
			return false;
		for (T elem: set)
		{
			if (test.equals(elem))
			{
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean isIn(T test, T[] set, T nullObj)
	{
		if (test == null || test.equals(nullObj))
			return false;
		return isIn(test, set);
	}
	
	public static <T> boolean isIn(T test, HashSet<T> set)
	{
		for (T elem: set)
		{
			if (test.equals(elem))
			{
				return true;
			}
		}
		return false;
	}
	
	public static <T> boolean isIn(T test, HashSet<T> set, T nullObj)
	{
		if (test == null || test.equals(nullObj))
			return false;
		return isIn(test, set);
	}
}
