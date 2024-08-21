package net.sodiumstudio.befriendmobs.temp;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.nautils.events.entity.EntityFinalizeLoadingEvent;
import net.sodiumstudio.nautils.events.entity.EntityLoadEvent;

/**
 * Dedicated event handlers only for temporary solutions when API changes. Should be removed in final releases.
 */
@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TempPortingEvents
{
	@SubscribeEvent
	public static void beforeLoad(EntityLoadEvent event)
	{
		if (!event.getNBT().getCompound("ForgeCaps").getCompound(BefriendMobs.MOD_ID + ":cap_befriended_mob_temp_data").isEmpty())
		{
			event.getNBT().getCompound("ForgeCaps").put(BefriendMobs.MOD_ID + ":" + "cap_befriended_mob_data", 
					event.getNBT().getCompound("ForgeCaps").getCompound(BefriendMobs.MOD_ID + ":cap_befriended_mob_temp_data").copy());
			event.getNBT().getCompound("ForgeCaps").remove(BefriendMobs.MOD_ID + ":" + "cap_befriended_mob_temp_data");
		}
	}
	
	// TODO Move it to CBefriendedMobData#deserializeNBT
	@SubscribeEvent
	public static void afterLoad(EntityFinalizeLoadingEvent event)
	{
		IBefriendedMob.ifBM(event.getEntity(), bm -> {
			bm.updateFromInventory();
			bm.init(bm.getOwnerUUID(), null);
			bm.setInit();
		});
	}
}
