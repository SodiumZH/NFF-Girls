package net.sodiumstudio.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Util;

public class CUndeadMobImpl implements CUndeadMob {

	// Once this mob has ever been hostile to a certain mob, the latter's UUID will be present in this list
	// Some actions about befriending will check this, including taming, Death Affinity undead neutral, ...
	protected Vector<UUID> hatred = new Vector<UUID>();
	
	public CUndeadMobImpl() 
	{		
	}

	/* Cap Interface Overrides */
	

	@Override
	public Vector<UUID> getHatred() 
	{
		return hatred;
	}

	@Override
	public void addHatred(LivingEntity entity) 
	{
		if(!hatred.contains(entity.getUUID()))
		{
			hatred.add(entity.getUUID());
		}
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		NbtHelper.serializeUUIDArray(tag, hatred, "hatred");
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		hatred = NbtHelper.deserializeUUIDArray(nbt, "hatred");
	}
	
}

