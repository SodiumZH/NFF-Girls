package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import java.util.HashSet;
import java.util.UUID;
import java.util.HashSet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendingHandler;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.events.BefriendableAddHatredEvent;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;

public interface CBefriendableMob extends INBTSerializable<CompoundTag> {

	public Mob getOwner();
	
	// ======== Hatred List related
	
	// Get this mob's hatred list
	// Sometimes hatred list prevents player to befriend the mob
	public HashSet<UUID> getHatred();
	
	// Add a player to the hatred list
	// If a player is in hatred list, it will take some time to remove from it. 15min by default.
	// Or input -1 to add permanent hatred.
	// This function doesn't post events.
	public void addHatred(Player player, int ticks);
	
	public default void addHatred(Player player)
	{
		addHatred(player, 18000);
	}

	public default void addHatredPermanent(Player player)
	{
		addHatred(player, -1);
	}
	
	// Check if a player is in the hatred list
	public boolean isInHatred(Player player);
	
	public int getHatredDuration(Player player);
	
	// ======== Serialization
	
	// Get NBT tag
	public CompoundTag getNbt();
	
	// Get the whole player data subtag under NBT
	public CompoundTag getPlayerDataNbt();

	public default Tag getPlayerData(Player player, String key)
	{
		return NbtHelper.getPlayerData(getPlayerDataNbt(), player, key);
	}

	public default int getPlayerDataInt(Player player, String key)
	{
		return ((IntTag)getPlayerData(player, key)).getAsInt();
	}
	
	public default float getPlayerDataFloat(Player player, String key)
	{
		return ((FloatTag)getPlayerData(player, key)).getAsFloat();
	}
	
	public default double getPlayerDataDouble(Player player, String key)
	{
		Tag tag = getPlayerData(player, key);
		if (tag instanceof FloatTag f)
			return f.getAsDouble();
		else if (tag instanceof DoubleTag d)
			return d.getAsDouble();
		else throw new ClassCastException();
	}
		
	public default void putPlayerData(Tag data, Player player, String key)
	{
		NbtHelper.putPlayerData(data, getPlayerDataNbt(), player, key);
	}
	
	public default boolean hasPlayerData(Player player, String key)
	{
		return NbtHelper.containsPlayerData(getPlayerDataNbt(), player, key);
	}
	
	public default void removePlayerData(Player player, String key)
	{
		NbtHelper.removePlayerData(getPlayerDataNbt(), player, key);
	}
	
	// =========== Timer
	
	// Get the value of non-player-specified timer (tick)
	public int getTimer(String key);
	
	// Get the value of player-specified timer (tick). (PS = player specified)
	public int getPlayerTimer(Player player, String key);
	
	public boolean hasTimer(String key);
	
	public boolean hasPlayerTimer(Player player, String key);
	
	// Setup a non-player-specified timer
	public IntTag setTimer(String key, int ticks);
	
	// Setup a player-specified timer. (PS = player specified)
	public IntTag setPlayerTimer(Player player, String key, int ticks);
	
	// Update all timers that should be executed every tick
	public void updateTimers();
	
	
	// ============ Related utils
	
	public static CBefriendableMob getCap(Mob mob)
	{
		Wrapped<CBefriendableMob> cap = new Wrapped<CBefriendableMob>(null);
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			cap.set(l);
		});
		return cap.get();
	}
	
	public static CompoundTag getCapNbt(Mob mob)
	{
		Wrapped<CompoundTag> tag = new Wrapped<CompoundTag>(null);
		mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			tag.set(l.getNbt());
		});
		return tag.get();
	}
	
	// Try adding hatred with given reason. This function will check in befriending handler if this reason is accepted,
	// post event and check if canceled.
	// Return if added hatred.
	public default boolean addHatredWithReason(Player player, BefriendableAddHatredReason reason)
	{
		BefriendingHandler handler = BefriendingTypeRegistry.getHandler(getOwner());
		int ticks = handler.getHatredDurationTicks(reason);
		if (handler.getAddHatredReasons() != null
			&& handler.getAddHatredReasons().contains(reason) 
			&& !(this.isInHatred(player) && this.getHatredDuration(player) > ticks))
		{
			BefriendableAddHatredEvent e = 
					new BefriendableAddHatredEvent(getOwner(), player, ticks, reason);
			boolean canceled = MinecraftForge.EVENT_BUS.post(e);
			if (!canceled)
			{
				addHatred(player, ticks);
				handler.onAddingHatred(getOwner(), player);
				return true;
			}
		}
		return false;
	}

}
