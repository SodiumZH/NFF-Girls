package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.github.mechalopa.hmag.world.entity.MeltyMonsterEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.befriendmobs.entity.capability.wrapper.ILivingDelayedActions;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.DwmgMeltyMonsterFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedRangedAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.inventory.InventoryMenuMeltyMonster;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NaItemUtils;
import net.sodiumstudio.nautils.NaParticleUtils;
import net.sodiumstudio.nautils.entity.ConditionalAttributeModifier;
import net.sodiumstudio.nautils.math.GeometryUtil;
import net.sodiumstudio.nautils.math.RndUtil;

public class HmagMeltyMonsterEntity extends MeltyMonsterEntity implements IDwmgBefriendedMob, ILivingDelayedActions {

	public static final ConditionalAttributeModifier MODIFIER_SLOWNESS_ON_LOW_STAMINA = 
			new ConditionalAttributeModifier(Attributes.MOVEMENT_SPEED, -0.5d,  AttributeModifier.Operation.MULTIPLY_TOTAL, living ->
			(
					living instanceof HmagMeltyMonsterEntity mm
					&& mm.getStamina() <= 0
			));
	public static final ConditionalAttributeModifier MODIFIER_SPEED_WITH_LAVA_BUCKET = 
			new ConditionalAttributeModifier(Attributes.MOVEMENT_SPEED, 0.5d,  AttributeModifier.Operation.MULTIPLY_BASE, living ->
			(
					living instanceof HmagMeltyMonsterEntity mm
					&& mm.getAdditionalInventory().getItem(4).is(Items.LAVA_BUCKET)
			));
	
	/* Data sync */

