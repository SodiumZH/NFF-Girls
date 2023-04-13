package net.sodiumstudio.dwmg.befriendmobs.entity.vanillapreset.creeper;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PowerableMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.sodiumstudio.dwmg.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventoryWithEquipment;

public abstract class AbstractBefriendedCreeper extends Monster implements IBefriendedMob, PowerableMob
{

	/* Adjusted from vanilla creeper */

	protected static final EntityDataAccessor<Integer> DATA_SWELL_DIR = SynchedEntityData
			.defineId(AbstractBefriendedCreeper.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Boolean> DATA_IS_POWERED = SynchedEntityData
			.defineId(AbstractBefriendedCreeper.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Boolean> DATA_IS_IGNITED = SynchedEntityData
			.defineId(AbstractBefriendedCreeper.class, EntityDataSerializers.BOOLEAN);
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(AbstractBefriendedCreeper.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(AbstractBefriendedCreeper.class, EntityDataSerializers.BYTE);
	protected static final EntityDataAccessor<Integer> DATA_SWELL = SynchedEntityData
			.defineId(AbstractBefriendedCreeper.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Integer> DATA_SWELL_LAST_TICK= SynchedEntityData
			.defineId(AbstractBefriendedCreeper.class, EntityDataSerializers.INT);
	protected String modId;

	public int maxSwell = 30;

	protected int explosionRadius = 3;
	protected int droppedSkulls;
	public boolean canExplode = true;
	public boolean canIgnite = true;
	public boolean shouldDiscardAfterExplosion = false;
	public boolean shouldDestroyBlocks = false;
	public int ignitionCooldownTicks = 200;
	protected int currentIgnitionCooldown = 0;
	
	
	public AbstractBefriendedCreeper(EntityType<? extends AbstractBefriendedCreeper> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0f);
		Arrays.fill(this.handDropChances, 0f);
		/* Initialization */
	}

	// Swell value related
	public int getSwell()
	{
		return entityData.get(DATA_SWELL);
	}

	public void setSwell(int val)
	{
		entityData.set(DATA_SWELL, val);
	}
	
	protected int getSwellLastTick()
	{
		return entityData.get(DATA_SWELL_LAST_TICK);
	}
	
	protected void setSwellLastTick(int val)
	{
		entityData.set(DATA_SWELL_LAST_TICK, val);
	}
	
	// Update swell from SwellDir
	protected void updateSwell()
	{
		entityData.set(DATA_SWELL_LAST_TICK, getSwell());
		int newSwell = getSwell() + getSwellDir();
		newSwell = Mth.clamp(newSwell, 0, maxSwell);
		setSwell(newSwell);
	}
	
	
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	@Override
	public int getMaxFallDistance() {
		return this.getTarget() == null ? 3 : 3 + (int) (this.getHealth() - 1.0F);
	}

	@Override
	public boolean causeFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
		boolean flag = super.causeFallDamage(pFallDistance, pMultiplier, pSource);
		this.setSwell(getSwell() + (int) (pFallDistance * 1.5F));
		if (this.getSwell() > this.maxSwell - 5)
		{
			this.setSwell(this.maxSwell - 5);
		}

		return flag;
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(DATA_SWELL_DIR, -1);
		this.entityData.define(DATA_IS_POWERED, false);
		this.entityData.define(DATA_IS_IGNITED, false);
		this.entityData.define(DATA_OWNERUUID, Optional.empty());
		this.entityData.define(DATA_AISTATE, (byte) 0);
		this.entityData.define(DATA_SWELL, 0);
		this.entityData.define(DATA_SWELL_LAST_TICK, 0);
		//this.entityData.define();
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (this.entityData.get(DATA_IS_POWERED))
		{
			tag.putBoolean("powered", true);
		}

		tag.putShort("Fuse", (short) this.maxSwell);
		tag.putByte("ExplosionRadius", (byte) this.explosionRadius);
		tag.putBoolean("ignited", this.isIgnited());
		tag.putInt("current_ignition_cooldown", currentIgnitionCooldown);
		tag.putInt("ignition_cooldown", ignitionCooldownTicks);
		
		BefriendedHelper.addBefriendedCommonSaveData(this, tag, this.modId);
	}

	/**
	 * (abstract) Protected helper method to read subclass mob data from NBT.
	 */
	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.entityData.set(DATA_IS_POWERED, tag.getBoolean("powered"));
		if (tag.contains("Fuse", 99))
		{
			this.maxSwell = tag.getShort("Fuse");
		}

		if (tag.contains("ExplosionRadius", 99))
		{
			this.explosionRadius = tag.getByte("ExplosionRadius");
		}

		if (tag.getBoolean("ignited"))
		{
			this.setIgnited(true);
		}
		if (tag.contains("ignition_cooldown"))
			this.ignitionCooldownTicks = tag.getInt("ignition_cooldown");
		if (tag.contains("current_ignition_cooldown"))
			this.currentIgnitionCooldown = tag.getInt("current_ignition_cooldown");
		
		BefriendedHelper.readBefriendedCommonSaveData(this, tag, this.modId);
		/* Add more save data... */
		this.setInit();
	}

	/**
	 * Called to update the mob's position/logic.
	 */
	@Override
	public void tick() {
		if (this.isAlive())
		{
			if (this.isIgnited())
			{
				this.setSwellDir(1);
			}

			if (getSwellDir() > 0 && this.getSwell() == 0)
			{
				this.playSound(SoundEvents.CREEPER_PRIMED, 1.0F, 0.5F);
				this.gameEvent(GameEvent.PRIME_FUSE);
			}

			updateSwell();

			if (this.getSwell() == this.maxSwell)
			{
				this.explodeCreeper();
			}
			
			if (currentIgnitionCooldown > 0)
				currentIgnitionCooldown--;
			
		} 
		super.tick();
	}

		
	

	/**
	 * Sets the active target the Goal system uses for tracking
	 */
	@Override
	public void setTarget(@Nullable LivingEntity pTarget) {
		if (!(pTarget instanceof Goat))
		{
			super.setTarget(pTarget);
		}
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource pDamageSource) {
		return SoundEvents.CREEPER_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.CREEPER_DEATH;
	}

	@Override
	public boolean doHurtTarget(Entity target) {
		return super.doHurtTarget(target);
	}

	@Override
	public boolean isPowered() {
		return this.entityData.get(DATA_IS_POWERED);
	}

	public void setPowered(boolean powered)
	{
		this.entityData.set(DATA_IS_POWERED, powered);
	}
	
	/**
	 * Params: (Float)Render tick. Returns the intensity of the creeper's flash when
	 * it is ignited.
	 */
	public float getSwelling(float pPartialTicks) {
		return Mth.lerp(pPartialTicks, (float) this.getSwellLastTick(), (float) this.getSwell()) / (float) (this.maxSwell - 2);
	}

	/**
	 * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
	 */
	public int getSwellDir() {
		return this.entityData.get(DATA_SWELL_DIR);
	}

	/**
	 * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
	 */
	 // Use setSwelling() instead
	public void setSwellDir(int val) {
		this.entityData.set(DATA_SWELL_DIR, val);
	}

	@Deprecated // Use getSwellDir instead
	public boolean isSwelling() {
		return this.entityData.get(DATA_SWELL_DIR) > 0;
	}

	@Deprecated // Use setSwellDir instead
	public void setSwelling(boolean swelling) {
		this.entityData.set(DATA_SWELL_DIR, swelling ? 1 : -1);
	}

	@Override
	public void thunderHit(ServerLevel level, LightningBolt lightning) {
		super.thunderHit(level, lightning);
		this.entityData.set(DATA_IS_POWERED, true);
	}

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		return super.mobInteract(player, hand);
	}

