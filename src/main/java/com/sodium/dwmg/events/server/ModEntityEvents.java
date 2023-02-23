package com.sodium.dwmg.events.server;

import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import org.jetbrains.annotations.NotNull;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.entities.capabilities.ICapUndeadMob;
import com.sodium.dwmg.registries.ModCapabilities;
import com.sodium.dwmg.registries.ModEffects;
import com.sodium.dwmg.registries.ModItems;
import com.sodium.dwmg.util.Debug;
import com.sodium.dwmg.util.TagHelper;
import com.sodium.dwmg.util.Util;
import com.sodium.dwmg.util.Util.GlobalBoolean;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntityEvents
{

	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event)
	{
		Entity target = event.getTarget();
		Player player = event.getPlayer();
		InteractionResult result = InteractionResult.PASS;
		boolean isClientSide = event.getSide() ==  LogicalSide.CLIENT;
		boolean isMainHand = event.getHand() == InteractionHand.MAIN_HAND;
		
		Debug.printToScreen("Entity interaction on client, Side: " + event.getSide().toString() + ", Hand: " + event.getHand().toString(), target, player);
	
			// Mob interaction start //
			if (target != null && target instanceof LivingEntity living)
			{
				// Handle befriendable mob start //
				if(living.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).isPresent() && !(living instanceof IBefriendedMob))
				{
					living.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
						if (isMainHand)
						{
							if (player.getMainHandItem().getItem() == ModItems.DEBUG_BEFRIENDER.get())
							{
								if (!isClientSide)
									Dwmg.befriendingMethodGetter.get().befriend(player, living);
								InteractionResult.sidedSuccess(isClientSide);
							}
							else
							{
								Dwmg.befriendingMethodGetter.get().onBefriendingMobInteract(event);
							}
							
						}
					});
				}
				// Handle befriendable mob end //
				// Handle befriended mob start //
				else if (living instanceof IBefriendedMob bef)
				{
					if (isMainHand)
					{				
						if (player.isShiftKeyDown() && player.getMainHandItem().getItem() == ModItems.DEBUG_BEFRIENDER.get())
						{
							bef.init(player, null);
							result = InteractionResult.sidedSuccess(isClientSide);
						}
						else 
						{					
							result = (player.isShiftKeyDown() ? bef.onInteractionShift(player) : bef.onInteraction(player))
									? InteractionResult.sidedSuccess(isClientSide) : result;
						}
					}
				}
				// Handle befriended mob end //
			}
			// Mob interaction end //
		
		// Server events end //
		// Client events start //
		else
		{
			//Debug.printToScreen("Entity interacting on client: " + event.getHand().toString(), player, target);
		}
		// Client events end //
		event.setCancellationResult(result);
	}
	
	
	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event)
	{
		LivingEntity lastHurtBy = event.getEntityLiving().getLastHurtByMob();
		LivingEntity target = event.getTarget();		
		Util.GlobalBoolean isCancelledByEffect = Util.createGB(false);
		
		// Handle mobs //
		if (target != null && event.getEntity() instanceof Mob mob)
		{
        	// Handle undead mobs start //
	        if (mob.getMobType() == MobType.UNDEAD) 
	        {
	        	// Handle CapUndeadMob //
        		mob.getCapability(ModCapabilities.CAP_UNDEAD_MOB).ifPresent((l) ->
        		{
        			if (target != null && target.hasEffect(ModEffects.DEATH_AFFINITY.get()) && lastHurtBy != target && !l.getHatred().contains(target.getUUID()))
        			{
        				mob.setTarget(null);
        				isCancelledByEffect.set(true);
        			}
        			else if(target != null)
        			{
        				l.addHatred(target);
        			}
        		});
        		// Handle CapUndeadMob end //
		    } 
	        // Handle undead mobs end //
	        // Handle befriendable mobs //
	        if (target instanceof Player player && mob.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
	        {
	        	mob.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
	        	{
	        		// Add to hatred list (disable befriending permanently)
	        		if(target != null && !l.getHatred().contains(player.getUUID()) && !isCancelledByEffect.get())
	        		{
	        			l.addHatred(player);
	        			Debug.printToScreen("Player " + Util.getNameString(player) + " put into hatred list by " + Util.getNameString(mob), player, player);
	        		}
	        	});
	        }
	        // Handle befriendable mobs end //
		}
		// Handle mobs end //
	}	
}