	protected static final EntityDataAccessor<Integer> DATA_STAMINA = SynchedEntityData
			.defineId(HmagMeltyMonsterEntity.class, EntityDataSerializers.INT);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_STAMINA, 10000);;
	}

	/* Initialization */

	public HmagMeltyMonsterEntity(EntityType<? extends HmagMeltyMonsterEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}
	
	@Override
	public void onInit(UUID playerUUID, Mob source)
	{
		if (source != null && source.getClass().equals(MeltyMonsterEntity.class))
			this.setStamina(10000);
		MODIFIER_SLOWNESS_ON_LOW_STAMINA.apply(this);
		MODIFIER_SPEED_WITH_LAVA_BUCKET.apply(this);
	}
	
	/* Behavior */
	
	// Melty Monster consumes stamina when getting on the ground and firing, and gets stamina when in lava.
	public int getStamina()
	{
		return this.getEntityData().get(DATA_STAMINA);
	}
	
	public void setStamina(int value)
	{
		this.getEntityData().set(DATA_STAMINA, Mth.clamp(value, 0, getMaxStamina()));
	}
	
	public int getMaxStamina()
	{
		return Math.min(10000 + this.getLevelHandler().getExpectedLevel() * 1000, 1000000);
	}
	
	@Override
	protected void registerGoals() {
		goalSelector.addGoal(3, new GoToLavaGoal(this, 1.5D));
		goalSelector.addGoal(5, new DwmgBefriendedRangedAttackGoal(this, 1.0D, 30, 40, 8.0F));
		goalSelector.addGoal(5, new DwmgMeltyMonsterFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));
	}
	
	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor)
	{
		Consumer<Vec3> action = (offset) ->
		{
			this.fire(target.getBoundingBox().getCenter().add(offset));
		};
		Runnable action1 = () -> {
			action.accept(Vec3.ZERO);
			this.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		};
		switch (getFireLevel())
		{
		case 0: 
		{
			action1.run();
			break;
		}
		case 1:
		{
			action1.run();
			this.addMultipleDelayedActions(action1, 4, 8);
			break;
		}
		case 2:
		{
			Runnable action2 = () -> {
				action1.run();
				for (int j = 0; j < 3; ++j)
					action.accept(GeometryUtil.randomVector().scale(RndUtil.rndRangedDouble(0, 2)));
			};
			action2.run();
			this.addMultipleDelayedActions(action2, 3, 6, 9, 12);
			break;
		}
		case 3:
		{
			Runnable action3 = () -> {
				action1.run();
				for (int j = 0; j < 6; ++j)
					action.accept(GeometryUtil.randomVector().scale(RndUtil.rndRangedDouble(0, 2)));
			};
			action3.run();
			this.addMultipleDelayedActions(action3, 3, 6, 9, 12, 15, 18);
			break;
		}
		case 4:
		{
			Runnable action4 = () -> {
				action1.run();
				for (int j = 0; j < 9; ++j)
					action.accept(GeometryUtil.randomVector().scale(RndUtil.rndRangedDouble(0, 3)));
			};
			action4.run();
			this.addMultipleDelayedActions(action4, NaContainerUtils.intRangeArray(2, 20, 2));
			break;
		}
		default: 
		{
			throw new RuntimeException();
		}
		}
		
	}
	
	protected int getFireLevel()
	{
		return this.getLevelHandler().getExpectedLevel() < 15 ? 0 : (
				this.getLevelHandler().getExpectedLevel() < 30 ? 1 : (
				this.getLevelHandler().getExpectedLevel() < 50 ? 2 : (
				this.getLevelHandler().getExpectedLevel() < 80 ? 3 : 4)));
	}
	
	protected void playFiringSound()
	{
		this.playSound(SoundEvents.BLAZE_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	}

	protected Optional<MMFireball> fire(LivingEntity target)
	{
		return fire(target.position().subtract(this.position()));
	}
	
	protected Optional<MMFireball> fire(LivingEntity target, float distanceFactor)
	{
		return fire(target.position().subtract(this.position()));
	}
	
	protected Optional<MMFireball> fire(Vec3 targetPos)
	{
		if (getStamina() > 5)
		{
			MMFireball fireball = newFireball(targetPos);
			this.setStamina(this.getStamina() - 5);
			this.level.addFreshEntity(fireball);
			return Optional.of(fireball);
		}
		else return Optional.empty();
	}
	
	protected MMFireball newFireball(Vec3 targetPos)
	{
		double d1 = targetPos.x - this.getX();
		double d2 = targetPos.y - this.getY(0.5D);
		double d3 = targetPos.z - this.getZ();
		//double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.02D;
		MMFireball fireballentity = new MMFireball(this.level, this, d1 /*+ this.getRandom().nextGaussian() * d4*/, d2, d3/* + this.getRandom().nextGaussian() * d4*/);
		fireballentity.setPos(fireballentity.getX(), this.getY(0.5D) + 0.5D, fireballentity.getZ());
		fireballentity.owner = this;
		return fireballentity;
	}

	@Override
	protected void customServerAiStep()
	{
		if (this.isOwnerInDimension() && this.getOwner().isInLava() && this.distanceToSqr(this.getOwner()) <= 64d)
		{
			EntityHelper.addEffectSafe(this.getOwner(), MobEffects.FIRE_RESISTANCE, 19);
		}
		if (this.takingLavaCooldown > 0)
			this.takingLavaCooldown --;
		if (this.isInLava())
		{
			// Add 5 each second
			if (this.tickCount % 4 == 0 && this.getStamina() < this.getMaxStamina())
				this.setStamina(this.getStamina() + 1 + getFireLevel() / 2);
		}
		else
		{
			// Consume 2 each second
			if (this.tickCount % 10 == 0 && this.getStamina() > 0)
			{
				if (this.getAdditionalInventory().getItem(4).is(Items.LAVA_BUCKET))
				{
					if (this.tickCount % 20 == 0)
						this.setStamina(this.getStamina() - 1);
				}
				else this.setStamina(this.getStamina() - 1);
			}
		}
		// Lava bath with it can slowly increase the favorability
		if (this.isInLava() 
				&& this.level.getBlockState(new BlockPos(this.getEyePosition())).is(Blocks.AIR)
				&& this.isOwnerInDimension()
				&& this.getOwner().isInLava()
				&& this.level.getBlockState(new BlockPos(this.getOwner().getEyePosition())).is(Blocks.AIR)
				&& this.getEyePosition().distanceToSqr(this.getOwner().getEyePosition()) < 9d
				&& this.hasLineOfSight(this.getOwner())
				&& this.tickCount % 5 == 0)	// Invoke 4 times per second
			
		{
			this.getFavorabilityHandler().addFavorability(1f / 240f);	// 1 per minute
		}
	}
	
	protected boolean shouldSetFire = true;
	
	public boolean shouldSetFire() {
		return shouldSetFire;
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HealingItemTable getHealingItems()
	{
		return DwmgHealingItems.BLAZE;
	}
	
	protected int takingLavaCooldown = 0;
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (player.getUUID().equals(getOwnerUUID())) {
			// For normal interaction
			if (!player.isShiftKeyDown())
			{
				if (!player.level.isClientSide()) 
				{
					/* Put checks before healing item check */
					// You can take a bucket of lava each 5 minutes
					if (player.getItemInHand(hand).is(Items.BUCKET))
					{
						if (this.takingLavaCooldown <= 0)
						{
							player.getItemInHand(hand).shrink(1);
							NaItemUtils.giveOrDrop(player, new ItemStack(Items.LAVA_BUCKET, 1));
							this.takingLavaCooldown = 5 * 60 * 20;	// 5 min
						}
						else
						{
							NaParticleUtils.sendSmokeParticlesToEntityDefault(this);
						}
					}
					// Use water bucket to suppress setting fire
					else if (player.getItemInHand(hand).is(Items.WATER_BUCKET) && this.shouldSetFire)
					{
						this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.GENERIC_EXTINGUISH_FIRE,
								this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
						player.getItemInHand(hand).shrink(1);
						NaItemUtils.giveOrDrop(player, new ItemStack(Items.BUCKET));
						this.shouldSetFire = false;
					}
					// Use Flint and Steel to allow setting fire
					else if (player.getItemInHand(hand).is(Items.FLINT_AND_STEEL) && !this.shouldSetFire)
					{
						this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE,
								this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
						if (!this.level.isClientSide)
						{
							player.getItemInHand(hand).hurtAndBreak(1, player, (p) ->
							{
								p.broadcastBreakEvent(hand);
							});
						}
						this.shouldSetFire = true;
					}
					// Healing items
					else if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					// The function above returns PASS when the items are not correct. So when not PASS it should stop here
					else if (hand == InteractionHand.MAIN_HAND
							&& DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}
					// Here it's main hand but no interaction. Return pass to enable off hand interaction.
					else return InteractionResult.PASS;
				}
				// Interacted
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			// For interaction with shift key down
			else
			{
				// Open inventory and GUI
				if (hand == InteractionHand.MAIN_HAND && DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
				{
					BefriendedHelper.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
		} 
		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}
	
	/* Inventory */

	@Override
	public BefriendedInventory createAdditionalInventory() {
		return new BefriendedInventory(5, this);
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuMeltyMonster(containerId, playerInventory, container, this);
		// You can keep it null, but in this case never call openBefriendedInventory() or it will crash.
	}

	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		// Add other data to save here
		nbt.putInt("taking_lava_cooldown", this.takingLavaCooldown);
		nbt.putInt("stamina", getStamina());
		nbt.putBoolean("should_set_fire", shouldSetFire());
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here
		this.takingLavaCooldown = nbt.getInt("taking_lava_cooldown");
		this.setStamina(nbt.getInt("stamina"));
		this.shouldSetFire = nbt.getBoolean("should_set_fire");
		setInit();
	}
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		return this.continuousBaubleSlots(0, 4);
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}
*/
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.generalAmbient(super.getAmbientSound());
		/* Change only when it's using zombie or skeleton variation sounds */
	}
	
	// Misc
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return Dwmg.MOD_ID;
	}
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //
/*
	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	@Override
	public boolean isPreventingPlayerRest(Player pPlayer) {
		return false;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}
*/
	// ========================= General Settings end ========================= //
	// ======================================================================== //

	public static class MMFireball extends SmallFireball
	{
		
		protected HmagMeltyMonsterEntity owner;

		public MMFireball(EntityType<? extends SmallFireball> pEntityType, Level pLevel)
		{
			super(pEntityType, pLevel);
		}
		public MMFireball(Level pLevel, double pX, double pY, double pZ, double pOffsetX, double pOffsetY,
				double pOffsetZ)
		{
			super(pLevel, pX, pY, pZ, pOffsetX, pOffsetY, pOffsetZ);
		}

		public MMFireball(Level pLevel, LivingEntity pShooter, double pOffsetX, double pOffsetY, double pOffsetZ)
		{
			super(pLevel, pShooter, pOffsetX, pOffsetY, pOffsetZ);
		}

		/**
		 * Called when the arrow hits an entity
		 */
		@Override
		protected void onHitEntity(EntityHitResult pResult) 
		{
			if (pResult.getEntity() instanceof LivingEntity living)
			{
				if (!this.level.isClientSide)
				{
					if (!BefriendedHelper.isLivingAlliedToBM(owner, living))
						{
						int i = living.getRemainingFireTicks();
						living.setSecondsOnFire((int) Math.round(5.0d * (1d + owner.getAttributeValue(Attributes.ATTACK_DAMAGE))));
						if (!living.hurt(DamageSource.fireball(this, this.owner), (float) (owner.getAttributeValue(Attributes.ATTACK_DAMAGE))))
						{
							living.setRemainingFireTicks(i);
						} else if (this.owner instanceof LivingEntity)
						{
							this.doEnchantDamageEffects((LivingEntity) this.owner, living);
						}
					}
				}
				this.discard();
			}
		}

		@Override
		protected void onHitBlock(BlockHitResult pResult) {
		      BlockState blockstate = this.level.getBlockState(pResult.getBlockPos());
		      blockstate.onProjectileHit(this.level, blockstate, pResult, this);
		      this.discard();
		}
	}
	
	protected static class GoToLavaGoal extends MoveToBlockGoal
	{
		private final HmagMeltyMonsterEntity parent;
		protected Predicate<HmagMeltyMonsterEntity> condition = (living) -> true;
		
		private GoToLavaGoal(HmagMeltyMonsterEntity mob, double d0)
		{
			super(mob, d0, 8, 2);
			this.parent = mob;
		}

		public GoToLavaGoal condition(Predicate<HmagMeltyMonsterEntity> condition)
		{
			this.condition = condition;
			return this;
		}
		
		@Override
		public BlockPos getMoveToTarget()
		{
			return this.blockPos;
		}

		@Override
		public boolean canContinueToUse()
		{
			if (parent.getAIState() == BefriendedAIState.FOLLOW && this.parent.getStamina() > this.parent.getMaxStamina() / 5)
				return false;
			return !this.parent.isInLava() && this.isValidTarget(this.parent.level, this.blockPos) && (!this.parent.getAdditionalInventory().getItem(4).is(Items.LAVA_BUCKET) || this.parent.getStamina() == 0);
		}

		@Override
		public boolean canUse()
		{
			if (parent.getAIState() == BefriendedAIState.FOLLOW && this.parent.getStamina() > this.parent.getMaxStamina() / 5)
				return false;
			return !this.parent.isInLava() && super.canUse() && (!this.parent.getAdditionalInventory().getItem(4).is(Items.LAVA_BUCKET) || this.parent.getStamina() == 0) && this.parent.getStamina() < this.parent.getMaxStamina() / 5;
		}

		@Override
		protected boolean isValidTarget(LevelReader levelReader, BlockPos pos)
		{
			return levelReader.getBlockState(pos).is(Blocks.LAVA) && levelReader.getBlockState(pos.above()).isPathfindable(levelReader, pos, PathComputationType.LAND);
		}

	}

}
