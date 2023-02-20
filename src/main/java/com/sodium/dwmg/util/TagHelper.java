package com.sodium.dwmg.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class TagHelper 
{
	
	// Check if an entity has a tag
	public static boolean hasTag(Entity obj, String domain, String tag)
	{
		TagKey<EntityType<?>> tagKey = ForgeRegistries.ENTITIES.tags().createTagKey(new ResourceLocation(domain, tag));
		return ForgeRegistries.ENTITIES.tags().getTag(tagKey).contains(obj.getType());
	}
}
