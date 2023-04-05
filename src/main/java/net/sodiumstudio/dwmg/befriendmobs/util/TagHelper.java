package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public class TagHelper 
{
	
	// Check if an mob has a tag
	public static boolean hasTag(Entity obj, ResourceLocation tag)
	{
		TagKey<EntityType<?>> tagKey = ForgeRegistries.ENTITIES.tags().createTagKey(tag);
		return ForgeRegistries.ENTITIES.tags().getTag(tagKey).contains(obj.getType());
	}
	
	public static boolean hasTag(Entity obj, String domain, String tag)
	{
		return hasTag(obj, new ResourceLocation(domain, tag));
	}
	
	public static boolean hasTag(Item obj, ResourceLocation tag)
	{
		TagKey<Item> tagKey = ForgeRegistries.ITEMS.tags().createTagKey(tag);
		return ForgeRegistries.ITEMS.tags().getTag(tagKey).contains(obj);
	}
	
	public static boolean hasTag(Item obj, String domain, String tag)
	{
		return hasTag(obj, new ResourceLocation(domain, tag));
	}
	
}
