package com.sodium.dwmg.entities.capabilities;

import java.util.UUID;
import java.util.Vector;

import com.sodium.dwmg.util.NbtHelper;
import com.sodium.dwmg.util.Util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;

public class CapUndeadMob implements ICapUndeadMob {
	
	// The mob should be hostile to this entity
	protected UUID nowHostile = null;
	
	// Once this mob has ever been hostile to a certain mob, the latter's UUID will be present in this list
	// Some actions about befriending will check this, including taming, Death Affinity undead neutral, ...
	protected Vector<UUID> everBeenHostile = new Vector<UUID>();
	
	public CapUndeadMob() 
	{		
	}
	
	
	/* Cap Interface Overrides */
	
	@Override
	public UUID getBeingHostileTo()
	{
		return nowHostile;
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
		// Now hostile
		tag.putUUID("now_hostile", nowHostile);
		// Ever been hostile as list
		NbtHelper.serializeUUIDArray(tag, everBeenHostile, "ever_hostile");
		return tag;
	}
	
	@Override 
	public void deserializeNBT(CompoundTag nbt)
	{
		// Load now hostile
		this.nowHostile = nbt.getUUID("now_hostile");
		// Load been hostile list
		ListTag everHostileList = nbt.getList("ever_hostile", Tag.TAG_INT_ARRAY);
		everBeenHostile.clear();
		for(Tag tag : everHostileList)
		{
			everBeenHostile.add(NbtUtils.loadUUID(tag));
		}
	}
	
	@Override
	public void setHostileTo(LivingEntity mob) {
		if(mob != null)
		{
			nowHostile = mob.getUUID();
			if(!everBeenHostile.contains(mob.getUUID()))
			{
				everBeenHostile.add(mob.getUUID());
			}
		}
		else
		{
			nowHostile = Util.UUID_NULL;
		}
	}
}
