package net.sodiumstudio.dwmg.befriendmobs.entitiy.capability;

import java.util.HashSet;
import java.util.UUID;
import java.util.HashSet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;

public interface CBefriendableMob extends INBTSerializable<CompoundTag> {

	public Mob getOwner();
	
	// ======== Hatred List related
	
	// Get this mob's hatred list
	// Sometimes hatred list prevents player to befriend the mob
	public HashSet<UUID> getHatred();
	
	// Add a player to the hatred list
	// If a player is in hatred list, it will take some time to remove from it. 15min by default.
	// Or input -1 to add permanent hatred.
	public void addHatred(Player player, int ticks);
	
	public default void addHatred(Player player)
	{
		addHatred(player, 180);
	}

	public default void addHatredPermanent(Player player)
	{
		addHatred(player, -1);
	}
	
	
	// Check if a player is in the hatred list
	public boolean isInHatred(Player player);
	
	// ======== Serialization
	
	// Get NBT tag
	public CompoundTag getNBT();

	// Get the player data tag under NBT
	public CompoundTag getPlayerData();
	
	// =========== Timer
	
	// Get the value of non-player-specified timer (tick)
	public int getTimer(String key);
	
	// Get the value of player-specified timer (tick). (PS = player specified)
	public int getTimerPS(Player player, String key);
	
	public boolean hasTimer(String key);
	
	public boolean hasPlayerTimer(Player player, String key);
	
	// Setup a non-player-specified timer
	public IntTag setTimer(String key, int ticks);
	
	// Setup a player-specified timer. (PS = player specified)
	public IntTag setTimerPS(Player player, String key, int ticks);
	
	// Update all timers that should be executed every tick
	public void updateTimers();
}
