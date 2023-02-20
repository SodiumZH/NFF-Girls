package com.sodium.dwmg.util;

import java.util.UUID;
import java.util.Vector;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;

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
	
}
