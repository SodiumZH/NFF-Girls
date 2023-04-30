package net.sodiumstudio.dwmg.befriendmobs.entity.vanillapreset.enderman;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.target.BefriendedNearestAttackableTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.dwmg.befriendmobs.inventory.BefriendedInventoryWithEquipment;

public abstract class AbstractBefriendedEnderMan extends Monster implements IBefriendedMob, NeutralMob
{

	protected static final UUID SPEED_MODIFIER_ATTACKING_UUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
	protected static final AttributeModifier SPEED_MODIFIER_ATTACKING = new AttributeModifier(
			SPEED_MODIFIER_ATTACKING_UUID, "Attacking speed boost", (double) 0.15F,
			AttributeModifier.Operation.ADDITION);
	protected static final int DELAY_BETWEEN_CREEPY_STARE_SOUND = 400;
	protected static final int MIN_DEAGGRESSION_TIME = 600;
	protected int lastStareSound = Integer.MIN_VALUE;
	protected int targetChangeTime;
	protected static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
	protected int remainingPersistentAngerTime;
	@Nullable
	protected UUID persistentAngerTarget;
	public boolean canAutoTakeBlocks = false;
	public boolean canAutoPlaceBlocks = false;
	public boolean angryOnLookedAt = false;
	public boolean teleportOnHurtByWater = true;
	public boolean teleportNotOnHurtByWater = false;
	public boolean teleportToAvoidProjectile = true;
	protected String modId;
	
