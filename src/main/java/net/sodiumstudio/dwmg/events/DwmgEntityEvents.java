package net.sodiumstudio.dwmg.events;

import java.util.List;
import java.util.function.Predicate;

import com.github.mechalopa.hmag.HMaG;
import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;
import com.github.mechalopa.hmag.world.entity.DyssomniaEntity;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Phantom;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Spider;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.entity.monster.piglin.PiglinBrute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityMobGriefingEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedChangeAiStateEvent;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.befriendmobs.entity.ai.util.AiHelper;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entity.capability.CAttributeMonitor;
import net.sodiumstudio.befriendmobs.events.BefriendableAddHatredEvent;
import net.sodiumstudio.befriendmobs.events.BefriendedDeathEvent;
import net.sodiumstudio.befriendmobs.events.ServerEntityTickEvent;
import net.sodiumstudio.befriendmobs.registry.BefMobCapabilities;
import net.sodiumstudio.befriendmobs.registry.BefMobItems;
import net.sodiumstudio.befriendmobs.util.EntityHelper;
import net.sodiumstudio.befriendmobs.util.InfoHelper;
import net.sodiumstudio.befriendmobs.util.MiscUtil;
import net.sodiumstudio.befriendmobs.util.ReflectHelper;
import net.sodiumstudio.befriendmobs.util.TagHelper;
import net.sodiumstudio.befriendmobs.util.Wrapped;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.compat.CompatTwilightForest;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.capabilities.CUndeadMobImpl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedCreeperGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedDrownedGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedEnderExecutor;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedGhastlySeeker;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedHornet;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedHuskGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedNecroticReaper;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedSkeletonGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedStrayGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedWitherSkeletonGirl;
import net.sodiumstudio.dwmg.entities.hmag.EntityBefriendedZombieGirl;
import net.sodiumstudio.dwmg.entities.projectile.NecromancerMagicBulletEntity;
import net.sodiumstudio.dwmg.item.ItemNecromancerArmor;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.dwmg.registries.DwmgEffects;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;

