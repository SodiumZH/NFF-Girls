package net.sodiumstudio.nautils.entity.vanillatrade;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

/**
 * Post on common setup for registering mob trade listings that uses {@code CVanillaMerchant}.
 */
public class VanillaMerchantRegisterListingEvent extends Event implements IModBusEvent
{
	
	public Registerer push(EntityType<? extends Mob> type)
	{
		return new Registerer(type, this);
	}
	
	/**
	 * <b>Link</b> an entity type to another one (target). After linking, the listings of the two types will be identical,
	 * and changing one will change the other at the same time. 
	 * <b>Warning: If there're entries under "{@code type}" before linking, they will be wiped.
	 */
	public VanillaMerchantRegisterListingEvent link(EntityType<? extends Mob> type, EntityType<? extends Mob> target)
	{
		VanillaTradeRegistry.putRaw(type, VanillaTradeRegistry.getRaw(target));
		return this;
	}
	
	public static class Registerer
	{
		private EntityType<? extends Mob> type;
		private VanillaMerchantRegisterListingEvent event;
		private Registerer(EntityType<? extends Mob> type, VanillaMerchantRegisterListingEvent event)
		{
			this.type = type;
			this.event = event;
		}
		
		public Registerer addListings(VillagerProfession profession, int xpLevel, VillagerTrades.ItemListing... listings)
		{
			VanillaTradeRegistry.putTrades(type, profession, xpLevel, listings);
			return this;
		}
		
		public Registerer addListings(int xpLevel, VillagerTrades.ItemListing... listings)
		{
			return addListings(VillagerProfession.NONE, xpLevel, listings);
		}
		
		public Registerer addListings(VillagerTrades.ItemListing... listings)
		{
			return addListings(1, listings);
		}
		
		public VanillaMerchantRegisterListingEvent pop()
		{
			return event;
		}
	}
}
