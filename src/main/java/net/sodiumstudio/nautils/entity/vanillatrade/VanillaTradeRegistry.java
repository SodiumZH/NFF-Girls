package net.sodiumstudio.nautils.entity.vanillatrade;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;

/**
 * The registry for NaUtils Vanilla-Merchant interface.
 */
public class VanillaTradeRegistry
{

	private static final Map<EntityType<? extends Mob>, Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>>> TABLE = new HashMap<>();
	
	static Int2ObjectMap<List<VillagerTrades.ItemListing>> getTrades(EntityType<? extends Mob> mobType, VillagerProfession profession)
	{
		if (!TABLE.containsKey(mobType))
			throw new IllegalArgumentException("NaUtils Vanila Merchant: mob type not registered: " + mobType.getDescriptionId());
		if (!TABLE.get(mobType).containsKey(profession))
			throw new IllegalArgumentException("NaUtils Vanila Merchant: mob profession not registered: " + profession.name());
		return TABLE.get(mobType).get(profession);
	}
	
	static Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> getRaw(EntityType<? extends Mob> type)
	{
		return TABLE.get(type);
	}
	
	static void putRaw(EntityType<? extends Mob> type, Map<VillagerProfession, Int2ObjectMap<List<VillagerTrades.ItemListing>>> listings)
	{
		TABLE.put(type, listings);
	}
	
	static void putTrades(EntityType<? extends Mob> mobType, VillagerProfession profession, int xpLevel, VillagerTrades.ItemListing... listings)
	{
		if (!TABLE.containsKey(mobType))
			TABLE.put(mobType, new HashMap<>());
		if (!TABLE.get(mobType).containsKey(profession))
			TABLE.get(mobType).put(profession, new Int2ObjectOpenHashMap<>());
		if (!TABLE.get(mobType).get(profession).containsKey(xpLevel))
			TABLE.get(mobType).get(profession).put(xpLevel, new ArrayList<>());
		if (listings.length > 0)
			for (int i = 0; i < listings.length; ++i)
			{
				TABLE.get(mobType).get(profession).get(xpLevel).add(listings[i]);
			}
	}
	
	static void putTrades(EntityType<? extends Mob> mobType, VillagerProfession profession, int xpLevel, Collection<VillagerTrades.ItemListing> listings)
	{
		putTrades(mobType, profession, xpLevel);
		for (var listing: listings)
		{
			TABLE.get(mobType).get(profession).get(xpLevel).add(listing);
		}
	}
	
	static List<VillagerTrades.ItemListing> getTrades(EntityType<?> mobType, VillagerProfession profession, int xpLevel)
	{
		if (!TABLE.containsKey(mobType))
			throw new IllegalArgumentException("mob type not registered");
		if (!TABLE.get(mobType).containsKey(profession))
			throw new IllegalArgumentException("profession not registered");
		if (!TABLE.get(mobType).get(profession).containsKey(xpLevel))
			throw new IllegalArgumentException("xp level invalid");
		return TABLE.get(mobType).get(profession).get(xpLevel);
	}
	
	@SuppressWarnings("unchecked")
	static List<VillagerTrades.ItemListing> getTrades(Mob mob, VillagerProfession profession, int xpLevel)
	{
		return getTrades((EntityType<? extends Mob>) mob.getType(), profession, xpLevel);
	}
	
	static Int2ObjectMap<List<VillagerTrades.ItemListing>> getAllTrades(EntityType<?> mobType, VillagerProfession profession)
	{
		if (!TABLE.containsKey(mobType))
			throw new IllegalArgumentException("mob type not registered");
		if (!TABLE.get(mobType).containsKey(profession))
			throw new IllegalArgumentException("profession not registered");
		return TABLE.get(mobType).get(profession);
	}
	
	public static ImmutableList<VillagerTrades.ItemListing> getTradesImmutable(EntityType<?> mobType, VillagerProfession profession, int xpLevel)
	{
		if (!TABLE.containsKey(mobType) || !TABLE.get(mobType).containsKey(profession) || !TABLE.get(mobType).get(profession).containsKey(xpLevel))
			return ImmutableList.of();
		return ImmutableList.copyOf(getTrades(mobType, profession, xpLevel));
	}
	
	public static ImmutableList<VillagerTrades.ItemListing> getTradesImmutable(Mob mob, VillagerProfession profession, int xpLevel)
	{
		return ImmutableList.copyOf(getTrades(mob, profession, xpLevel));
	}
	
	public static boolean contains(EntityType<?> type)
	{
		return TABLE.containsKey(type);
	}
}
