package com.sodium.dwmg.util;

import java.util.UUID;
import java.util.Vector;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

public class NbtHelper {

	// Serialize a UUID array from vector into the given compound tag with given key.
	// Return the ListTag containing the UUIDs.
	public static ListTag serializeUUIDArray(CompoundTag tag, Vector<UUID> array, String key)
	{
		ListTag list = new ListTag();
		for (UUID id : array)
		{
			list.add(NbtUtils.createUUID(id));
		}
		tag.put(key, list);
		return list;
	}
	
	// Deserialize a UUID array into vector from a compound tag with given key.
	// Return a new vector containing the UUIDs.
	public static Vector<UUID> deserializeUUIDArray(CompoundTag inTag, String key)
	{
		ListTag uuidArrayTag = inTag.getList(key, Tag.TAG_INT_ARRAY);
		Vector<UUID> out = new Vector<UUID>();
		for(Tag tag : uuidArrayTag)
		{
			out.add(NbtUtils.loadUUID(tag));
		}
		return out;
	}

	// Check if a compound tag contains a compound subtag with player's string uuid as key.
	public static boolean containsPlayer(CompoundTag inTag, Player player)
	{
		return inTag.contains(player.getStringUUID()) && (inTag.get(player.getStringUUID()) instanceof CompoundTag);
	}
	
	// Check if a compound tag contains a player
	public static boolean containsPlayerData(CompoundTag inTag, Player player, String key)
	{
		return containsPlayer(inTag, player) && inTag.getCompound(player.getStringUUID()).contains(key);
	}
	
	
	
}
