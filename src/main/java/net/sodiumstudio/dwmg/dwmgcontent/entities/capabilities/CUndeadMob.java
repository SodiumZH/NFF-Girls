package net.sodiumstudio.dwmg.dwmgcontent.entities.capabilities;

import java.util.HashSet;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.util.INBTSerializable;

public interface CUndeadMob extends INBTSerializable<CompoundTag>{

	// Get this mob's hatred list
	public HashSet<UUID> getHatred();
	
	// Add a player to the hatred list
	// This action is permanent and there's no handler to remove a player=
	public void addHatred(LivingEntity entity);
	
}