	protected static final EntityDataAccessor<Optional<BlockState>> DATA_CARRY_STATE = SynchedEntityData
			.defineId(AbstractBefriendedEnderMan.class, EntityDataSerializers.BLOCK_STATE);
	protected static final EntityDataAccessor<Boolean> DATA_CREEPY = SynchedEntityData
			.defineId(AbstractBefriendedEnderMan.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> DATA_STARED_AT = SynchedEntityData
			.defineId(AbstractBefriendedEnderMan.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(AbstractBefriendedEnderMan.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(AbstractBefriendedEnderMan.class, EntityDataSerializers.BYTE);	
	
	@SuppressWarnings("deprecation")
	public AbstractBefriendedEnderMan(EntityType<? extends AbstractBefriendedEnderMan> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.maxUpStep = 1.0F;
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0f);
		Arrays.fill(this.handDropChances, 0f);
		befriendedInventory = new BefriendedInventory(getInventorySize());
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		//this.goalSelector.addGoal(1, new BefriendedEnderManGoals.FreezeWhenLookedAt(this));
		this.goalSelector.addGoal(2, new BefriendedMeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(7, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
		this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		this.goalSelector.addGoal(10, new BefriendedEnderManGoals.LeaveBlockGoal(this));
		this.goalSelector.addGoal(11, new BefriendedEnderManGoals.TakeBlockGoal(this));
		// this.targetSelector.addGoal(1, new
		// BefriendedEnderManGoals.LookForPlayerGoal(this, this::isAngryAt));
		this.targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new BefriendedNearestAttackableTargetGoal<>(this, Endermite.class, true, false));
		this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
	}

	/**
	 * Sets the active target the Goal system uses for tracking
	 */
	@Override
	public void setTarget(@Nullable LivingEntity pLivingEntity) {
		AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
		if (pLivingEntity == null)
		{
			this.targetChangeTime = 0;
			this.entityData.set(DATA_CREEPY, false);
			this.entityData.set(DATA_STARED_AT, false);
			attributeinstance.removeModifier(SPEED_MODIFIER_ATTACKING);
		} else
		{
			this.targetChangeTime = this.tickCount;
			this.entityData.set(DATA_CREEPY, true);
			if (!attributeinstance.hasModifier(SPEED_MODIFIER_ATTACKING))
			{
				attributeinstance.addTransientModifier(SPEED_MODIFIER_ATTACKING);
			}
		}

		super.setTarget(pLivingEntity); // Forge: Moved down to allow event handlers to write data manager values.
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_CARRY_STATE, Optional.empty());
		this.entityData.define(DATA_CREEPY, false);
		this.entityData.define(DATA_STARED_AT, false);
		this.entityData.define(DATA_OWNERUUID, Optional.empty());
		this.entityData.define(DATA_AISTATE, (byte) 0);
	}

	@Override
	public void startPersistentAngerTimer() {
		/*this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));*/
	}

	@Override
	public void setRemainingPersistentAngerTime(int pTime) {
		/*this.remainingPersistentAngerTime = pTime;*/
	}

	@Override
	public int getRemainingPersistentAngerTime() {
		return 0;/*return this.remainingPersistentAngerTime;*/
	}

	@Override
	public void setPersistentAngerTarget(@Nullable UUID pTarget) {
		/*this.persistentAngerTarget = pTarget;*/
	}

	@Override
	@Nullable
	public UUID getPersistentAngerTarget() {
		return this.persistentAngerTarget;
	}

	public void playStareSound() {
		if (this.tickCount >= this.lastStareSound + 400)
		{
			this.lastStareSound = this.tickCount;
			if (!this.isSilent())
			{
				this.level.playLocalSound(this.getX(), this.getEyeY(), this.getZ(), SoundEvents.ENDERMAN_STARE,
						this.getSoundSource(), 2.5F, 1.0F, false);
			}
		}

	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> pKey) {
		if (DATA_CREEPY.equals(pKey) && this.hasBeenStaredAt() && this.level.isClientSide)
		{
			this.playStareSound();
		}

		super.onSyncedDataUpdated(pKey);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		BlockState blockstate = this.getCarriedBlock();
		if (blockstate != null)
		{
			tag.put("carriedBlockState", NbtUtils.writeBlockState(blockstate));
		}

		this.addPersistentAngerSaveData(tag);
		BefriendedHelper.addBefriendedCommonSaveData(this, tag, BefriendMobs.MOD_ID);
	}

	/**
	 * (abstract) Protected helper method to read subclass mob data from NBT.
	 */
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		BlockState blockstate = null;
		if (tag.contains("carriedBlockState", 10))
		{
			blockstate = NbtUtils.readBlockState(tag.getCompound("carriedBlockState"));
			if (blockstate.isAir())
			{
				blockstate = null;
			}
		}

		this.setCarriedBlock(blockstate);
		this.readPersistentAngerSaveData(this.level, tag);
		BefriendedHelper.readBefriendedCommonSaveData(this, tag, BefriendMobs.MOD_ID);
		/* Add more save data... */
		this.setInit();
	}

	public HashSet<Item> getMaskTypes()
	{
		HashSet<Item> set = new HashSet<Item>();
		set.add(Items.CARVED_PUMPKIN);
		return set;
	}
	
	public boolean isMask(ItemStack stack, Player player) 
	{
		return getMaskTypes().contains(stack.getItem());
	}

	/**
	 * Checks to see if this AbstractBefriendedEnderMan should be attacking this
	 * player
	 *//*
	public boolean isLookingAtMe(Player player) {
		ItemStack helmet = player.getInventory().armor.get(3);
		if (!this.angryOnLookedAt
				|| isMask(helmet, player) 				 
				|| this.getOwnerUUID() != null && this.getOwnerUUID().equals(player.getUUID())
				|| MinecraftForge.EVENT_BUS.post(new BefriendedEnderManAngerEvent(this, player)))
		{
			return false;
		} else
		{
			Vec3 vec3 = player.getViewVector(1.0F).normalize();
			Vec3 vec31 = new Vec3(this.getX() - player.getX(), this.getEyeY() - player.getEyeY(),
					this.getZ() - player.getZ());
			double d0 = vec31.length();
			vec31 = vec31.normalize();
			double d1 = vec3.dot(vec31);
			return d1 > 1.0D - 0.025D / d0 ? player.hasLineOfSight(this) : false;
		}
	}
*/
	@Override
	protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
		return 2.55F;
	}

