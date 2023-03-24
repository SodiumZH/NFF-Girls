package net.sodiumstudio.dwmg.befriendmobs.events;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.ZombieEvent.SummonAidEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.Event.Result;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobItems;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.BMDebugItemHandler;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgCapabilities;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEffects;

// TODO: change modid after isolation
@SuppressWarnings("removal")
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
			else if (mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).isPresent()
					&& !(mob instanceof IBefriendedMob)) {
				mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> 
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

				if (player.isShiftKeyDown() && player.getMainHandItem().getItem() == BefMobItems.DEBUG_BEFRIENDER.get()) {
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
	
	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event)
	{
		@SuppressWarnings("deprecation")
		LivingEntity target = event.getTarget();		
		Wrapped<Boolean> isCancelledByEffect = new Wrapped<Boolean>(Boolean.FALSE);
		
		// Handle mobs //
		if (target != null && event.getEntity() instanceof Mob mob)
		{ 	
	        // Handle befriendable mobs //
	        if (target instanceof Player player && mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
	        {
	        	mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) ->
	        	{
	        		// Add to hatred list
	        		if(target != null && !l.getHatred().contains(player.getUUID()) && !isCancelledByEffect.get())
	        		{
	        			l.addHatred(player);
	        			// Debug.printToScreen("Player " + Util.getNameString(player) + " put into hatred list by " + Util.getNameString(mob), player, player);
	        		}
	        	});
	        }
	        // Handle befriendable mobs end //
	        // Handle befriended mobs //	        
	        if (mob instanceof IBefriendedMob bef)
	        {
	        	// Befriended mob should never attack the owner
	        	if (target == bef.getOwner())
	        		mob.setTarget(bef.getPreviousTarget());
	        	// Befriended mob shouldn't attack owner's other befriended mobs
	        	else if (target instanceof IBefriendedMob tbef)
	        	{
	        		if (bef.getOwner() != null && tbef.getOwner() != null && bef.getOwner() == tbef.getOwner())
	        		{
	        			mob.setTarget(bef.getPreviousTarget());
	        		}
	        	}
	        	// Befriended mob shouldn't attack owner's tamable animals
	        	else if (target instanceof TamableAnimal ta)
	        	{
	        		if (bef.getOwner() != null && ta.getOwner() != null && bef.getOwner() == ta.getOwner())
	        		{
	        			mob.setTarget(bef.getPreviousTarget());
	        		}
	        	}
	        	else
	        		bef.setPreviousTarget(target);
	        }
	        // Handle befriended mobs end //
	        // Handle TamableAnimal //	
	        if (mob instanceof TamableAnimal ta)
	        {
	        	// Tamable animals shouldn't attack owner's befriended mobs
	        	if (target instanceof IBefriendedMob tbef)
	        	{
	        		if (ta.getOwner() != null && tbef.getOwner() != null && ta.getOwner() == tbef.getOwner())
	        		{
	        			ta.setTarget(null);
	        		}
	        	}
				// Handle TamableAnimal end //
	        }
		}
		// Handle mobs end //
	}	
	
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event) {
		if (event.getEntity() instanceof IBefriendedMob bef)
		{
			if (MinecraftForge.EVENT_BUS.post(new BefriendedDeathEvent(bef, event.getSource())))
			{
				event.setCanceled(true);
				return;
			}
			// Befriended mobs should not kill each other with same owner, or get killed by
			// owner-tamed animals
			else if (event.getSource().getEntity() instanceof IBefriendedMob srcBef)
			{
				if (srcBef.getOwner() != null && bef.getOwner() != null && srcBef.getOwner() == bef.getOwner())
				{
					bef.asMob().setHealth(1.0f);
					bef.asMob().invulnerableTime += 20;
					event.setCanceled(true);
					return;
				}
			}
			else if (event.getSource().getEntity() instanceof TamableAnimal ta)
			{
				if (ta.getOwner() != null && bef.getOwner() != null && ta.getOwner() == bef.getOwner())
				{
					bef.asMob().setHealth(1.0f);
					bef.asMob().invulnerableTime += 20;
					event.setCanceled(true);
					return;
				}
			}
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
		
		else if (event.getEntity() instanceof TamableAnimal ta)
		{
			if (event.getSource().getEntity() instanceof IBefriendedMob srcBef)
			{
				if (srcBef.getOwner() != null && ta.getOwner() != null && srcBef.getOwner() == ta.getOwner())
				{
					ta.setHealth(1.0f);
					ta.invulnerableTime += 20;
					event.setCanceled(true);
					return;
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
	
	@SubscribeEvent
	public static void onTimerUp(BefriendableTimerUpEvent event)
	{
		if (event.getPlayer() != null)
		{
			if (event.getKey().equals("in_hatred"))
			{
				if (event.getCapability().getHatred().contains(event.getPlayer().getUUID()))
				{
					event.getCapability().getHatred().remove(event.getPlayer().getUUID());
				}
			}
		}
	}
}