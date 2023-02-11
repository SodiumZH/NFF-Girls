package com.sodium.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import com.sodium.dwmg.util.Util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public class CapHostileMob implements ICapHostileMob {
	
	// The mob should be hostile to this entity
	protected UUID nowHostile = null;
	
	// Once this mob has ever been hostile to a certain mob, the latter's UUID will be present in this list
	// Some actions about befriending will check this, including taming, Death Affinity undead neutral, ...
	protected Vector<UUID> everBeenHostile = Vector<UUID>();
	
	public CapHostileMob() 
	{		
	}
	
	
	/* Cap Interface Overrides */
	
	@Override
	public UUID getHostileTo()
	{
		return Util.getUUIDIfExists(nowHostile);
	}
	
	@Override
	public Vector<UUID> getEverHostileTo()
	{
		return everBeenHostile;
	}

	@Override
	public boolean haveEverBeenHostile(LivingEntity mob)
	{
		return mob != null && everBeenHostile.contains(mob.getUUID());
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		tag.putUUID("now_hostile", nowHostile);
		ListTag hostileList = new ListTag();
		for (UUID id : everBeenHostile)
		{
			hostileList.add(NbtUtils.createUUID(id));
		}
		tag.put("ever_hostile", hostileList);
	}
	
	@Override 
	public void deserializeNBT(CompoundTag nbt)
	{
		this.nowHostile = nbt.getUUID("now_hostile");
		ListTag everHostileList = nbt.getList("ever_hostile", Tag.TAG_INT_ARRAY);
		everBeenHostile.clear();
		for(Tag tag : everHostileList)
		{
			//everBeenHostile.add(((IntArrayTag)tag).)
		}
	}
}
