package com.sodium.dwmg.befriendmobs.entitiy.capability;

import java.util.UUID;
import java.util.Vector;

import javax.annotation.Nonnull;

import com.sodium.dwmg.befriendmobs.util.NbtHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.player.Player;

public class CBefriendableMobImpl implements CBefriendableMob
{
	
	protected Vector<UUID> hatred = new Vector<UUID>();
	
	protected CompoundTag nbt = new CompoundTag();
	
	protected CBefriendableMobImpl()
	{
		nbt.put("player_data", new CompoundTag());
		nbt.put("timers", new CompoundTag());
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

	@Override
	public int getTimer(String key)
	{
		int val = 0;
		if (nbt.getCompound("timers").contains(key))
			val = nbt.getCompound("timers").getInt(key);
		if (val < 0)
			throw new IllegalStateException("CapBefriendableMob: timer \"" + key + "\" contains negative value.");
		return val;
	}
	
	@Override
	public int getTimerPS(@Nonnull Player player, String key)
	{
		int val = 0;
		String timerKey = key + "_ps_" + player.getStringUUID();
		if (nbt.getCompound("timers").contains(timerKey))
			val = nbt.getCompound("timers").getInt(timerKey);
		if (val < 0)
			throw new IllegalStateException("CapBefriendableMob: player-specified timer \"" + key + "\" for \"" + player.getName().getString() + " contains negative value.");
		return val;
	}
	
	@Override
	public IntTag setTimer(String key, int ticks)
	{
		if (ticks <= 0)
			throw new IllegalArgumentException("CapBefriendableMob: timer \"" + key + " setting non-positive value.");
		IntTag newTag = IntTag.valueOf(ticks);
		nbt.getCompound("timers").put(key, newTag);
		return newTag;
	}
	
	
	@Override
	public IntTag setTimerPS(Player player, String key, int ticks)
	{
		if (ticks <= 0)
			throw new IllegalArgumentException("CapBefriendableMob: player-specified timer \"" + key + "\" for \"" + player.getName().getString() + " setting non-positive value.");
		IntTag newTag = IntTag.valueOf(ticks);
		nbt.getCompound("timers").put(key + "_ps_" + player.getStringUUID(), newTag);
		return newTag;
	}
	
	@Override
	public void updateTimers()
	{
		for (String key : nbt.getCompound("timers").getAllKeys())
		{
			int val = nbt.getCompound("timers").getInt(key);
			if (val > 0)
			{
				val --;
				nbt.getCompound("timers").put(key, IntTag.valueOf(val));
			}
		}
	}
}