	/**
	 * Called every tick so the mob can update its state as required. For
	 * example, zombies and skeletons use this to react to sunlight and start to
	 * burn.
	 */
	@Override
	public void aiStep() {
		if (this.level.isClientSide)
		{
			for (int i = 0; i < 2; ++i)
			{
				this.level.addParticle(ParticleTypes.PORTAL, this.getRandomX(0.5D), this.getRandomY() - 0.25D,
						this.getRandomZ(0.5D), (this.random.nextDouble() - 0.5D) * 2.0D, -this.random.nextDouble(),
						(this.random.nextDouble() - 0.5D) * 2.0D);
			}
		}

		this.jumping = false;
		if (!this.level.isClientSide)
		{
			this.updatePersistentAnger((ServerLevel) this.level, true);
		}

		super.aiStep();
	}

	@Override
	public boolean isSensitiveToWater() {
		return true;
	}

	@Override
	protected void customServerAiStep() {
/*
		if (this.level.isDay() && this.tickCount >= this.targetChangeTime + 600)
		{
			float f = this.getBrightness();
			if (f > 0.5F && this.level.canSeeSky(this.blockPosition())
					&& this.random.nextFloat() * 30.0F < (f - 0.4F) * 2.0F)
			{
				this.setTarget((LivingEntity) null);
				this.teleport();
			}
		}
*/
		super.customServerAiStep();
	}

