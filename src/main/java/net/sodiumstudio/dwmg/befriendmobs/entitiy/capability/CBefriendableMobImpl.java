package net.sodiumstudio.dwmg.befriendmobs.entitiy.capability;

import java.util.UUID;
import java.util.Vector;

import javax.annotation.Nonnull;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.dwmg.befriendmobs.events.BefriendableTimerUpEvent;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;

public class CBefriendableMobImpl implements CBefriendableMob
{
	
	protected Vector<UUID> hatred = new Vector<UUID>();
	
	protected CompoundTag nbt = new CompoundTag();
	
	protected Mob owner = null;
	
	protected CBefriendableMobImpl()
	{
		nbt.put("player_data", new CompoundTag());
		nbt.put("timers", new CompoundTag());
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
	public Vector<UUID> getHatred() 
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
			if (ticks > 0)
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
		UUID uuid;
		if (nbt.getCompound("timers").contains(key, 10))
			uuid = nbt.getCompound("timers").getCompound(key).getUUID("player_uuid");
		else return 0;
		if (player.getUUID().equals(uuid))
			return nbt.getCompound("timers").getCompound(key).getInt("timer");
		else return 0;
	}
	
	@Override
	public IntTag setTimer(String key, int ticks)
	{
		IntTag newTag = IntTag.valueOf(ticks);
		nbt.getCompound("timers").put(key, newTag);
		return newTag;
	}
	
	
	@Override
	public CompoundTag setTimerPS(Player player, String key, int ticks)
	{
		if (ticks <= 0)
			throw new IllegalArgumentException("CapBefriendableMob: player-specified timer \"" + key + "\" for \"" + player.getName().getString() + " setting non-positive value.");
		CompoundTag newTag = new CompoundTag();
		nbt.getCompound("timers").put(key, newTag);
		nbt.getCompound("timers").getCompound(key).put("player_uuid", NbtUtils.createUUID(player.getUUID()));
		nbt.getCompound("timers").getCompound(key).put("timer", IntTag.valueOf(ticks));
		return newTag;
	}
	
	@Override
	public void updateTimers()
	{
		for (String key : nbt.getCompound("timers").getAllKeys())
		{
			Tag tag = nbt.getCompound("timers").get(key);
			if (val > 0)
			{
				val --;
				nbt.getCompound("timers").put(key, IntTag.valueOf(val));
				if (val == 0)
				{
					MinecraftForge.EVENT_BUS.post(new BefriendableTimerUpEvent(this, key));
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onTimerUp(BefriendableTimerUpEvent event)
	{
		if ()
	}
}
