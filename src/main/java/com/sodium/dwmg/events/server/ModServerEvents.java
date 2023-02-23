package com.sodium.dwmg.events.server;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingChangeTargetEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.registries.ModEffects;
import com.sodium.dwmg.util.Debug;


@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModServerEvents 
{

	@SubscribeEvent
	public static void onServerStart(ServerStartedEvent event)
	{
		@SuppressWarnings("unused")
		int test = 1;
		
	}
	

	@SubscribeEvent
	public static void onWorldTick(TickEvent.WorldTickEvent event)
	{
		
	}
	/*
		@SubscribeEvent
		public static void onLivingChangeTargetEvent(LivingChangeTargetEvent event) 
		{
			LivingEntity lastHurtBy = event.getEntityLiving().getLastHurtByMob();
			
			if (event.getNewTarget() != null && event.getEntity() instanceof Mob mob) 
			{
				// Undead mobs keep neutral to mobs with Death Affinity effect
				if (mob.getMobType() == MobType.UNDEAD) 
				{
					if (event.getNewTarget().hasEffect(ModEffects.DEATH_AFFINITY.get())) 
					{
						if (event.getEntityLiving().getLastHurtByMob() != event.getNewTarget()) 
						{
							mob.setTarget(null);
							Dwmg.logInfo("Death Affinity worked");
							Dwmg.logInfo("Last hurt by: " + Debug.getMobNameString(lastHurtBy));
						}
						else
						{
							Dwmg.logInfo("Death Affinity not working because attacked");
							Dwmg.logInfo("Last hurt by: " + Debug.getMobNameString(lastHurtBy));
						}
					}
				}
			}
		}
	}
*/
}