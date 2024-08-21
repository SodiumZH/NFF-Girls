package net.sodiumstudio.befriendmobs.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entity.befriended.CBefriendedMobData;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendableMobRegistry;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.CAttributeMonitorProvider;
import net.sodiumstudio.befriendmobs.entity.capability.CBMPlayerModule;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMobProvider;
import net.sodiumstudio.befriendmobs.entity.capability.CHealingHandlerProvider;
import net.sodiumstudio.befriendmobs.entity.capability.CLivingEntityDelayedActionHandler;
import net.sodiumstudio.befriendmobs.entity.capability.wrapper.IAttributeMonitor;
import net.sodiumstudio.befriendmobs.entity.capability.wrapper.ILivingDelayedActions;
import net.sodiumstudio.befriendmobs.item.capability.CItemStackMonitor;
import net.sodiumstudio.befriendmobs.item.capability.wrapper.IItemStackMonitor;
import net.sodiumstudio.befriendmobs.level.CBMLevelModule;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMCapabilityAttachment {

	// Attach capabilities
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
	
		if (event.getObject() instanceof LivingEntity living)
		{
			// Attribute change monitor
			if (living instanceof IAttributeMonitor)
			{
				CAttributeMonitorProvider prvd = new CAttributeMonitorProvider(living);
				event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_attribute_monitor")
						, prvd);
			}
			// Item Stack monitor
			if (living instanceof IItemStackMonitor)
			{
				CItemStackMonitor.Prvd prvd1 = new CItemStackMonitor.Prvd(living);
				event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_item_stack_monitor"), prvd1);
			}
			// Delay action handler
			if (living instanceof ILivingDelayedActions)
			{
				CLivingEntityDelayedActionHandler.Prvd prvd = new CLivingEntityDelayedActionHandler.Prvd(living);
				event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_delay_action_handler"), prvd);
			}
		}			
		
		
		// CBefriendableMob
		if (event.getObject() instanceof Mob mob) {
			if (BefriendingTypeRegistry.contains((EntityType<? extends Mob>) mob.getType())
					&& !(mob instanceof IBefriendedMob)) 
			{
				event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_befriendable"),
						new CBefriendableMobProvider(mob));
				if (BefriendingTypeRegistry.contains((EntityType<? extends Mob>) mob.getType()) 
						&& BefriendingTypeRegistry.getHandler((EntityType<? extends Mob>) mob.getType()) != null)
				{
					// Initialize capability (defined in handlers)
					mob.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> 
					{
						BefriendableMobRegistry.put(mob);
					});
				}
			}
		}

		if (event.getObject() instanceof IBefriendedMob bm)
		{
			// Temp data (CBefriendedMobData)
			// Renamed key in 0.x.25 from "cap_befriended_mob_temp_data" to "cap_befriended_mob_data"
			/*event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_befriended_mob_temp_data"),
					new CBefriendedMobData.Prvd(bef));*/
			event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_befriended_mob_data"),
					new CBefriendedMobData.Prvd(bm));
			
			
			// CHealingHandler
			if (bm.healingHandlerClass() != null)
			{
				try
				{
					event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_healing_handler"), 
						new CHealingHandlerProvider(
							// Implementation class defined in IBefriendedMob implementation
							bm.healingHandlerClass().getDeclaredConstructor(LivingEntity.class).newInstance(bm.asMob()), 
							bm.asMob()));
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		
		// CBaubleDataCache
		/*if (event.getObject() instanceof IBaubleEquipable b)
		{
			event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_bauble_data_cache"), 
					new CBaubleDataCache.Prvd(b));
		}*/
		
		// CBMPlayerModule
		if (event.getObject() instanceof Player p)
		{
			event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_bm_player"), 
					new CBMPlayerModule.Prvd(p));
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void attachLevelCapabilities(AttachCapabilitiesEvent<Level> event)
	{
		if (event.getObject() instanceof ServerLevel sl)
		{
			event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_bm_level"), 
					new CBMLevelModule.Prvd(sl));
		}
	}
}
