package com.sodium.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

public interface ICapHostileMob extends INBTSerializable<CompoundTag>{

	// UUID of the living entity this mob is now hostile to.
	UUID getHostileTo();
	
	// UUIDs of living entities this mob has ever been hostile to.
	Vector<UUID> getEverHostileTo();
	
	// Check if the mob has ever been hostile to the input mob.
	boolean haveEverBeenHostile(LivingEntity mob);
	
}
