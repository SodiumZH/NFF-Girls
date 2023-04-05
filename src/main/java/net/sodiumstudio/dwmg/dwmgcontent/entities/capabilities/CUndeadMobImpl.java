package net.sodiumstudio.dwmg.dwmgcontent.entities.capabilities;

import java.util.UUID;
import java.util.HashSet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.MiscUtil;

public class CUndeadMobImpl implements CUndeadMob {

	// Once this mob has ever been hostile to a certain mob, the latter's UUID will be present in this list
	// Some actions about befriending will check this, including taming, Death Affinity undead neutral, ...
	protected HashSet<UUID> hatred = new HashSet<UUID>();
	
	public CUndeadMobImpl() 
	{		
	}

	/* Cap Interface Overrides */
	

	@Override
	public HashSet<UUID> getHatred() 
	{
		return hatred;
	}

	@Override
	public void addHatred(LivingEntity entity, int forgiveTime) 
	{
		if(!hatred.contains(entity.getUUID()))
		{
			hatred.add(entity.getUUID());
			forgivingTimers.put(entity.getStringUUID(), IntTag.valueOf(forgiveTime));
		}
	}
	
	@Override
	public CompoundTag serializeNBT() 
	{
		CompoundTag tag = new CompoundTag();
		NbtHelper.serializeUUIDSet(tag, hatred, "hatred");
		tag.put("forgiving_timers", forgivingTimers);
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		hatred = NbtHelper.deserializeUUIDSet(nbt, "hatred");
		forgivingTimers = nbt.getCompound("forgiving_timers");
	}
	
	// Key is entity UUID string and value is time in ticks
	protected CompoundTag forgivingTimers = new CompoundTag();
	
	public void updateForgivingTimers()
	{
		HashSet<String> toRemove = new HashSet<String>();
		for (String key: forgivingTimers.getAllKeys())
		{
			if (forgivingTimers.contains(key, 3))
			{
				int val = forgivingTimers.getInt(key);
				if (val > 0)
				{
					val--;
					if (val == 0)
					{
						// Timer up, label to remove
						toRemove.add(key);
					}
					else
					{
						// count down
						forgivingTimers.put(key, IntTag.valueOf(val));
					}
				}
			}
		}
		for (String str: toRemove)
		{
			hatred.remove(UUID.fromString(str));
			forgivingTimers.remove(str);
		}
		
	}
	
	
	
}

