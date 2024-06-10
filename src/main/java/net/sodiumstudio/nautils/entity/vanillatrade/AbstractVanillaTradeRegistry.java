package net.sodiumstudio.nautils.entity.vanillatrade;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class AbstractVanillaTradeRegistry<T extends IVanillaTradeListing>
{
	private Map<ResourceLocation, Map<VillagerProfession, VanillaTradeListings<T>>> table = new HashMap<>();

	public AbstractVanillaTradeRegistry() {}
	
	// Getters
	
	protected Map<ResourceLocation, Map<VillagerProfession, VanillaTradeListings<T>>> getRaw()
	{
		return this.table;
	}
	
	@Nullable
	public Map<VillagerProfession, VanillaTradeListings<T>> getAllListings(ResourceLocation key)
	{
		if (key == null) return null;
		return table.get(key);
	}
	
	@Nullable
	public Map<VillagerProfession, VanillaTradeListings<T>> getAllListings(String key)
	{
		if (key == null) return null;
		return getAllListings(new ResourceLocation(key));
	}
	
	@Nullable
	public Map<VillagerProfession, VanillaTradeListings<T>> getAllListings(EntityType<?> ofType)
	{
		if (ofType == null) return null;
		return getAllListings(ForgeRegistries.ENTITIES.getKey(ofType));
	}
	
	@Nullable
	public VanillaTradeListings<T> getListings(ResourceLocation key, @Nullable VillagerProfession profession)
	{
		if (profession == null) profession = VillagerProfession.NONE;
		var allListings = this.getAllListings(key);
		if (allListings == null) return null;
		return allListings.get(profession);
	}
	
	@Nullable
	public VanillaTradeListings<T> getListings(String key, @Nullable VillagerProfession profession)
	{
		if (key == null) return null;
		return this.getListings(new ResourceLocation(key), profession);
	}
	
	@Nullable
	public VanillaTradeListings<T> getListings(EntityType<?> ofType, @Nullable VillagerProfession profession)
	{
		if (ofType == null) return null;
		return this.getListings(ForgeRegistries.ENTITIES.getKey(ofType), profession);
	}
	
	@Nullable
	public VanillaTradeListings<T> getListings(ResourceLocation key)
	{
		return this.getListings(key, null);
	}
	
	@Nullable
	public VanillaTradeListings<T> getListings(String key)
	{
		return this.getListings(key, null);
	}
	
	@Nullable
	public VanillaTradeListings<T> getListings(EntityType<?> ofType)
	{
		return this.getListings(ofType, null);
	}
	
	/**
	 * Check if the listings exists for a given key and profession.
	 * <p>
	 * Note: this method returning true only represents the key/profession has a
	 * Listings instance object, but not guarantees there're valid Listing instances
	 * in the Listings.
	 */
	public boolean hasListings(ResourceLocation key, VillagerProfession prof)
	{
		return this.table.containsKey(key) && this.table.get(key).containsKey(prof);
	}
	
	/**
	 * Check if the listings exists for a given key and profession.
	 * <p>
	 * Note: this method returning true only represents the key/profession has a
	 * Listings instance object, but not guarantees there're valid Listing instances
	 * in the Listings.
	 */
	public boolean hasListings(String key, VillagerProfession prof)
	{
		return this.hasListings(new ResourceLocation(key), prof);
	}
	
	/**
	 * Check if the listings exists for a given key and profession.
	 * <p>
	 * Note: this method returning true only represents the key/profession has a
	 * Listings instance object, but not guarantees there're valid Listing instances
	 * in the Listings.
	 */
	public boolean hasListings(EntityType<?> type, VillagerProfession prof)
	{
		return this.hasListings(ForgeRegistries.ENTITIES.getKey(type), prof);
	}
	
	public void putIfAbsent(ResourceLocation key, @Nullable VillagerProfession prof)
	{
		if (prof == null) prof = VillagerProfession.NONE;
		if (!this.table.containsKey(key))
			this.table.put(key, new HashMap<>());
		if (!this.table.get(key).containsKey(prof))
			this.table.get(key).put(prof, new VanillaTradeListings<>());
	}
	
	public void putIfAbsent(ResourceLocation key)
	{
		this.putIfAbsent(key, null);
	}

	public void putListing(ResourceLocation key, @Nullable VillagerProfession prof, T listing)
	{
		this.putIfAbsent(key, prof);
		this.table.get(key).get(prof).add(listing);
	}
	
}