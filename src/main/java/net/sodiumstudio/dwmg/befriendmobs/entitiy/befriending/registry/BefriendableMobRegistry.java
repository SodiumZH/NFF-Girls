package net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Mob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;

public class BefriendableMobRegistry
{

	// A list to register all befriendable mobs in the whole game
	private static BefriendableMobRegistry REGISTRY = new BefriendableMobRegistry();
	
	private HashMap<UUID, Mob> mobMap = new HashMap<UUID, Mob>();
	
	public static void put(Mob mob)
	{
		if (!mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			throw new IllegalArgumentException("Mob put into BefriendableMobRegistry must have CBefriendableMob capability.");
		if (!mob.isAlive())
			return;
		REGISTRY.mobMap.put(mob.getUUID(), mob);
	}
	
	@Nullable
	public static Mob get(UUID uuid)
	{
		if (!REGISTRY.mobMap.containsKey(uuid))
			return null;
		else if (!REGISTRY.mobMap.get(uuid).isAlive())
			return null;
		else return REGISTRY.mobMap.get(uuid);
	}
	
	public static boolean contains(UUID uuid)
	{
		if (!REGISTRY.mobMap.containsKey(uuid))
			return false;
		else if (!REGISTRY.mobMap.get(uuid).isAlive())
			return false;
		else return true;
	}
	
	// Clean invalid references (not alive) of mobs in the registry
	public static void clean()
	{
		HashSet<UUID> toRemove = new HashSet<UUID>();
		for (UUID uuid: REGISTRY.mobMap.keySet())
		{
			if (REGISTRY.mobMap.get(uuid) == null || !REGISTRY.mobMap.get(uuid).isAlive())
			{
				toRemove.add(uuid);
			}
		}
		for (UUID uuid: toRemove)
		{
			REGISTRY.mobMap.remove(uuid);
		}
	}
	
	public static Set<UUID> allUUIDs()
	{
		return REGISTRY.mobMap.keySet();
	}
	
	public static Collection<Mob> allMobs()
	{
		return REGISTRY.mobMap.values();
	}
	
	public static HashSet<CBefriendableMob> allCaps()
	{
		HashSet<CBefriendableMob> set = new HashSet<CBefriendableMob>();
		for (Mob mob: allMobs())
		{
			set.add(CBefriendableMob.getCap(mob));
		}
		return set;
	}
}