	/**
	 * Creates an explosion as determined by this creeper's power and explosion
	 * radius.
	 */
	protected void explodeCreeper() {
		if (!this.level.isClientSide)
		{
			Explosion.BlockInteraction explosion$blockinteraction = shouldDestroyBlocks && net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)
					? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
			float f = this.isPowered() ? 2.0F : 1.0F;

			if (!shouldDiscardAfterExplosion)
			{
				// Set 2 tick invulnerable to avoid being damaged by the explosion
				this.invulnerableTime += 2;
				this.resetExplosionProcess();
			} else
			{
				this.dead = true;
			}

			this.level.explode(this, this.getX(), this.getY(), this.getZ(), (float) this.explosionRadius * f,
					explosion$blockinteraction);

			if (shouldDiscardAfterExplosion)
				this.discard();

			this.spawnLingeringCloud();
		}
		else
		{
			if (!shouldDiscardAfterExplosion)
				this.resetExplosionProcess();
		}
	}

	protected void resetExplosionProcess()
	{
		this.setIgnited(false);
		this.setSwellDir(-1);
		this.setSwell(0);
		this.setSwellLastTick(0);
		this.currentIgnitionCooldown = 0;
	}
	
	protected void spawnLingeringCloud() {
		Collection<MobEffectInstance> collection = this.getActiveEffects();
		if (!collection.isEmpty())
		{
			AreaEffectCloud areaeffectcloud = new AreaEffectCloud(this.level, this.getX(), this.getY(), this.getZ());
			areaeffectcloud.setRadius(2.5F);
			areaeffectcloud.setRadiusOnUse(-0.5F);
			areaeffectcloud.setWaitTime(10);
			areaeffectcloud.setDuration(areaeffectcloud.getDuration() / 2);
			areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float) areaeffectcloud.getDuration());

			for (MobEffectInstance mobeffectinstance : collection)
			{
				areaeffectcloud.addEffect(new MobEffectInstance(mobeffectinstance));
			}

			this.level.addFreshEntity(areaeffectcloud);
		}

	}

	public boolean isIgnited() {
		return this.entityData.get(DATA_IS_IGNITED);
	}

	@Deprecated // Use setIgnited instead
	public void ignite() {
		setIgnited(true);
	}

	public void setIgnited(boolean ignited) {
		this.entityData.set(DATA_IS_IGNITED, ignited);
	}

	/**
	 * Returns true if an mob is able to drop its skull due to being blown up by
	 * this creeper.
	 * 
	 * Does not test if this creeper is charged" the caller must do that. However,
	 * does test the doMobLoot gamerule.
	 */
	public boolean canDropMobsSkull() {
		return this.isPowered() && this.droppedSkulls < 1;
	}

	public void increaseDroppedSkulls() {
		++this.droppedSkulls;
	}

	/* BefriendedMob setup */

	// Attributes

	@Override
	public void updateAttributes() {
		/* Update attributes here */
		/* It will auto-called on initialization and container update. */

	}

	// Interaction

	public boolean playerIgniteDefault(Player player, InteractionHand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(Items.FLINT_AND_STEEL) && this.canIgnite && this.currentIgnitionCooldown == 0)
		{
			this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE,
					this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
			if (!this.level.isClientSide)
			{
				this.setIgnited(true);
				itemstack.hurtAndBreak(1, player, (p) ->
				{
					p.broadcastBreakEvent(hand);
				});
			}
			return true;
		}
		return false;
	}
	
	public boolean playerIgniteDefault(Player player, InteractionHand hand, Item ignitingItem)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(ignitingItem) && this.canIgnite && this.currentIgnitionCooldown == 0)
		{
			this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE,
					this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
			if (!this.level.isClientSide)
			{
				this.setIgnited(true);
				itemstack.hurtAndBreak(1, player, (p) ->
				{
					p.broadcastBreakEvent(hand);
				});
			}
			return true;
		}
		return false;
	}
	
	public boolean playerIgnite(Player player, InteractionHand hand, Item ignitingItem)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		if (itemstack.is(ignitingItem) && this.canIgnite && this.currentIgnitionCooldown == 0)
		{
			if (!this.level.isClientSide)
			{
				this.setIgnited(true);
			}
			return true;
		}
		return false;
	}
	
	
	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {

		if (player.getUUID().equals(getOwnerUUID())) {
			return true;
		}
		return false;
	}

	@Override
	public boolean onInteractionShift(Player player, InteractionHand hand) 
	{
		if (player.getUUID().equals(getOwnerUUID())) {
			return true;
		}
		return false;
	}

	// Interaction end

	// Inventory related
	// Generally no need to modify unless noted

	protected AdditionalInventory additionalInventory = new AdditionalInventoryWithEquipment(getInventorySize());

	@Override
	public AdditionalInventory getAdditionalInventory()
	{
		return additionalInventory;
	}

	@Override
	public int getInventorySize()
	{
		/* Change to your size */
		return 8;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			/* If mob's properties (e.g. equipment, HP, etc.) needs to sync with inventory, set here */
		}
	}

	@Override
	public void setInventoryFromMob() {
		if (!this.level.isClientSide) {
			/* If inventory needs to be set from mob's properties on initialization, set here */
		}
	}

	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container) {
		return null; /* return new YourMenuClass(containerId, playerInventory, container, this) */
	}

	// Inventory end

	// save&load

	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //

	// ------------------ IBefriendedMob interface ------------------ //

	protected boolean initialized = false;

	@Override
	public boolean hasInit()
	{
		return initialized;
	}

	@Override
	public void setInit()
	{
		initialized = true;
	}

	@Override
	public UUID getOwnerUUID() {
		return entityData.get(DATA_OWNERUUID).orElse(null);
	}

	@Override
	public void setOwnerUUID(UUID ownerUUID) {
		entityData.set(DATA_OWNERUUID, Optional.of(ownerUUID));
	}

	// AI related

	@Override
	public BefriendedAIState getAIState() {
		return BefriendedAIState.fromID(entityData.get(DATA_AISTATE));
	}

	@Override
	public void setAIState(BefriendedAIState state) {
		entityData.set(DATA_AISTATE, state.id());
	}

	protected LivingEntity PreviousTarget = null;

	@Override
	public LivingEntity getPreviousTarget() {
		return PreviousTarget;
	}

	@Override
	public void setPreviousTarget(LivingEntity target) {
		PreviousTarget = target;
	}

	/* Inventory */

	// ------------------ IBefriendedMob interface end ------------------ //

	// ------------------ Misc ------------------ //

	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	/* add @Override annotation if inheriting Monster class */
	/* @Override */
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

}
