package com.sodium.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.util.NbtHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapBefriendableMob extends INBTSerializable<CompoundTag> {

	// ======== Hatred List related
	
	// Get this mob's hatred list
	// Once a player gets onto the hatred list, it will be permanently unable to befriend this mob.
	public Vector<UUID> getHatred();
	
	// Add a player to the hatred list
	// This action is permanent and there's no method to remove a player
	public void addHatred(Player player);

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
	
	// Setup a non-player-specified timer
	public IntTag setTimer(String key, int ticks);
	
	// Setup a player-specified timer. (PS = player specified)
	public IntTag setTimerPS(Player player, String key, int ticks);
	
	// Update all timers that should be executed every tick
	public void updateTimers();
}
