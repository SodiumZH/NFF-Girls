package net.sodiumstudio.dwmg.befriendmobs.entity.befriending;

import java.util.UUID;

import net.sodiumstudio.dwmg.befriendmobs.events.AddHatredEvent;

public class LivingHatred
{
	public UUID uuid;
	public AddHatredEvent.Reason reason;
	/*
	public void saveToTag(CompoundTag tag)
	{
		if (!tag.contains("hatred_list_befriendmobs", NbtHelper.TagType.TAG_COMPOUND.getID()))
		{
			tag.put("hatred_list_befriendmobs", new CompoundTag());
		}
		
	}*/
}
