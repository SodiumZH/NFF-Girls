package net.sodiumstudio.dwmg.befriendmobs.entitiy.capability;

import java.util.HashSet;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.dwmg.befriendmobs.events.BefriendableTimerUpEvent;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;

public class CBefriendableMobImpl implements CBefriendableMob
{
	
	protected HashSet<UUID> hatred = new HashSet<UUID>();
	
	protected CompoundTag nbt = new CompoundTag();
	
	protected Mob owner = null;
	
	protected CBefriendableMobImpl()
	{
		nbt.put("player_data", new CompoundTag());
		nbt.put("timers", new CompoundTag());
		nbt.getCompound("timers").put("player_timers", new CompoundTag());
	}
	
	
	@Override
	public Mob getOwner()
	{
		if (owner != null)
			return owner;
		else throw new IllegalStateException("CBefriendableMob owner uninitialized.");
	}
	
	public void setOwner(@Nonnull Mob owner)
	{
		this.owner = owner;
	}
	
	@Override
	public HashSet<UUID> getHatred() 
	{
		return hatred;
	}

	@Override
	public void addHatred(Player player, int ticks) 
	{
		if (ticks == 0 || ticks < -1)
			throw new IllegalArgumentException("Ticks must be positive or -1 for permanent.");		
		if (!isInHatred(player))
		{
			hatred.add(player.getUUID());
			setTimerPS(player, "in_hatred", ticks);		
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
		NbtHelper.serializeUUIDSet(tag, hatred, "hatred");
		tag.put("nbt", nbt);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		hatred = NbtHelper.deserializeUUIDSet(nbt, "hatred");
		this.nbt = nbt.getCompound("nbt");
	}

	@Override
	public int getTimer(String key)
	{
		int val = 0;
		if (nbt.getCompound("timers").contains(key, 3))
			val = nbt.getCompound("timers").getInt(key);
		if (val < 0)
			throw new IllegalStateException("CapBefriendableMob: timer \"" + key + "\" contains negative value.");
		return val;
	}
	
	// Player timers are in nbt/timers/player_timers
	@Override
	public int getTimerPS(@Nonnull Player player, String key)
	{
		String actualKey = player.getStringUUID() + "_" + key;
		if (nbt.getCompound("timers").getCompound("player_timers").contains(actualKey, 3))
		{
			return nbt.getCompound("timers").getCompound("player_timers").getInt(actualKey);
		}
		else return 0;
	}
	
	public boolean hasTimer(String key)
	{
		return nbt.getCompound("timers").contains(key, 3);
	}
	
	public boolean hasPlayerTimer(Player player, String key)
	{
		return nbt.getCompound("timers").getCompound("player_timers")
				.contains(player.getStringUUID() + "_" + key, 0);
	}
	
	@Override
	public IntTag setTimer(String key, int ticks)
	{
		CompoundTag timers = nbt.getCompound("timers");
		if (ticks < -1 || ticks == 0)
			throw new IllegalArgumentException("setTimer: ticks should be positive, or -1 for permanent");
		timers.putInt(key, ticks);
		return (IntTag)(timers.get(key));
	}
	
	
	@Override
	public IntTag setTimerPS(Player player, String key, int ticks)
	{
		if (ticks < -1 || ticks == 0)
			throw new IllegalArgumentException("setPlayerTimer: ticks should be positive, or -1 for permanent");
		String actualKey = player.getStringUUID() + "_" + key;
		nbt.getCompound("timers").getCompound("player_timers").putInt(actualKey, ticks);
		return (IntTag)(nbt.getCompound("timers").getCompound("player_timers").get(actualKey));
	}
	
	@Override
	public void updateTimers()
	{
		for (String key : nbt.getCompound("timers").getAllKeys())
		{
			Tag tag = nbt.getCompound("timers").get(key);
			if (tag instanceof IntTag intTag)	// player_timers tag is excluded
			{
				int val = intTag.getAsInt();
				if (val > 0)
				{
					val --;
					nbt.getCompound("timers").putInt(key, val);
					if (val == 0)
					{
						MinecraftForge.EVENT_BUS.post(new BefriendableTimerUpEvent(this, key, null));
					}
				}
			}
		}
		for (String key : nbt.getCompound("timers").getCompound("player_timers").getAllKeys())
		{
			UUID uuid = UUID.fromString(key.substring(0, 36));
			Player player = owner.level.getPlayerByUUID(uuid);
			if (player != null)
			{
				int val = nbt.getCompound("timers").getCompound("player_timers").getInt(key);
				if (val > 0)
				{
					val --;
					nbt.getCompound("timers").getCompound("player_timers").putInt(key, val);
					if (val == 0)
					{
						// position 36 is "_"
						MinecraftForge.EVENT_BUS.post(new BefriendableTimerUpEvent(this, key.substring(37), player));
						//Debug.printToScreen("Timer up: " + key, player);
					}
				}
			}	
		}
	}

}
