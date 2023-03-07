package com.sodium.dwmg.befriendmobsapi.entitiy.befriending.registry;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import com.sodium.dwmg.befriendmobsapi.util.exceptions.DuplicatedRegistryEntryException;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// Define which type it will convert to after befriending for each befriendable mob
public class BefriendingTypeMapRegistry {

	BefriendingTypeMapRegistry()
	{
		FMLJavaModLoadingContext.get().getModEventBus().post(new BefriendingTypeMapRegisterEvent());
	};
	
	private static final BefriendingTypeMapRegistry REGISTRY = new BefriendingTypeMapRegistry(); 
	
	private ArrayList<BefriendingTypeMapRegistryEntry> map = new ArrayList<BefriendingTypeMapRegistryEntry>();
	
	public static EntityType<?> getConvertTo(EntityType<?> type)
	{
		for (BefriendingTypeMapRegistryEntry entry: REGISTRY.map)
		{
			if (entry.beforeType.equals(type))
			{
				return entry.afterType;
			}
		}
		return null;
	}
	
	public static void add(@Nonnull EntityType<?> before, @Nonnull EntityType<?> after, boolean override)
	{
		for (BefriendingTypeMapRegistryEntry entry: REGISTRY.map)
		{
			if (entry.beforeType.equals(before))
			{
				if (override)
				{
					REGISTRY.map.remove(entry);
					break;
				}
				else
				{
					throw new DuplicatedRegistryEntryException("Duplication registering befriending type maps. Trying register befriendable: " + before.getRegistryName().toString()
							+ " with existing befriended type: " + entry.beforeType.getRegistryName().toString() + ". If need to register anyway, set override true to remove the existing one.");
				}
			}
		}
		REGISTRY.map.add(new BefriendingTypeMapRegistryEntry(before, after));
	}
	
	public static void add(@Nonnull EntityType<?> before, @Nonnull EntityType<?> after)
	{
		add(before, after, false);
	}

}
