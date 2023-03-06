package com.sodium.dwmg.befriendmobsapi.events;

import java.util.Vector;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.befriendmobsapi.BefriendMobsAPI;
import com.sodium.dwmg.befriendmobsapi.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobsapi.entitiy.befriending.BefriendableMobInteractArguments;
import com.sodium.dwmg.befriendmobsapi.entitiy.befriending.BefriendableMobInteractionResult;
import com.sodium.dwmg.befriendmobsapi.registry.RegCapabilities;
import com.sodium.dwmg.befriendmobsapi.registry.RegItems;
import com.sodium.dwmg.befriendmobsapi.util.Debug;
import com.sodium.dwmg.befriendmobsapi.util.EntityHelper;
import com.sodium.dwmg.befriendmobsapi.util.Util;
import com.sodium.dwmg.registries.ModCapabilities;
import com.sodium.dwmg.registries.ModEffects;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

// TODO: change modid after isolation
@Mod.EventBusSubscriber(modid = BefriendMobsAPI.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler
{

	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event)
	{
		Entity target = event.getTarget();
		Player player = event.getPlayer();
		// Wrap result as a vector to set in lambda
		Vector<InteractionResult> result = new Vector<InteractionResult>();
		result.add(InteractionResult.PASS);
		boolean isClientSide = event.getSide() ==  LogicalSide.CLIENT;
		boolean isMainHand = event.getHand() == InteractionHand.MAIN_HAND;
		
		Debug.printToScreen("Entity interaction on client, Side: " + event.getSide().toString() + ", Hand: " + event.getHand().toString(), target, player);
	
			// Mob interaction start //
			if (target != null && target instanceof LivingEntity living)
			{
				if (living.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).isPresent() && !(living instanceof IBefriendedMob))
				{
					living.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
						if (player.getMainHandItem().getItem() == RegItems.DEBUG_BEFRIENDER.get())
						{
							if (!isClientSide && isMainHand)
							{
								IBefriendedMob bef = BefriendMobsAPI.getBefriendingHandler().befriend(player, living);
								if (bef != null)
								{
									// TODO: customize this effect
									EntityHelper.sendHeartParticlesToMob((LivingEntity)bef);
									event.setCancellationResult(InteractionResult.sidedSuccess(isClientSide));
									return;
								}
							}
							result.add(0, InteractionResult.sidedSuccess(isClientSide));
						}
						else
						{
							BefriendableMobInteractionResult res = BefriendMobsAPI.getBefriendingHandler().onBefriendableMobInteract(BefriendableMobInteractArguments.of(event.getSide(), player, living, event.getHand()));
							if (res.befriendedMob != null)	// Directly exit if befriended, as this mob is no longer valid
							{
								event.setCanceled(true);
								event.setCancellationResult(InteractionResult.sidedSuccess(isClientSide));
								return;
							}
							else if (res.handled)
							{
								event.setCanceled(true);
								result.add(0, InteractionResult.sidedSuccess(isClientSide));
							}
						}	
					});
				}
				// Handle befriendable mob end //
				// Handle befriended mob start //
				else if (living instanceof IBefriendedMob bef)
				{
					//if (!isClientSide && isMainHand)
						//Debug.printToScreen("Befriended mob interacting...", player, living);		
					if (player.isShiftKeyDown() && player.getMainHandItem().getItem() == RegItems.DEBUG_BEFRIENDER.get())
					{
						bef.init(player.getUUID(), null);
						result.add(0, InteractionResult.sidedSuccess(isClientSide));
					}
					else 
					{					
						result.add(0, (player.isShiftKeyDown() ? bef.onInteractionShift(player, event.getHand()) : bef.onInteraction(player, event.getHand()))
								? InteractionResult.sidedSuccess(isClientSide) : result.get(0));
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
		event.setCanceled(result.get(0) == InteractionResult.sidedSuccess(isClientSide));
		event.setCancellationResult(result.get(0));
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
	        if (target instanceof Player player && mob.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
	        {
	        	mob.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
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
	        if (mob instanceof IBefriendedMob bef)
	        {
	        	// Befriended mob should never attack the owner
	        	if (target == bef.getOwner())
	        		mob.setTarget(bef.getPreviousTarget());
	        	else
	        		bef.setPreviousTarget(target);
	        }
		}
		// Handle mobs end //
	}	
}
