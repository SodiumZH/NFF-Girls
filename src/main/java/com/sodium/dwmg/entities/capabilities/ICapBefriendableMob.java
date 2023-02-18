package com.sodium.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapBefriendableMob extends INBTSerializable<CompoundTag> {

	// Get this mob's hatred list
	// Once a player gets onto the hatred list, he/she will be permanently unable to befriend this mob.
	Vector<UUID> getHatred();
	
	// Add a player to the hatred list
	// This action is permanent and there's no method to remove a player
	void addHatred(Player player);
	
}
