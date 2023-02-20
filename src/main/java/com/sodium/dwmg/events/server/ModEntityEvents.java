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

import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
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
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = Dwmg.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEntityEvents
{

	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event)
	{
		Entity target = event.getTarget();
		Player player = event.getPlayer();
		// Server events start //
		if (event.getSide() ==  LogicalSide.SERVER)
		{
			// Mob interaction start //
			if (target != null && target instanceof LivingEntity living)
			{
				// Handle befriendable mob start //
				if(living.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
				{
					living.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
						if (event.getHand() == InteractionHand.OFF_HAND)
						{
							// TODO: Actions when interacting with a befriendable mob
						}
					});
				}
				// Handle befriendable mob end //
				// Handle befriended mob start //
				if (living instanceof IBefriendedMob bef)
				{
					if (event.getHand() == InteractionHand.MAIN_HAND)
					{
						Debug.printToScreen("Entity interacting on server: " + event.getHand().toString(), player, target);
						if (player.isShiftKeyDown())
						{
							if (player.getMainHandItem().getItem() == ModItems.DEBUG_BEFRIENDER.get())
							{
								bef.init(player, null);
								Debug.printToScreen("Befriended mob initialized: " + event.getHand().toString(), player, target);
							}
							bef.onShiftRightClicked(player);
						}
						else
						{
							bef.onRightClicked(player);
						}
						
					}
				}
				// Handle befriended mob end //
			}
			// Mob interaction end //
		}
		// Server events end //
		// Client events start //
		else
		{
			//Debug.printToScreen("Entity interacting on client: " + event.getHand().toString(), player, target);
		}
		// Client events end //
	}
	
	
	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event)
	{
		LivingEntity lastHurtBy = event.getEntityLiving().getLastHurtByMob();
		LivingEntity target = event.getTarget();
		
		// Flag 0: Undead mob cancelled target because of Death Affinity effect
		Vector<Integer> flags = new Vector<Integer>();
		
		// Handle mobs //
		if (target != null && event.getEntity() instanceof Mob mob)
		{
        	// Handle undead mobs start //
	        if (mob.getMobType() == MobType.UNDEAD) 
	        {
	        	// Handle CapUndeadMob //
	        	if(mob.getCapability(ModCapabilities.CAP_UNDEAD_MOB).isPresent())
	        	{
	        		mob.getCapability(ModCapabilities.CAP_UNDEAD_MOB).ifPresent((l) ->
	        		{
	        			if (target != null && target.hasEffect(ModEffects.DEATH_AFFINITY.get()) && lastHurtBy != target && !l.getHatred().contains(target.getUUID()))
	        			{
	        				mob.setTarget(null);
	        				flags.add(0);
	        			}
	        			else if(target != null)
	        			{
	        				l.addHatred(target);
	        			}
	        		});
	        	}
        		// Handle CapUndeadMob end //
		    } 
	        // Handle undead mobs end //
	        // Handle befriendable mobs //
	        if (target instanceof Player player && mob.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
	        {
	        	mob.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
	        	{
	        		// Add to hatred list (disable befriending permanently)
	        		if(target != null && !l.getHatred().contains(player.getUUID()) && !flags.contains(0))
	        		{
	        			l.addHatred(player);
	        			flags.add(1);
	        			Debug.printToScreen("Player " + Debug.getNameString(player) + " put into hatred list by " + Debug.getNameString(mob), player, player);
	        		}
	        	});
	        }
	        // Handle befriendable mobs end //
		}
		// Handle mobs end //
	}	
}
