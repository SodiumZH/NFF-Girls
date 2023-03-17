package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegItems;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Util;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.BMDebugItemHandler;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgCapabilities;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;

// TODO: change modid after isolation
@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEvents
{

	@SubscribeEvent
	public static void onEntityInteract(EntityInteract event) {
		Entity target = event.getTarget();
		Player player = event.getPlayer();
		Wrapped<InteractionResult> result = new Wrapped<InteractionResult>(InteractionResult.PASS);
		boolean isClientSide = event.getSide() == LogicalSide.CLIENT;
		boolean isMainHand = event.getHand() == InteractionHand.MAIN_HAND;
		// TODO: post interact event condition?
		Wrapped<Boolean> shouldPostInteractEvent = new Wrapped<Boolean>(Boolean.FALSE);

		// Mob interaction start //
		if (target != null && target instanceof Mob) 
		{
	
			Mob mob = (Mob)target;
			@SuppressWarnings("unchecked")
			EntityType<Mob> type = (EntityType<Mob>) mob.getType();

			
			// Do debug actions and skip when holding debug items
			if (TagHelper.hasTag(player.getMainHandItem().getItem(), "befriendmobs", "debug_tools"))
			{
				if (!isClientSide && isMainHand)
					BMDebugItemHandler.onDebugItemUsed(player, (Mob)target, player.getMainHandItem().getItem());
				event.setCancellationResult(InteractionResult.sidedSuccess(isClientSide));			
				return;		
			}
			
			// Handle befriendable mob start //
			else if (mob.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).isPresent()
					&& !(mob instanceof IBefriendedMob)) {
				mob.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> 
				{

					BefriendableMobInteractionResult res = BefriendingTypeRegistry.getHandler(type).handleInteract(
							BefriendableMobInteractArguments.of(event.getSide(), player, mob, event.getHand()));
					if (res.befriendedMob != null) // Directly exit if befriended, as this mob is no longer valid
					{
						event.setCanceled(true);
						event.setCancellationResult(InteractionResult.sidedSuccess(isClientSide));
						return;
					} else if (res.handled)
					{
						event.setCanceled(true);
						result.set(InteractionResult.sidedSuccess(isClientSide));
						shouldPostInteractEvent.set(true);
					}
					
				});
			}
			// Handle befriendable mob end //
			// Handle befriended mob start //
			else if (mob instanceof IBefriendedMob bef) 
			{
				// if (!isClientSide && isMainHand)

				if (player.isShiftKeyDown() && player.getMainHandItem().getItem() == RegItems.DEBUG_BEFRIENDER.get()) {
					bef.init(player.getUUID(), null);
					// Debug.printToScreen("Befriended mob initialized", player, living);
					result.set(InteractionResult.sidedSuccess(isClientSide));
				}
				else 
				{
					result.set((player.isShiftKeyDown() ? bef.onInteractionShift(player, event.getHand())
							: bef.onInteraction(player, event.getHand())) ? InteractionResult.sidedSuccess(isClientSide)
									: result.get());
				}
			}
			// Handle befriended mob end //
		}
		// Mob interaction end //

		// Server events end //
		// Client events start //
		else {
		}
		// Client events end //
		event.setCanceled(result.get().equals(InteractionResult.sidedSuccess(isClientSide)));
		event.setCancellationResult(result.get());
	}
	
	@SuppressWarnings("removal")
	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event)
	{
		LivingEntity lastHurtBy = event.getEntityLiving().getLastHurtByMob();
		@SuppressWarnings("deprecation")
		LivingEntity target = event.getTarget();		
		Wrapped<Boolean> isCancelledByEffect = new Wrapped<Boolean>(Boolean.FALSE);
		
		// Handle mobs //
		if (target != null && event.getEntity() instanceof Mob mob)
		{
        	// Handle undead mobs start //
	        if (mob.getMobType() == MobType.UNDEAD) 
	        {
	        	// Handle CapUndeadMob //
        		mob.getCapability(DwmgCapabilities.CAP_UNDEAD_MOB).ifPresent((l) ->
        		{
        			if (target != null && target.hasEffect(DwmgEffects.DEATH_AFFINITY.get()) && lastHurtBy != target && !l.getHatred().contains(target.getUUID()))
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
	
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if (event.getEntity() instanceof IBefriendedMob bef)
		{
			if (!event.getEntity().level.isClientSide)
			{
				// Drop all items in inventory if no vanishing curse
				AdditionalInventory container = bef.getAdditionalInventory();
				for (int i = 0; i < container.getContainerSize(); ++i)
				{
					if (container.getItem(i) != ItemStack.EMPTY 
							&& !EnchantmentHelper.hasVanishingCurse(container.getItem(i)))
					{
						event.getEntity().spawnAtLocation(container.getItem(i));
					}
			}
			}
		}
	}
	
	// Don't allow befriended zombies to summon
	@SubscribeEvent
	public static void onZombieSummon(SummonAidEvent event)
	{
		if (event.getEntity() instanceof IBefriendedMob)
		{
			event.setResult(Result.DENY);
		}
	}
}
