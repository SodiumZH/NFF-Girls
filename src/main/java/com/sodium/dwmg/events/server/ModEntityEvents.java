package com.sodium.dwmg.events.server;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.entities.capabilities.ICapUndeadMob;
import com.sodium.dwmg.registries.ModCapabilities;
import com.sodium.dwmg.registries.ModEffects;
import com.sodium.dwmg.util.Debug;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Dwmg.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntityEvents
{

	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event)
	{
		if(event.getTarget() != null && event.getTarget() instanceof LivingEntity mob)
		{
			if(mob.getMobType() == MobType.UNDEAD)
			{
				LazyOptional<ICapUndeadMob> undeadCap = mob.getCapability(ModCapabilities.CAP_UNDEAD_MOB);
				if(undeadCap.isPresent())
					if(!mob.getLevel().isClientSide())
						event.getPlayer().sendMessage(new TextComponent("This is an undead mob."), event.getPlayer().getUUID());
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event)
	{
		LivingEntity lastHurtBy = event.getEntityLiving().getLastHurtByMob();
		if (event.getTarget() != null && event.getEntity() instanceof Mob mob)
		{
        	// Undead mobs handler start //
	        // Undead mobs keep neutral to mobs with Death Affinity effect 
	        if (mob.getMobType() == MobType.UNDEAD) 
	        {
	        	mob.getCapability(ModCapabilities.CAP_UNDEAD_MOB).ifPresent((cp) ->
	        	{
	        		if (event.getTarget() != null && event.getTarget().hasEffect(ModEffects.DEATH_AFFINITY.get()) && lastHurtBy != event.getTarget() && cp.getBeingHostileTo() != event.getTarget().getUUID())
	        		{
	        			mob.setTarget(null);
	        			cp.setHostileTo(null);
	        		}
	        		else
	        		{
	        			cp.setHostileTo(event.getTarget());
	        		}
	        	});
	        }
	    } 
	}
	
	
}
