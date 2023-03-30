package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import java.util.HashSet;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
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
	
	protected CompoundTag nbt = new CompoundTag();
	
	protected Mob owner = null;
	
	protected HashSet<UUID> cachedHatred = new HashSet<UUID>();
	
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
		cachedHatred = NbtHelper.deserializeUUIDSet(nbt, "hatred");	
		return cachedHatred;
	}

	@Override
	public void addHatred(Player player, int ticks) 
	{
		cachedHatred = NbtHelper.deserializeUUIDSet(nbt, "hatred");	
		if (ticks == 0 || ticks < -1)
			throw new IllegalArgumentException("Ticks must be positive or -1 for permanent.");		
		if (!isInHatred(player))
		{
			cachedHatred.add(player.getUUID());
			NbtHelper.serializeUUIDSet(getNbt(), cachedHatred, "hatred");			
			setPlayerTimer(player, "in_hatred", ticks);	
		}
	}
	
	@Override
	public boolean isInHatred(Player player) 
	{
		cachedHatred = NbtHelper.deserializeUUIDSet(nbt, "hatred");	
		return cachedHatred.contains(player.getUUID());
	}
	
	@Override
	public CompoundTag getNbt() 
	{
		return nbt;
	}

	@Override
	public CompoundTag getPlayerDataNbt()
	{
		return nbt.getCompound("player_data");
	}

	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		tag.put("nbt", nbt);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{		
		this.nbt = nbt.getCompound("nbt");
		this.cachedHatred = getHatred();
	}

	@Override
	public int getTimer(String key)
	{
		if (key.equals("player_timers"))
			throw new IllegalArgumentException
				("setPlayerTimer: \"player_timers\" is reserved and cannot be a timer key.");
		int val = 0;
		if (nbt.getCompound("timers").contains(key, 3))
			val = nbt.getCompound("timers").getInt(key);
		if (val < 0)
			throw new IllegalStateException("CapBefriendableMob: timer \"" + key + "\" contains negative value.");
		return val;
	}
	
	// Player timers are in nbt/timers/player_timers
	@Override
	public int getPlayerTimer(@Nonnull Player player, String key)
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
		if (key.equals("player_timers"))
			throw new IllegalArgumentException
				("setPlayerTimer: \"player_timers\" is reserved and cannot be a timer key.");
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
		if (key.equals("player_timers"))
			throw new IllegalArgumentException
				("setPlayerTimer: \"player_timers\" is reserved and cannot be a timer key.");
		CompoundTag timers = nbt.getCompound("timers");
		if (ticks < -1 || ticks == 0)
			throw new IllegalArgumentException("setTimer: ticks should be positive, or -1 for permanent");
		timers.putInt(key, ticks);
		return (IntTag)(timers.get(key));
	}
	
	@Override
	public IntTag setPlayerTimer(Player player, String key, int ticks)
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
						if (key.equals("in_hatred"))
						{
							// If a hatred timer is up, remove the player from hatred list
							cachedHatred = NbtHelper.deserializeUUIDSet(nbt, "hatred");	
							if (cachedHatred.contains(uuid))
								cachedHatred.remove(player.getUUID());
							NbtHelper.serializeUUIDSet(nbt, cachedHatred, "hatred");
							Debug.printToScreen("Player removed from hatred list of mob " + owner.getName().getString(), player);
						}
						// position 36 is "_"
						MinecraftForge.EVENT_BUS.post(new BefriendableTimerUpEvent(this, key.substring(37), player));
					}
				}
			}	
		}
	}
}
