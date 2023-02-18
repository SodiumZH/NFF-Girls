package com.sodium.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.player.Player;

public class CapBefriendableMob implements ICapBefriendableMob{

	
	protected Vector<UUID> hatred = new Vector<UUID>();
	
	@Override
	public Vector<UUID> getHatred() 
	{
		return hatred;
	}

	@Override
	public void addHatred(Player player) 
	{
		if(!hatred.contains(player.getUUID()))
		{
			hatred.add(player.getUUID());
		}
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		
		ListTag hatredTag = new ListTag();
		for (UUID id : hatred)
		{
			hatredTag.add(NbtUtils.createUUID(id));
		}
		tag.put("hatred", hatredTag);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		ListTag everHostileList = nbt.getList("hatred", Tag.TAG_INT_ARRAY);
		NBTUtil.
	}



}
