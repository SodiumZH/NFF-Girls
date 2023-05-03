package net.sodiumstudio.dwmg.entities.capabilities;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

public interface CUndeadMob extends INBTSerializable<CompoundTag>{

	// Get this mob's hatred list
	public HashSet<UUID> getHatred();
	
	// Add a living entity to the hatred list
	// Forgive after 15 min by default
	public default void addHatred(LivingEntity entity)
	{
		addHatred(entity, 18000);
	}
	
	// Add a living entity to the hatred list
	// Param forgiveTime in tick, -1 means never forgive
	public void addHatred(LivingEntity entity, int forgiveTime);
	
}
