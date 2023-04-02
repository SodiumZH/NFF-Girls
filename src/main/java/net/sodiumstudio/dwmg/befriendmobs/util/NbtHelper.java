package net.sodiumstudio.dwmg.befriendmobs.util;

import java.util.HashSet;
import java.util.UUID;
import java.util.Vector;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class NbtHelper {

	// Generate a unique key from the base key in a compound tag
	public static String getUniqueKey(String baseKey, CompoundTag cpd)
	{
		int i = 0;
		while (cpd.contains(baseKey + "_" + Integer.toString(i)))
		{
			i += 1;
		}
		return baseKey + "_" + Integer.toString(i);
	}
	
	// Serialize a UUID array from vector into the given compound tag with given key.
	// Return the ListTag containing the UUIDs.
	public static ListTag serializeUUIDSet(CompoundTag tag, HashSet<UUID> set, String key)
	{
		ListTag list = new ListTag();
		for (UUID id : set)
		{
			list.add(NbtUtils.createUUID(id));
		}
		tag.put(key, list);
		return list;
	}
	
	// Deserialize a UUID array into vector from a compound tag with given key.
	// Return a new vector containing the UUIDs.
	public static HashSet<UUID> deserializeUUIDSet(CompoundTag inTag, String key)
	{
		ListTag uuidSetTag = inTag.getList(key, Tag.TAG_INT_ARRAY);
		HashSet<UUID> out = new HashSet<UUID>();
		for(Tag tag : uuidSetTag)
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
	
	public static void removePlayerData(CompoundTag removeFrom, Player player, String key)
	{
		if (containsPlayerData(removeFrom, player, key))
		{
			removeFrom.getCompound(player.getStringUUID()).remove(key);
		}
	}
	
	public static CompoundTag saveItemStack(@Nullable ItemStack stack, @Nonnull CompoundTag saveTo, String key) {
		CompoundTag newTag = new CompoundTag();
		if (stack == null || stack.isEmpty())
			ItemStack.EMPTY.save(newTag);
		else
			stack.save(newTag);
		saveTo.put(key, newTag);
		return newTag;
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
	
	public static void saveEquipment(CompoundTag toTag, Mob inMob)
	{
		saveItemStack(inMob.getItemBySlot(EquipmentSlot.HEAD), toTag, "nbt_helper_equipment_item_head");
		saveItemStack(inMob.getItemBySlot(EquipmentSlot.CHEST), toTag, "nbt_helper_equipment_item_chest");
		saveItemStack(inMob.getItemBySlot(EquipmentSlot.LEGS), toTag, "nbt_helper_equipment_item_legs");
		saveItemStack(inMob.getItemBySlot(EquipmentSlot.FEET), toTag, "nbt_helper_equipment_item_feet");
		saveItemStack(inMob.getItemBySlot(EquipmentSlot.MAINHAND), toTag, "nbt_helper_equipment_item_main_hand");
		saveItemStack(inMob.getItemBySlot(EquipmentSlot.OFFHAND), toTag, "nbt_helper_equipment_item_off_hand");
	}
	
	public static void readEquipment(Mob toMob, CompoundTag inTag)
	{
		toMob.setItemSlot(EquipmentSlot.HEAD, readItemStack(inTag, "nbt_helper_equipment_item_head"));
		toMob.setItemSlot(EquipmentSlot.CHEST, readItemStack(inTag, "nbt_helper_equipment_item_chest"));
		toMob.setItemSlot(EquipmentSlot.LEGS, readItemStack(inTag, "nbt_helper_equipment_item_legs"));
		toMob.setItemSlot(EquipmentSlot.FEET, readItemStack(inTag, "nbt_helper_equipment_item_feet"));
		toMob.setItemSlot(EquipmentSlot.MAINHAND, readItemStack(inTag, "nbt_helper_equipment_item_main_hand"));
		toMob.setItemSlot(EquipmentSlot.OFFHAND, readItemStack(inTag, "nbt_helper_equipment_item_off_hand"));
	}
	
	public static enum TagType
	{
		   TAG_BYTE(1),
		   TAG_SHORT(2),
		   TAG_INT(3),
		   TAG_LONG(4),
		   TAG_FLOAT(5),
		   TAG_DOUBLE(6),
		   TAG_BYTE_ARRAY(7),
		   TAG_STRING(8),
		   TAG_LIST(9),
		   TAG_COMPOUND(10),
		   TAG_INT_ARRAY(11),
		   TAG_LONG_ARRAY(12),
		   TAG_ANY_NUMERIC(99);
		
		protected int id;
		
		private TagType(int id)
		{
			this.id = id;
		}
		
		public int getID()
		{
			return id;
		}
		
	}
	
	public static Player getPlayerFromKey(CompoundTag fromTag, String key, Level level)
	{
		if (level == null)
			return null;
		else if (!fromTag.contains(key, 11))
			return null;
		return level.getPlayerByUUID(fromTag.getUUID(key));
	}
	
	public static void putPlayerToKey(Player player, CompoundTag toTag, String key)
	{
		if (player == null)
			return;
		toTag.putUUID(key, player.getUUID());
	}
}
