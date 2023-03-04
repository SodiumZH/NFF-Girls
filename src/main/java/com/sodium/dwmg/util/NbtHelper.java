package com.sodium.dwmg.util;

import java.util.UUID;
import java.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

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
	
	// Check if a compound tag contains a player's string uuid as subtag, and a tag with given key under it
	public static boolean containsPlayerData(CompoundTag inTag, Player player, String key)
	{
		return containsPlayer(inTag, player) && inTag.getCompound(player.getStringUUID()).contains(key);
	}
	
	// Get tag if a compound tag has a player-uuid-named subtag which contains subtag with given key
	// Return null if not present
	public static Tag getPlayerData(CompoundTag inTag, Player player, String key)
	{
		return containsPlayerData(inTag, player, key) ? inTag.getCompound(player.getStringUUID()).get(key) : null;
	}
	
	public static void putPlayerData(Tag inTag, CompoundTag putTo, Player player, String key)
	{
		if (!containsPlayer(putTo, player))
			putTo.put(player.getStringUUID(), new CompoundTag());
		putTo.getCompound(player.getStringUUID()).put(key, inTag);
	}
	
	public static CompoundTag saveItemStack(@Nullable ItemStack stack, @Nonnull CompoundTag saveTo, String key)
	{
		if (stack == null || stack.isEmpty())
			return null;
		else
		{
			CompoundTag newTag = new CompoundTag();
			stack.save(newTag);
			saveTo.put(key, newTag);
			return newTag;
		}
	}
	
	public static ItemStack readItemStack(CompoundTag nbt, String key)
	{
		if (nbt.contains(key, 10))
		{
			ItemStack stack = ItemStack.of(nbt.getCompound(key));
			if (stack != null && !stack.isEmpty())
				return stack;
			else return ItemStack.EMPTY;
		}
		else return ItemStack.EMPTY;
	}
	
}
