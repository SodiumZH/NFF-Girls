package net.sodiumstudio.dwmg.befriendmobs.event;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.event.events.BefriendableMobInteractEvent;
import net.sodiumstudio.dwmg.befriendmobs.event.events.MobBefriendEvent;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegCapabilities;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegItems;
import net.sodiumstudio.dwmg.befriendmobs.util.Debug;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.Util;
import net.sodiumstudio.dwmg.befriendmobs.util.Wrapped;
import net.sodiumstudio.dwmg.befriendmobs.util.exceptions.UnimplementedException;
import net.sodiumstudio.dwmg.dwmgcontent.registries.ModCapabilities;
import net.sodiumstudio.dwmg.dwmgcontent.registries.ModEffects;

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
			Mob mob = (Mob) target;
			@SuppressWarnings("unchecked")
			EntityType<Mob> type = (EntityType<Mob>) mob.getType();
			// Handle befriendable mob start //
			if (mob.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).isPresent()
					&& !(mob instanceof IBefriendedMob)) {
				mob.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> 
				{
					/* For debug items only */
					if (player.getMainHandItem().getItem() == RegItems.DEBUG_BEFRIENDER.get()) 
					{
						if (!isClientSide && isMainHand) 
						{
							IBefriendedMob bef = BefriendingTypeRegistry.getHandler(type).befriend(player,
									mob);
							if (bef != null) 
							{
								MinecraftForge.EVENT_BUS.post(new MobBefriendEvent(player, mob, bef));
								EntityHelper.sendHeartParticlesToMob(bef.asMob()); // TODO: move this to a MobBefriendEvent listener
								event.setCancellationResult(InteractionResult.sidedSuccess(isClientSide));
								return;
							} 
							else throw new UnimplementedException("Entity type befriend method unimplemented: "
										+ mob.getType().toShortString() + ", handler class: "
										+ BefriendingTypeRegistry.getHandler(type).toString());
						}
						MinecraftForge.EVENT_BUS.post(
								new BefriendableMobInteractEvent(event.getSide(), player, mob, event.getHand()));
						result.set(InteractionResult.sidedSuccess(isClientSide));
					}
					else if (player.getMainHandItem().getItem() == RegItems.DEBUG_ARMOR_GIVER.get())
					{
						if (!isClientSide && isMainHand) 
						{
							if (mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty())
								mob.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET.asItem()));
							else if (mob.getItemBySlot(EquipmentSlot.CHEST).isEmpty())
								mob.setItemSlot(EquipmentSlot.CHEST, new ItemStack(Items.DIAMOND_CHESTPLATE.asItem()));
							else if (mob.getItemBySlot(EquipmentSlot.LEGS).isEmpty())
								mob.setItemSlot(EquipmentSlot.LEGS, new ItemStack(Items.DIAMOND_LEGGINGS.asItem()));
							else if (mob.getItemBySlot(EquipmentSlot.FEET).isEmpty())
								mob.setItemSlot(EquipmentSlot.FEET, new ItemStack(Items.DIAMOND_BOOTS.asItem()));
							result.set(InteractionResult.sidedSuccess(isClientSide));
						}
					}
					/* Main actions */
					else 
					{
						BefriendableMobInteractionResult res = BefriendingTypeRegistry.getHandler(type)
								.handleInteract(BefriendableMobInteractArguments.of(event.getSide(), player, mob,
										event.getHand()));
						if (res.befriendedMob != null) // Directly exit if befriended, as this mob is no longer valid
						{
							event.setCanceled(true);
							event.setCancellationResult(InteractionResult.sidedSuccess(isClientSide));
							return;
						} 
						else if (res.handled) 
						{
							event.setCanceled(true);
							result.set(InteractionResult.sidedSuccess(isClientSide));
							shouldPostInteractEvent.set(true);
							MinecraftForge.EVENT_BUS.post(
									new BefriendableMobInteractEvent(event.getSide(), player, mob, event.getHand()));
						}
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
	
	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event)
	{
		LivingEntity lastHurtBy = event.getEntityLiving().getLastHurtByMob();
		LivingEntity target = event.getTarget();		
		Wrapped<Boolean> isCancelledByEffect = new Wrapped<Boolean>(Boolean.FALSE);
		
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
	
	@SubscribeEvent
	public static void onLivingDeath(LivingDeathEvent event)
	{
		if (event.getEntity() instanceof IBefriendedMob bef)
		{
			if (!event.getEntity().level.isClientSide)
			{
				// Drop all items in inventory
				SimpleContainer container = bef.makeContainerFromInventory();
				for (int i = 0; i < container.getContainerSize(); ++i)
				{
					if (container.getItem(i) != ItemStack.EMPTY)
					{
						event.getEntity().spawnAtLocation(container.getItem(i));
					}
			}
			}
		}
	}
	
}
