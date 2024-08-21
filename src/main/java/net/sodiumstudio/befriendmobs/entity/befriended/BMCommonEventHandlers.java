package net.sodiumstudio.befriendmobs.entity.befriended;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.nautils.events.entity.MobCheckDespawnEvent;
import net.sodiumstudio.nautils.events.entity.MobSunBurnTickEvent;
import net.sodiumstudio.nautils.events.entity.MonsterPreventSleepEvent;

/**
 * Common event listeners for {@link IBefriendedMob}.
 */
@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMCommonEventHandlers
{
	/**
	 * Make BMs not preventing sleep
	 */
	@SubscribeEvent
	public static void preventSleep(MonsterPreventSleepEvent event)
	{
		IBefriendedMob.ifBM(event.getEntity(), bm -> {
			if (bm.getOwnerUUID().equals(event.getPlayer().getUUID()) || bm.canPreventOtherPlayersSleep(event.getPlayer()))
				event.setCanceled(true);
		});
	}
	
	/**
	 * Make BMs not despawning
	 */
	@SubscribeEvent
	public static void checkDespawn(MobCheckDespawnEvent event)
	{
		IBefriendedMob.ifBM(event.getEntity(), bm -> event.setCanceled(true));
	}
	
	/**
	 * Handle sun immunity
	 */
	@SubscribeEvent
	public static void onMobSunBurnTick(MobSunBurnTickEvent event)
	{
		if (event.getEntity() instanceof IBefriendedSunSensitiveMob bssm && bssm.isSunImmune())
			event.setCanceled(true);
	}
}
