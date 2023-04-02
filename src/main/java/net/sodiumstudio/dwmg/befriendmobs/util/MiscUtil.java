package net.sodiumstudio.dwmg.befriendmobs.util;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public class MiscUtil {
	
	public static UUID getUUIDIfExists(Entity entity)
	{
		return entity == null ? null : entity.getUUID();
	}
	
	public static String getNameString(Entity target)
	{
		return target != null ? target.getName().getString() : "null";
	}

	@Deprecated
	public static void printToScreen(Component text, Player receiver, Entity sender)
	{
		printToScreen(text, receiver);
	}
	
	@Deprecated
	public static void printToScreen(String text, Player receiver, Entity sender)
	{
		printToScreen(text, receiver);
	}
	
	public static void printToScreen(Component text, Player receiver)
	{
		receiver.sendSystemMessage(text);
	}
	
	public static void printToScreen(String text, Player receiver)
	{
		receiver.sendSystemMessage(MutableComponent.create(new LiteralContents(text)));
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
