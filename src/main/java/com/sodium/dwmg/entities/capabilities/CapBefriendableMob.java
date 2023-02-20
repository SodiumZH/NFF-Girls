package com.sodium.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.util.NbtHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class CapBefriendableMob implements ICapBefriendableMob{

	
	protected Vector<UUID> hatred = new Vector<UUID>();
	
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
		if(!hatred.contains(player.getUUID()))
		{
			hatred.add(player.getUUID());
		}
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		NbtHelper.serializeUUIDArray(tag, hatred, "hatred");
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		hatred = NbtHelper.deserializeUUIDArray(nbt, "hatred");
	}
	
	@Override
	public IBefriendedMob befriend() {
		return null;
	}

}