	public boolean tryTeleportOnWaterHurt(int tryTimes)
	{
		if (!this.teleportOnHurtByWater)
			return false;
		for (int i = 0; i < tryTimes; ++i)
		{
			if (this.teleport())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean tryTeleportToAvoidProjectile(int tryTimes)
	{
		if (!teleportToAvoidProjectile)
			return false;
		for (int i = 0; i < tryTimes; ++i)
		{
			if (this.teleport())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean tryTeleportInOtherCases(int tryTimes)
	{
		if (!this.teleportNotOnHurtByWater)
			return false;
		for (int i = 0; i < tryTimes; ++i)
		{
			if (this.teleport())
			{
				return true;
			}
		}
		return false;
	}
	
	public boolean teleport() {
		if (!this.level.isClientSide() && this.isAlive())
		{
			double d0 = this.getX() + (this.random.nextDouble() - 0.5D) * 64.0D;
			double d1 = this.getY() + (double) (this.random.nextInt(64) - 32);
			double d2 = this.getZ() + (this.random.nextDouble() - 0.5D) * 64.0D;
			return this.teleport(d0, d1, d2);
		} else
		{
			return false;
		}
	}

	public boolean teleportTowards(Entity pTarget) {
		Vec3 vec3 = new Vec3(this.getX() - pTarget.getX(), this.getY(0.5D) - pTarget.getEyeY(),
				this.getZ() - pTarget.getZ());
		vec3 = vec3.normalize();
		double d1 = this.getX() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3.x * 16.0D;
		double d2 = this.getY() + (double) (this.random.nextInt(16) - 8) - vec3.y * 16.0D;
		double d3 = this.getZ() + (this.random.nextDouble() - 0.5D) * 8.0D - vec3.z * 16.0D;
		return this.teleport(d1, d2, d3);
	}

	public boolean teleport(double pX, double pY, double pZ) {
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(pX, pY, pZ);

		while (blockpos$mutableblockpos.getY() > this.level.getMinBuildHeight()
				&& !this.level.getBlockState(blockpos$mutableblockpos).getMaterial().blocksMotion())
		{
			blockpos$mutableblockpos.move(Direction.DOWN);
		}

		BlockState blockstate = this.level.getBlockState(blockpos$mutableblockpos);
		boolean flag = blockstate.getMaterial().blocksMotion();
		boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
		if (flag && !flag1)
		{
			net.minecraftforge.event.entity.EntityTeleportEvent.EnderEntity event = net.minecraftforge.event.ForgeEventFactory
					.onEnderTeleport(this, pX, pY, pZ);
			if (event.isCanceled())
				return false;
			boolean flag2 = this.randomTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
			if (flag2 && !this.isSilent())
			{
				this.level.playSound((Player) null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT,
						this.getSoundSource(), 1.0F, 1.0F);
				this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}

			return flag2;
		} else
		{
			return false;
		}
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.isCreepy() ? SoundEvents.ENDERMAN_SCREAM : SoundEvents.ENDERMAN_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.ENDERMAN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENDERMAN_DEATH;
	}

	@Override
	protected void dropCustomDeathLoot(DamageSource pSource, int pLooting, boolean pRecentlyHit) {
		super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
		BlockState blockstate = this.getCarriedBlock();
		if (blockstate != null)
		{
			this.spawnAtLocation(blockstate.getBlock());
		}

	}

	public void setCarriedBlock(@Nullable BlockState pState) {
		this.entityData.set(DATA_CARRY_STATE, Optional.ofNullable(pState));
	}

	@Nullable
	public BlockState getCarriedBlock() {
		return this.entityData.get(DATA_CARRY_STATE).orElse((BlockState) null);
	}

	/**
	 * Called when the mob is attacked.
	 */
	@Override
	public boolean hurt(DamageSource pSource, float pAmount) {
		if (this.isInvulnerableTo(pSource))
		{
			return false;
		} 
		else if (pSource.equals(DamageSource.DROWN))
		{
			return this.tryTeleportOnWaterHurt(64);
		}
		
		else if (pSource instanceof IndirectEntityDamageSource)
		{
			Entity entity = pSource.getDirectEntity();
			boolean isByWater;
			if (entity instanceof ThrownPotion)
			{
				isByWater = this.hurtWithCleanWater(pSource, (ThrownPotion) entity, pAmount);
			} else
			{
				isByWater = false;
			}
			
			if (isByWater)	// Hurt by water bottle
			{
				this.tryTeleportOnWaterHurt(64);
				return true;
			}
			else	// Hurt by projectile
			{
				return this.tryTeleportToAvoidProjectile(64);
			}
		}
		else
		{
			boolean flag = super.hurt(pSource, pAmount);
			if (!this.level.isClientSide() && !(pSource.getEntity() instanceof LivingEntity)
					&& this.random.nextInt(10) != 0)
			{
				this.tryTeleportInOtherCases(1);
			}
			return flag;
		}
	}

	protected boolean hurtWithCleanWater(DamageSource pSource, ThrownPotion pPotion, float pAmount) {
		if (!this.isSensitiveToWater())
			return false;
		ItemStack itemstack = pPotion.getItem();
		Potion potion = PotionUtils.getPotion(itemstack);
		List<MobEffectInstance> list = PotionUtils.getMobEffects(itemstack);
		boolean flag = potion == Potions.WATER && list.isEmpty();
		return flag ? super.hurt(pSource, pAmount) : false;
	}

	public boolean isCreepy() {
		return this.entityData.get(DATA_CREEPY);
	}

	public boolean hasBeenStaredAt() {
		return this.entityData.get(DATA_STARED_AT);
	}

	public void setBeingStaredAt() {
		this.entityData.set(DATA_STARED_AT, true);
	}

	@Override
	public boolean requiresCustomPersistence() {
		return super.requiresCustomPersistence() || this.getCarriedBlock() != null;
	}

	/* IBefriendedMob interface */

	// Initialization

	// Initialization end

	// Attributes

	@Override
	public void updateAttributes() {
		/* Update attributes here */
		/* It will auto-called on initialization and container update. */

	}

	// Interaction

	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {

		if (player.getUUID().equals(getOwnerUUID()))
		{
			if (!player.level.isClientSide())
			{
				switchAIState();
			}
			return true;
		}
		/* Other actions */
		return false;
	}

	@Override
	public boolean onInteractionShift(Player player, InteractionHand hand) {
		if (player.getUUID().equals(getOwnerUUID()))
		{

			if (hand.equals(InteractionHand.MAIN_HAND))
				BefriendedHelper.openBefriendedInventory(player, this);
			return true;
		}
		/* Other actions... */
		return false;
	}

	// Interaction end

	// Inventory related
	// Generally no need to modify unless noted

	protected BefriendedInventory befriendedInventory;

	@Override
	public BefriendedInventory getAdditionalInventory() {
		return befriendedInventory;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide)
		{
			/*
			 * If mob's properties (e.g. equipment, HP, etc.) needs to sync with inventory,
			 * set here
			 */
		}
	}

	@Override
	public void setInventoryFromMob() {
		if (!this.level.isClientSide)
		{
			/*
			 * If inventory needs to be set from mob's properties on initialization, set
			 * here
			 */
		}
	}


	// Inventory end
	
}
