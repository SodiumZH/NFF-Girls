package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
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
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraftforge.common.ForgeMod;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.befriendmobs.entity.capability.wrapper.ILivingDelayedActions;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.DwmgMeltyMonsterFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedRangedAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.inventory.InventoryMenuFourBaubles;
import net.sodiumstudio.dwmg.registries.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.registries.DwmgEntityTypes;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.ItemHelper;
import net.sodiumstudio.nautils.NaParticleUtils;
import net.sodiumstudio.nautils.entity.ConditionalAttributeModifier;
import net.sodiumstudio.nautils.math.GeometryUtil;
import net.sodiumstudio.nautils.math.RndUtil;

public class HmagMeltyMonsterEntity extends MeltyMonsterEntity implements IDwmgBefriendedMob, ILivingDelayedActions {

	/** Added in */
	public static final ConditionalAttributeModifier MODIFIER_OWNER_SPEED_UP_IN_LAVA = 
			new ConditionalAttributeModifier(ForgeMod.SWIM_SPEED.get(), 4d, AttributeModifier.Operation.MULTIPLY_BASE, living -> 
			(
				living instanceof Player player 
				&& BefriendedHelper.getOwningMobsInArea(player, DwmgEntityTypes.HMAG_MELTY_MONSTER.get(), 16d, true).size() > 0
				&& player.isInLava())
			);
	public static final ConditionalAttributeModifier MODIFIER_SELF_SPEED_UP_IN_LAVA = 
			new ConditionalAttributeModifier(Attributes.MOVEMENT_SPEED, 0d, AttributeModifier.Operation.MULTIPLY_BASE, living -> 
			(
				living instanceof HmagMeltyMonsterEntity mm
				&& BefriendedHelper.getOwnerInArea(mm, 16d, true).isPresent()
				&& mm.isInLava()
			));
	public static final ConditionalAttributeModifier MODIFIER_SELF_SPEED_UP_ON_GROUND = 
			new ConditionalAttributeModifier(Attributes.MOVEMENT_SPEED, 0d, AttributeModifier.Operation.MULTIPLY_BASE, living -> 
			(
				living instanceof HmagMeltyMonsterEntity mm
				&& BefriendedHelper.getOwnerInArea(mm, 16d, true).isPresent()
				&& mm.isOnGround()
			));
	
	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(HmagMeltyMonsterEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(HmagMeltyMonsterEntity.class, EntityDataSerializers.INT);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_OWNERUUID, Optional.empty());
		entityData.define(DATA_AISTATE, 0);
	}
	
	@Override
	public EntityDataAccessor<Optional<UUID>> getOwnerUUIDAccessor() {
		return DATA_OWNERUUID;
	}

	@Override
	public EntityDataAccessor<Integer> getAIStateData() {
		return DATA_AISTATE;
	}

	/* Initialization */

	public HmagMeltyMonsterEntity(EntityType<? extends HmagMeltyMonsterEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}
	
	public void onInit()
	{
		
	}
	
	/* Behavior */

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
			this.addMultipleDelayedActions(action4, 2, 4, 6, 8, 10, 12, 14, 16, 18);
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

	protected MMFireball fire(LivingEntity target)
	{
		return fire(target.position().subtract(this.position()));
	}
	
	protected MMFireball fire(LivingEntity target, float distanceFactor)
	{
		return fire(target.position().subtract(this.position()));
	}
	
	protected MMFireball fire(Vec3 targetPos)
	{
		MMFireball fireball = newFireball(targetPos);
		this.level.addFreshEntity(fireball);
		return fireball;
	}
	
	protected MMFireball newFireball(Vec3 targetPos)
	{
		double d1 = targetPos.x - this.getX();
		double d2 = targetPos.y - this.getY(0.5D);
		double d3 = targetPos.z - this.getZ();
		//double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.02D;
		MMFireball fireballentity = new MMFireball(this.level, this, d1 /*+ this.getRandom().nextGaussian() * d4*/, d2, d3/* + this.getRandom().nextGaussian() * d4*/);
		fireballentity.setPos(fireballentity.getX(), this.getY(0.5D) + 0.5D, fireballentity.getZ());
		return fireballentity;
	}

	@Override
	protected void customServerAiStep()
	{
		if (this.isInLava() && this.isOwnerPresent() && this.getOwner().isInLava() && this.distanceToSqr(this.getOwner()) <= 64d)
		{
			EntityHelper.addEffectSafe(this.getOwner(), MobEffects.FIRE_RESISTANCE, 19);
		}
		if (this.takingLavaCooldown > 0)
			this.takingLavaCooldown --;
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
		return DwmgHealingItems.NONE;
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
							ItemHelper.giveOrDrop(player, new ItemStack(Items.LAVA_BUCKET, 1));
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
						ItemHelper.giveOrDrop(player, new ItemStack(Items.BUCKET));
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

	// This enables mob armor and hand items by default.
	// If not needed, use BefriendedInventory class instead.
	protected BefriendedInventory additionalInventory = new BefriendedInventory(getInventorySize(), this);

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	@Override
	public int getInventorySize()
	{
		return 4;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			//additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			//additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuFourBaubles(containerId, playerInventory, container, this);
		// You can keep it null, but in this case never call openBefriendedInventory() or it will crash.
	}

	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		// Add other data to save here
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here
		setInit();
	}

	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		return this.continuousBaubleSlots(0, 4);
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}

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
					if (!DwmgEntityHelper.isAlly(owner, living))
						{
						int i = living.getRemainingFireTicks();
						living.setSecondsOnFire((int) Math.round(5.0d * (1d + owner.getAttributeValue(Attributes.ATTACK_DAMAGE))));
						if (!living.hurt(DamageSource.fireball(this, this.owner), (float) (5.0d * (1d + owner.getAttributeValue(Attributes.ATTACK_DAMAGE)))))
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
			return !this.parent.isInLava() && this.isValidTarget(this.parent.level, this.blockPos);
		}

		@Override
		public boolean canUse()
		{
			return !this.parent.isInLava() && super.canUse();
		}

		@Override
		protected boolean isValidTarget(LevelReader levelReader, BlockPos pos)
		{
			return levelReader.getBlockState(pos).is(Blocks.LAVA) && levelReader.getBlockState(pos.above()).isPathfindable(levelReader, pos, PathComputationType.LAND);
		}

	}

}