@SuppressWarnings("removal")
@Mod.EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgEntityEvents
{
	private static final EquipmentSlot[] ARMOR_SLOTS =
		{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
	private static final EquipmentSlot[] ARMOR_SLOT_HELMET =
		{EquipmentSlot.HEAD};
	private static final EquipmentSlot[] ARMOR_AND_HANDS =
		{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
	
	
	
	@SubscribeEvent
	public static void onLivingSetAttackTargetEvent(LivingSetAttackTargetEvent event)
	{
		LivingEntity target = event.getTarget();		
		LivingEntity lastHurtBy = event.getEntityLiving().getLastHurtByMob();
		Wrapped<Boolean> isCancelledByEffect = new Wrapped<Boolean>(Boolean.FALSE);
		
		// Handle mobs //
		if (target != null && event.getEntity() instanceof Mob mob)
		{
			// Handle undead mobs start //
	        if (mob.getMobType() == MobType.UNDEAD 
	        		&& !(event.getEntity() instanceof IBefriendedMob) 
	        		&& !TagHelper.hasTag(mob, Dwmg.MOD_ID, "ignore_undead_affinity")) 
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
	        
	        // Befriendable mobs don't attack their befriended variation
	        if (BefriendingTypeRegistry.contains(mob) 
	        		&& BefriendingTypeRegistry.getConvertTo(mob) == target.getType()
	        		&& target instanceof IBefriendedMob bef
	        		&& bef.getModId().equals(Dwmg.MOD_ID))
	        {
				mob.setTarget(null);
	        }
	        // Befriended mobs don't attack their wild variation
	        if (mob instanceof IBefriendedMob bef 
	        		&& bef.getModId().equals(Dwmg.MOD_ID)
	        		&& BefriendingTypeRegistry.getTypeBefore(mob) == target.getType())
	        {
				mob.setTarget(null);
	        }
	        
		}
		// Handle befriended mobs //

		// Handle mobs end //
	}
	
	@SubscribeEvent
	public static void onBefriendedDie(BefriendedDeathEvent event)
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

		/** Favorability & Level */
		if (event.getMob() instanceof IDwmgBefriendedMob bm)
		{
			// Favorability loss on death
			if (event.getDamageSource().getEntity() != null
					&& event.getDamageSource().getEntity() == bm.getOwner()
					&& event.getDamageSource() != DamageSource.OUT_OF_WORLD)
				bm.getFavorability().setFavorability(0);
			else if (bm.asMob().distanceToSqr(bm.getOwner()) < 64d && event.getDamageSource() != DamageSource.OUT_OF_WORLD)
				bm.getFavorability().addFavorability(-20);
			// EXP loses by a half on death
			// As respawner construction (in befriendmobs) is after posting BefriendedDeathEvent, it can be set here
			bm.getLevelHandler().setExp(bm.getLevelHandler().getExp() / 2);
		}
		/** Favorability & Level end */
	}
	
	
	
	@SubscribeEvent
	public static void onLivingHurt(LivingHurtEvent event) {
		
		/** Compat */
		
		CompatTwilightForest.onLivingHurt(event);
		if (event.isCanceled())
			return;
			
		/** Compat end */
			
		LivingEntity living = event.getEntity();
		if (!living.level.isClientSide)
		{
			// Cancel necromancer magic bullet normal attack
			if (event.getSource().getDirectEntity() instanceof NecromancerMagicBulletEntity)
			{
				event.setCanceled(true);
			}

			/* Ender Protection Effect */
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
					EntityHelper.sendParticlesToEntity(living, null, 0, living.getBbHeight()/2, 0, 0.5, living.getBbHeight()/2, 0.5, 2, 1);
					/*living.level.addParticle(ParticleTypes.PORTAL, 
							living.getRandomX(0.5D), 
							living.getRandomY() - 0.25D,
							living.getRandomZ(0.5D), 
							(living.getRandom().nextDouble() - 0.5D) * 2.0D,
							-living.getRandom().nextDouble(), 
							(living.getRandom().nextDouble() - 0.5D) * 2.0D);*/
					EntityHelper.chorusLikeTeleport(living);
				}
			} 
			/** Ender Protection Effect end */
			
			/** Durability */
			// Weapon durability
			if (event.getSource().getEntity() != null 
					&& event.getSource().getEntity() instanceof IDwmgBefriendedMob bm 
					&& bm.getModId().equals(Dwmg.MOD_ID))
			{
				if (!bm.asMob().getMainHandItem().isEmpty() && bm.asMob().getMainHandItem().getItem() instanceof DiggerItem dg)
				{
					bm.asMob().getMainHandItem().hurtAndBreak(2, bm.asMob(), (mob) ->
					{
						mob.broadcastBreakEvent(EquipmentSlot.MAINHAND);
					});			
				}
				if (!bm.asMob().getMainHandItem().isEmpty() && bm.asMob().getMainHandItem().getItem() instanceof SwordItem sw)
				{
					bm.asMob().getMainHandItem().hurtAndBreak(1, bm.asMob(), (mob) ->
					{
						mob.broadcastBreakEvent(EquipmentSlot.MAINHAND);
					});
				}			
			}
			// Armor durability
			if (event.getEntity() instanceof IDwmgBefriendedMob bm
					&& bm.getModId().equals(Dwmg.MOD_ID))
			{
				if (!bm.asMob().getItemBySlot(EquipmentSlot.HEAD).isEmpty())
				{
					if (event.getSource().isDamageHelmet())
					{
						hurtHelmet(bm.asMob(), event.getSource(), event.getAmount());
					}
					hurtArmor(bm.asMob(), event.getSource(), event.getAmount());
				}				
			}
			
			/** Durability end */
			
			/** Favorability */
			
			// If owner attacked friendly mob, lose favorability depending on damage; no lost if < 0.5
			if (event.getEntity() instanceof IDwmgBefriendedMob bm 
					&& bm.getModId().equals(Dwmg.MOD_ID)
					&& event.getSource().getEntity() != null
					&& event.getSource().getEntity() instanceof Player player
					&& bm.getOwnerUUID().equals(player.getUUID())
					&& !event.getSource().equals(DamageSource.OUT_OF_WORLD)
					&& !event.getSource().isCreativePlayer())
			{
				if (event.getAmount() >= 0.5f)
				{
					event.getEntity().getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((cap) -> 
					{
						float loseValue = event.getAmount() / 2f;
						if (loseValue > 10f)
							loseValue = 10f;
						cap.addFavorability(-loseValue);
						if (loseValue < 1.0f)
							EntityHelper.sendSmokeParticlesToLivingDefault(bm.asMob());
						else
							EntityHelper.sendAngryParticlesToLivingDefault(bm.asMob());
					});
				}
			}
			
			/** Favorability end */
			
			// Label player on bef mob attacking target, just like for TamableAnimal, so that it can drop player's loot table
			if (event.getEntity() instanceof Mob mob
					&& event.getSource().getEntity() != null
					&& event.getSource().getEntity() instanceof IDwmgBefriendedMob bm)
			{
				mob.setLastHurtByPlayer(bm.getOwner());
			}
			
		}
	}
		
	protected static void hurtArmor(Mob mob, DamageSource damageSource, float damage, EquipmentSlot[] slots)
	{
		if (!(damage <= 0.0F))
		{
			damage /= 4.0F;
			if (damage < 1.0F)
			{
				damage = 1.0F;
			}
			for (EquipmentSlot slot : slots)
			{
				ItemStack itemstack = mob.getItemBySlot(slot);
				if ((!damageSource.isFire() || !itemstack.getItem().isFireResistant())
						&& itemstack.getItem() instanceof ArmorItem)
				{
					itemstack.hurtAndBreak((int) damage, mob, (m) ->
					{
						m.broadcastBreakEvent(EquipmentSlot.byTypeAndIndex(EquipmentSlot.Type.ARMOR, slot.getIndex()));
					});
				}
			}
		}
	}
	
	protected static void hurtArmor(Mob mob, DamageSource damageSource, float damage)
	{
		hurtArmor(mob, damageSource, damage, ARMOR_SLOTS);
	}
	
	protected static void hurtHelmet(Mob mob, DamageSource damageSource, float damage)
	{
		hurtArmor(mob, damageSource, damage, ARMOR_SLOT_HELMET);
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
	public static void onBefriendedAttributeChange(CAttributeMonitor.ChangeEvent event)
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
	public static void onLivingTick(LivingUpdateEvent event)
	{
		// Necromancer armor
		if (!event.getEntity().level.isClientSide)
		{
			ItemNecromancerArmor.necromancerArmorUpdate(event.getEntityLiving());
			
			if (event.getEntity() instanceof Mob mob)
			{
				// Undead mob forgiving player
				mob.getCapability(DwmgCapabilities.CAP_UNDEAD_MOB).ifPresent((l) -> 
				{
					((CUndeadMobImpl)l).updateForgivingTimers();
				});
				// Sync favorability and level
				for (Player player: mob.level.players())
				{
					if (player instanceof ServerPlayer sp)
					{
						mob.getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((cap) -> 
						{
							cap.sync(sp);
						});
						mob.getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent((cap) -> 
						{
							cap.sync(sp);
						});
					}
				}
			}
			/** Send overlap event */
			if (event.getEntity() instanceof IDwmgBefriendedMob bm)
			{
				if (bm.asMob().getHealth() > 0.0F) {
			         AABB aabb;
			         if (bm.asMob().isPassenger() && !bm.asMob().getVehicle().isRemoved()) {
			            aabb = bm.asMob().getBoundingBox().minmax(bm.asMob().getVehicle().getBoundingBox()).inflate(1.0D, 0.0D, 1.0D);
			         } else {
			            aabb = bm.asMob().getBoundingBox().inflate(1.0D, 0.5D, 1.0D);
			         }

			         List<Entity> list = bm.asMob().level.getEntities(bm.asMob(), aabb);

			         for(int i = 0; i < list.size(); ++i) {
			            Entity entity = list.get(i);
			            if (!entity.isRemoved()) {
			               bm.touchEntity(entity);
			            }
			         }
			     }
			}
		}
	}
	
	@SubscribeEvent
	public static void onBMOverlap(IDwmgBefriendedMob.OverlapEntityEvent event)
	{
		if (event.touchedEntity instanceof Slime slime)
		{
		      if (!slime.isTiny() && slime.isEffectiveAi() && slime.getTarget() == event.thisMob.asMob()) 
		      {
		    	  ReflectHelper.forceInvoke(slime, Slime.class, "dealDamage", event.thisMob.asMob());
		      }
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
					.append(event.getStateAfter().getDisplayInfo()), event.getMob().getOwner());
		}
	}

	@SubscribeEvent
	public static void onNonBefriendedDie(LivingDeathEvent event)
	{
		if (!event.getEntity().level.isClientSide)
		{
			// This function only handler non-befriended
			if (event.getEntity() instanceof IBefriendedMob)
				return;
			// When BM killed a mob targeting the player, favorability + 0.5 
			if (event.getSource().getEntity() != null 
					&& event.getSource().getEntity() instanceof IDwmgBefriendedMob bm
					&& event.getEntity() instanceof Mob mob
					&& mob.getTarget() != null
					&& mob.getTarget() == bm.getOwner())
			{
				bm.getFavorability().addFavorability(0.5f);
			}
			// When player killed a mob targeting BM, fav + 1
			if (event.getSource().getEntity() != null
					&& event.getSource().getEntity() instanceof Player player
					&& event.getEntity() instanceof Mob mob
					&& mob.getTarget() instanceof IDwmgBefriendedMob bm
					&& bm.getOwner() == player)
			{
				bm.getFavorability().addFavorability(1f);
			}
			
		}
	}
	
	@SubscribeEvent
	public static void onDropExp(LivingExperienceDropEvent event)
	{
		// When a mob is killed by a befriended mob, it don't drop exp orbs, but directly add exp to the mob.
		if (event.getEntityLiving().getLastHurtByMob() != null 
				&& event.getEntityLiving().getLastHurtByMob() instanceof IDwmgBefriendedMob bm)
		{
			int exp = event.getOriginalExperience();
			exp = (int)handleMending(exp, bm.asMob());
			bm.getLevelHandler().addExp(exp);
			event.setCanceled(true);
		}
	}
	
	
	// Handle equipment fixing from Mending enchantment for mobs, and return the exp remains
	protected static long handleMending(long expBefore, Mob mob)
	{
		if (expBefore >= (long) Integer.MAX_VALUE)
		{
			throw new UnsupportedOperationException("Adding too many exp (more than INT_MAX).");
		}
		
		int remained = (int)expBefore;
		for (int i = 0; i < 6; ++i)
		{
			if (mob.getItemBySlot(ARMOR_AND_HANDS[i]).isDamageableItem() 
					&& mob.getItemBySlot(ARMOR_AND_HANDS[i]).getDamageValue() > 0
					&& EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MENDING, mob.getItemBySlot(ARMOR_AND_HANDS[i])) > 0)
			{
				// If cannot fix up
				if (mob.getItemBySlot(ARMOR_AND_HANDS[i]).getDamageValue() > remained * 2)
				{
					mob.getItemBySlot(ARMOR_AND_HANDS[i]).setDamageValue(
							(int) (mob.getItemBySlot(ARMOR_AND_HANDS[i]).getDamageValue() - 2 * remained));
					remained = 0;
				}
				else
				{
					int needed = (mob.getItemBySlot(ARMOR_AND_HANDS[i]).getDamageValue() + 1) / 2;
					mob.getItemBySlot(ARMOR_AND_HANDS[i]).setDamageValue(0);
					remained -= needed;
				}
				if (remained < 0)
				{
					throw new RuntimeException("Math error: handleMending");
				}
				else if (remained == 0)
				{
					// Sync inventory
					if (mob instanceof IBefriendedMob bm)
						bm.setInventoryFromMob();
					return 0;
				}
			}
		}
		// Sync inventory
		if (mob instanceof IBefriendedMob bm)
			bm.setInventoryFromMob();
		return (long)remained;
	}
	
	@SubscribeEvent
	public static void onLivingDamage(LivingDamageEvent event)
	{
		if (!event.getEntity().level.isClientSide && event.getEntity() instanceof Mob mob)
		{
			if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof LivingEntity source)
			{
				// Favorbility change
				// On player attack a mob attacking the BM
				if (source instanceof Player player 
						&& mob.getTarget() != null
						&& mob.getTarget() instanceof IDwmgBefriendedMob bm
						&& bm.getOwner() == player)
				{
					bm.getFavorability().addFavorability(event.getAmount() / 50f);
				}
				// On BM attack a mob attacking the player
				if (source instanceof IDwmgBefriendedMob bm
						&& mob.getTarget() != null
						&& mob.getTarget() instanceof Player player
						&& bm.getOwner() == player)
				{
					bm.getFavorability().addFavorability(event.getAmount() / 100f);
				}
			}
		}
	}
	
	@SubscribeEvent
	public static void onEntityTickEnd(ServerEntityTickEvent.PostWorldTick event)
	{
		if (event instanceof IDwmgBefriendedMob bm)
		{
			// For unknown reason, it was observed that a Dummy Item remaining on the mob's head, so clear after tick
			if (bm.asMob().getItemBySlot(EquipmentSlot.HEAD).is(BefMobItems.DUMMY_ITEM.get()))
			{
				bm.asMob().setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
				bm.setInventoryFromMob();
			}
		}
	}

	
	@SubscribeEvent
	public static void onEntityJoinLevel(EntityJoinLevelEvent event)
	{
		if (event.getEntity() instanceof Mob mob && AiHelper.isMobHostileToPlayer(mob) && !(event.getEntity() instanceof IBefriendedMob))
		{
			Predicate<LivingEntity> none = (l) -> true;
			Predicate<LivingEntity> isNotWaiting = DwmgEntityHelper::isNotWaiting;
			Predicate<LivingEntity> isNotWearingGold = DwmgEntityHelper::isNotWearingGold;
			Predicate<LivingEntity> isUndead = (living -> living.getMobType() == MobType.UNDEAD);
			
			// Illagers and witches attack all mobs
			if (mob.getMobType() == MobType.ILLAGER)
			{
				setHostileToAllBefriendedMobs(mob, isNotWaiting);
			}
			// Phantom/dyssomnia hostile to all mobs
			else if (mob instanceof Phantom || mob.getClass() == DyssomniaEntity.class)
			{
				setHostileToAllBefriendedMobs(mob, isNotWaiting);
			}
			// Skeletons hostile to zombies & creepers
			else if (mob instanceof AbstractSkeleton 
				&& !(EntityType.getKey(mob.getType()).getNamespace().equals(HMaG.MODID))	// Exclude HMAG mob girls
				&& AiHelper.isMobHostileToPlayer(mob))	// For hostile mobs only
			{
				AiHelper.setHostileTo(mob, EntityBefriendedZombieGirl.class);
				AiHelper.setHostileTo(mob, EntityBefriendedHuskGirl.class);
				AiHelper.setHostileTo(mob, EntityBefriendedDrownedGirl.class);
				AiHelper.setHostileTo(mob, EntityBefriendedCreeperGirl.class);
			}
			// Zombies (including Zombified Piglins and Z
			oglins) hostile to skeletons & creepers
			if ((mob instanceof Zombie || mob instanceof Zoglin)
					&& !(EntityType.getKey(mob.getType()).getNamespace().equals(HMaG.MODID)))	// Exclude HMAG mob girls
			{
				AiHelper.setHostileTo(mob, EntityBefriendedSkeletonGirl.class);
				AiHelper.setHostileTo(mob, EntityBefriendedStrayGirl.class);
				AiHelper.setHostileTo(mob, EntityBefriendedWitherSkeletonGirl.class);
				AiHelper.setHostileTo(mob, EntityBefriendedCreeperGirl.class);
			}
			// Piglins hostile to all mobs not wearing gold
			if (mob instanceof Piglin)
			{
				setHostileToAllBefriendedMobs(mob.and(isNotWearingGold));
			}
			// Piglin brutes, Hoglins hostile to all mobs
			if (mob instanceof PiglinBrute || mob instanceof Hoglin)
			{
				setHostileToAllBefriendedMobs(mob);
			}
			// Ghasts attack non-undead mobs
			if (mob instanceof Ghast)
			{
				setHostileToAllBefriendedMobs(mob.and(isUndead.negate()));
			}
			// Slimes (including magical) and magma cubes attack all mobs
			if (mob instanceof Slime)
			{
				setHostileToAllBefriendedMobs(mob);
			}
			// Blaze attacks all flying mobs and skeletons (excluding wither)
			if (mob instanceof Blaze)
			{
				AiHelper.setHostileTo(mob, EntityBefriendedSkeletonGirl.class);
				AiHelper.setHostileTo(mob, EntityBefriendedStrayGirl.class);
				AiHelper.setHostileTo(mob, EntityBefriendedHornet.class);
			}
			if (mob instanceof Spider)
			{
				setHostileToAllBefriendedMobs(mob, (living) -> (living.getMobType() != MobType.ARTHROPOD));
			}

		}
	}
	
	/**
	 * Set a monster hostile to all dwmg befriended mobs
	 */
	public static void setHostileToAllBefriendedMobs(Mob mob, Predicate<LivingEntity> condition)
	{
		AiHelper.setHostileTo(mob, EntityBefriendedZombieGirl.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedHuskGirl.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedDrownedGirl.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedSkeletonGirl.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedStrayGirl.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedWitherSkeletonGirl.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedCreeperGirl.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedEnderExecutor.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedNecroticReaper.class, condition);
		AiHelper.setHostileTo(mob, EntityBefriendedHornet.class, condition);
		// Extending...
	}

	protected static boolean shouldPiglinAttack(LivingEntity living)
	{
		return DwmgEntityHelper.isNotWearingGold(living);
	}
	
	
	@SubscribeEvent
	public static void onMobGriefing(EntityMobGriefingEvent event)
	{
		if (event.getEntity() instanceof EntityBefriendedGhastlySeeker gs)
		{
		}
	}
	
}
