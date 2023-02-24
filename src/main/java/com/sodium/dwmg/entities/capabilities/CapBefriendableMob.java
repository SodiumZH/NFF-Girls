package com.sodium.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import com.sodium.dwmg.util.NbtHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class CapBefriendableMob implements ICapBefriendableMob
{
	
	protected Vector<UUID> hatred = new Vector<UUID>();
	
	public CompoundTag nbt = new CompoundTag();
	
	protected CapBefriendableMob()
	{
	}
	
	@Override
	public Vector<UUID> getHatred() 
	{
		return hatred;
	}

	@Override
	public void addHatred(Player player) 
	{
		if(!isInHatred(player))
		{
			hatred.add(player.getUUID());
		}
	}
	
	@Override
	public boolean isInHatred(Player player) 
	{
		return hatred.contains(player.getUUID());
	}
	
	@Override
	public CompoundTag getNBT() 
	{
		return nbt;
	}

	@Override
	public CompoundTag getPlayerData()
	{
		return nbt.getCompound("player_data");
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		NbtHelper.serializeUUIDArray(tag, hatred, "hatred");
		tag.put("nbt", nbt);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		hatred = NbtHelper.deserializeUUIDArray(nbt, "hatred");
		this.nbt = nbt.getCompound("nbt");
	}

}
