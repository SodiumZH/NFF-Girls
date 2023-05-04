package net.sodiumstudio.dwmg.events;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedChangeAiStateEvent;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.LivingAttributeValueChangeEvent;
import net.sodiumstudio.befriendmobs.events.BefriendableAddHatredEvent;
import net.sodiumstudio.befriendmobs.events.BefriendedDeathEvent;
import net.sodiumstudio.befriendmobs.events.ServerEntityTickEvent;
import net.sodiumstudio.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.befriendmobs.util.EntityHelper;
import net.sodiumstudio.befriendmobs.util.InfoHelper;
import net.sodiumstudio.befriendmobs.util.MiscUtil;
import net.sodiumstudio.befriendmobs.util.Wrapped;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.capabilities.CUndeadMobImpl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedCreeperGirl;
import net.sodiumstudio.dwmg.item.ItemNecromancerArmor;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.dwmg.registries.DwmgEffects;
import net.sodiumstudio.dwmg.registries.DwmgItems;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgEntityEvents
{

	@SuppressWarnings("deprecation")
	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event)
	{

		LivingEntity target = event.getTarget();		
		LivingEntity lastHurtBy = event.getEntity().getLastHurtByMob();
		Wrapped<Boolean> isCancelledByEffect = new Wrapped<Boolean>(Boolean.FALSE);
		
		// Handle mobs //
		if (target != null && event.getEntity() instanceof Mob mob)
		{
			// Handle undead mobs start //
	        if (mob.getMobType() == MobType.UNDEAD && !(event.getEntity() instanceof IBefriendedMob)) 
	        {
	        	// Handle CUndeadMob //
        		mob.getCapability(DwmgCapabilities.CAP_UNDEAD_MOB).ifPresent((l) ->
        		{
        			if (target != null && target.hasEffect(DwmgEffects.UNDEAD_AFFINITY.get()) && lastHurtBy != target && !l.getHatred().contains(target.getUUID()))
        			{
        				mob.setTarget(null);
        				isCancelledByEffect.set(true);
        			}
        			else if(target != null)
        			{
        				l.addHatred(target);
        			}
        		});
        		// Handle CUndeadMob end //
		    } 
	        // Handle undead mobs end //
		}
		// Handle mobs end //
	}
	
	@SubscribeEvent
	public static void onBefDie(BefriendedDeathEvent event)
	{
		if (event.getDamageSource().getEntity() != null)
		{
			if (event.getDamageSource().getEntity().getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).isPresent())
			{
				event.getDamageSource().getEntity().getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> 
				{
					if (event.getDamageSource().getEntity() instanceof CreeperGirlEntity cg)
					{
						// Befriended mobs won't be killed by CreeperGirl's "final explosion". They leave 1 health and get invulnerable for 3s, 
						// preventing them to be killed by falling down after blowed up by the explosion.
						if (l.getNbt().contains("final_explosion_player", 11)
						&& event.getMob().getOwner() != null
						&& l.getNbt().getUUID("final_explosion_player").equals(event.getMob().getOwnerUUID()))
						{
							event.getMob().asMob().setHealth(1.0f);
							event.getMob().asMob().invulnerableTime += 60;
							EntityHelper.sendGlintParticlesToLivingDefault(event.getMob().asMob());
							event.setCanceled(true);
							return;
						}
					}
				});
			
			}
			if (event.isCanceled())
				return;
		}
		
		if (event.getMob() instanceof EntityBefriendedCreeperGirl cg)
		{
			if (cg.isPowered())
				cg.spawnAtLocation(new ItemStack(ModItems.LIGHTNING_PARTICLE.get(), 1));
		}
	}
	
	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		LivingEntity living = event.getEntity();
		if (!living.level.isClientSide)
		{
			// Handle Ender Protection
			if (living.hasEffect(DwmgEffects.ENDER_PROTECTION.get()))
			{
				// If the player drops into the void, try pull up
				if (event.getSource().equals(DamageSource.OUT_OF_WORLD))
				{
					// Ignore damage by /kill
					if (living.getY() < -64.0d)
					{
						// Lift up
						living.setPosRaw(living.getX(), 64.0d, living.getZ());
						// and find a standable block
						EntityHelper.chorusLikeTeleport(living);
						living.level.addParticle(ParticleTypes.PORTAL, living.getRandomX(0.5D),
								living.getRandomY() - 0.25D, living.getRandomZ(0.5D),
								(living.getRandom().nextDouble() - 0.5D) * 2.0D, -living.getRandom().nextDouble(),
								(living.getRandom().nextDouble() - 0.5D) * 2.0D);
						living.removeEffect(DwmgEffects.ENDER_PROTECTION.get());

						// whether player is standing on a solid block
						BlockPos standingOn = new BlockPos(living.blockPosition().getX(),
								living.blockPosition().getY() - 1, living.blockPosition().getZ());
						if (living.level.getBlockState(standingOn).is(Blocks.AIR))
						{
							// failed, add slow falling
							if (living instanceof Player p)
							{
								MiscUtil.printToScreen(
										"You're lifted from the void because of the Ender Protection, but...", p);
							}
							living.setDeltaMovement(new Vec3(0, 0, 0)); // Velocity
							living.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 200));
						} 
						else
						{
							// succeeded
							if (living instanceof Player p)
							{
								MiscUtil.printToScreen("You're saved from the void because of the Ender Protection!", p);
							}
						}
					}
				}
				else if (!event.getSource().equals(DamageSource.IN_FIRE)
						&& !event.getSource().equals(DamageSource.STARVE))
				{
					living.level.addParticle(ParticleTypes.PORTAL, living.getRandomX(0.5D), living.getRandomY() - 0.25D,
							living.getRandomZ(0.5D), (living.getRandom().nextDouble() - 0.5D) * 2.0D,
							-living.getRandom().nextDouble(), (living.getRandom().nextDouble() - 0.5D) * 2.0D);
					EntityHelper.chorusLikeTeleport(living);
				}
			} 
		}
	}
	
	@SubscribeEvent
	public static void onEnderTeleport(EntityTeleportEvent.EnderEntity event)
	{
		if (event.getEntityLiving() instanceof EnderExecutorEntity ee)
		{
			ee.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> 
			{
				if (l.getNbt().getBoolean("cannot_teleport"))
				{
					// Still teleport in water
					if (!ee.isInWater())
					{
						event.setCanceled(true);
					}
					
				}
			});
		}
	}

	
	@SubscribeEvent
	public static void onBefriendableAddHatred(BefriendableAddHatredEvent event)
	{
		if (BefriendingTypeRegistry.contains(event.mob))
		{
			// Cancel add hatred if undead mob trying targeting to a player with undead affinity
			// Setting target will also be canceled in BefriendMobs-EntityEvents
			if (event.mob.getMobType() == MobType.UNDEAD
				&& event.toAdd.hasEffect(DwmgEffects.UNDEAD_AFFINITY.get())
				&& event.reason == BefriendableAddHatredReason.SET_TARGET
				)
				
			{
				Wrapped<Boolean> inHatred = new Wrapped<Boolean>(false);
				event.mob.getCapability(BefMobCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((cap) -> 
				{
					inHatred.set(cap.isInHatred(event.toAdd));
				});
				if (!inHatred.get())
					event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public static void onBefriendedAttributeChange(LivingAttributeValueChangeEvent event)
	{
		if (event.entity instanceof IBefriendedMob b 
				&& b.getModId().equals(Dwmg.MOD_ID)
				&& event.attribute.equals(Attributes.MAX_HEALTH)
				)
		{
			event.entity.setHealth((float) (event.entity.getHealth() * event.newValue / event.oldValue));
		}
	}
	
	@SubscribeEvent
	public static void onLivingUpdate(LivingTickEvent event)
	{
		// Necromancer armor
		ItemNecromancerArmor.necromancerArmorUpdate(event.getEntity());
		
		if (event.getEntity() instanceof Mob mob)
		{
			// Befriended undead mob sun sensitivity
			if (mob instanceof IBefriendedUndeadMob un)
			{
				un.setSunSensitive(!mob.getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.SUNHAT.get()));
			}
			// Undead mob forgiving player
			mob.getCapability(DwmgCapabilities.CAP_UNDEAD_MOB).ifPresent((l) -> 
			{
				((CUndeadMobImpl)l).updateForgivingTimers();
			});
		}
	}
	
	@SubscribeEvent
	public static void onBefriendedSwitchAiState(BefriendedChangeAiStateEvent event)
	{
		if (event.getMob().getModId().equals(Dwmg.MOD_ID) && !event.getMob().asMob().level.isClientSide)
		{
			MiscUtil.printToScreen(InfoHelper.createText("")
					.append(event.getMob().asMob().getName())
					.append(InfoHelper.createText(" "))
					.append(BefriendedAIState.getDisplayInfo.apply(event.getStateAfter())), event.getMob().getOwner());
		}
	}
}
