package net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.AbstractBefriendingHandler;
import net.sodiumstudio.dwmg.befriendmobs.util.exceptions.DuplicatedRegistryEntryException;

// Define which type it will convert to after befriending for each befriendable mob
public class BefriendingTypeRegistry {

	private BefriendingTypeRegistry()
	{
	};
	
	private static final BefriendingTypeRegistry REGISTRY = new BefriendingTypeRegistry(); 
	
	private static class Entry
	{
		public EntityType<?> fromType = null;
		public EntityType<?> convertToType = null;
		public AbstractBefriendingHandler handler = null;
		
		public Entry(EntityType<?> before, EntityType<?> after, AbstractBefriendingHandler handler)
		{
			this.fromType = before;
			this.convertToType = after;
			this.handler = handler;
		}
	}
		
	private ArrayList<Entry> map = new ArrayList<Entry>();

	/* Register */
	
	public static void register(@Nonnull EntityType<?> from, @Nonnull EntityType<?> convertTo, @Nonnull AbstractBefriendingHandler handler, boolean override)
	{
		for (Entry entry: REGISTRY.map)
		{
			if (entry.fromType.equals(from))
			{
				if (override)
				{
					REGISTRY.map.remove(entry);
					break;
				}
				else
				{
					return;
				}
			}
		}
		Entry newEntry = new Entry(from, convertTo, handler);
		REGISTRY.map.add(newEntry);
	}
	
	public static void register(@Nonnull EntityType<?> fromType, @Nonnull EntityType<?> convertToType, @Nonnull AbstractBefriendingHandler handler)
	{
		register(fromType, convertToType, handler, false);
	}

	/* Search */
	
	private static Entry getEntryFromType(EntityType<?> fromType)
	{
		for (Entry entry: REGISTRY.map)
		{
			if (entry.fromType.equals(fromType))
			{
				return entry;
			}
		}
		return null;
	}
	
	public static EntityType<?> getConvertTo(EntityType<?> fromType)
	{
		Entry entry = getEntryFromType(fromType);
		if (entry != null)
			return entry.convertToType;
		else return null;
	}
	
	public static AbstractBefriendingHandler getHandler(EntityType<?> fromType)
	{
		Entry entry = getEntryFromType(fromType);
		if (entry != null)
			return entry.handler;
		else return null;
	}
	
	public static boolean contains(EntityType<?> fromType)
	{
		for (Entry entry: REGISTRY.map)
		{
			if (entry.fromType.equals(fromType))
			{
				return true;
			}
		}
		return false;
	}
	
}
