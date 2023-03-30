package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.ModConfigs;
import com.github.mechalopa.hmag.world.entity.EnderExecutorEntity;
import com.github.mechalopa.hmag.world.entity.IBeamAttackMob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumstudio.dwmg.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.BefriendedNearestAttackableTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.vanillapreset.enderman.AbstractBefriendedEnderMan;
import net.sodiumstudio.dwmg.befriendmobs.entity.vanillapreset.enderman.BefriendedEnderManGoals;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedSunAvoidingFollowOwnerGoal;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuEnderExecutor;

// Adjusted from EnderExcutor in HMaG
public class EntityBefriendedEnderExecutor extends AbstractBefriendedEnderMan implements IBefriendedMob, IBeamAttackMob
{

	protected static final EntityDataAccessor<Integer> ATTACKING_TIME = SynchedEntityData.defineId(EntityBefriendedEnderExecutor.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Integer> ATTACK_TARGET = SynchedEntityData.defineId(EntityBefriendedEnderExecutor.class, EntityDataSerializers.INT);
	protected LivingEntity targetedEntity;
	protected int clientAttackTime;
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ATTACKING_TIME, -20);
		entityData.define(ATTACK_TARGET, 0);
	}
	
	public EntityBefriendedEnderExecutor(EntityType<? extends EntityBefriendedEnderExecutor> type, Level worldIn)
	{
		super(type, worldIn);
		this.xpReward = 0;
		this.additionalInventory = new AdditionalInventory(getInventorySize());
	}

	
	public static Builder createAttributes() {
		return EnderExecutorEntity.createAttributes();
	}
	
	@Override
	protected void registerGoals() {
	      this.goalSelector.addGoal(1, new FloatGoal(this));
	      //this.goalSelector.addGoal(1, new BefriendedEnderManGoals.FreezeWhenLookedAt(this));     
	      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
	      this.goalSelector.addGoal(4, new BefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
	      this.goalSelector.addGoal(7, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
	      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
	      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	      this.goalSelector.addGoal(10, new BefriendedEnderManGoals.LeaveBlockGoal(this));
	      this.goalSelector.addGoal(11, new BefriendedEnderManGoals.TakeBlockGoal(this));
	      this.targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
	      this.targetSelector.addGoal(2, new BefriendedNearestAttackableTargetGoal<Endermite>(this, Endermite.class, true, false).allowAllStates());
	      this.targetSelector.addGoal(3, new BefriendedHurtByTargetGoal(this));
	      this.targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
	}

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

		if (player.getUUID().equals(getOwnerUUID())) {
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
	public boolean onInteractionShift(Player player, InteractionHand hand) 
	{
		if (player.getUUID().equals(getOwnerUUID())) {
	
			if (hand.equals(InteractionHand.MAIN_HAND))
				BefriendedHelper.openBefriendedInventory(player, this);
			return true;
		}
		/* Other actions... */
		return false;
	}

	// Interaction end

	// No armor, hand items(0, 1), holding block(2) and 2 baubles(3, 4)
	@Override
	public int getInventorySize()
	{
		return 5;
	}

	@Override
	public void updateFromInventory() {
		super.updateFromInventory();
		if (!this.level.isClientSide) {
			setItemSlot(EquipmentSlot.MAINHAND, getAdditionalInventory().getItem(0));
			setItemSlot(EquipmentSlot.OFFHAND, getAdditionalInventory().getItem(1));
			
			if (getAdditionalInventory().getItem(2).isEmpty())
			{
				this.setCarriedBlock((BlockState) null);
			}			
			else if (getAdditionalInventory().getItem(2).getItem() instanceof BlockItem bi)
			{
				this.setCarriedBlock(bi.getBlock().defaultBlockState());
			}
			else throw new IllegalStateException("Ender Executor can only carry block items. Attempt to carry: " + getAdditionalInventory().getItem(2).getItem().getRegistryName());
			setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY);
			setItemSlot(EquipmentSlot.CHEST, ItemStack.EMPTY);
			setItemSlot(EquipmentSlot.LEGS, ItemStack.EMPTY);
			setItemSlot(EquipmentSlot.FEET, ItemStack.EMPTY);
		}
		
	}

	@Override
	public void setInventoryFromMob() {

		super.setInventoryFromMob();
		if (!this.level.isClientSide) {
			getAdditionalInventory().setItem(0, getItemBySlot(EquipmentSlot.MAINHAND));
			getAdditionalInventory().setItem(1, getItemBySlot(EquipmentSlot.OFFHAND));
			if (getCarriedBlock() != null && !getCarriedBlock().getBlock().equals(Blocks.AIR))
				getAdditionalInventory().setItem(2, new ItemStack(getCarriedBlock().getBlock().asItem()));
			else getAdditionalInventory().setItem(2, ItemStack.EMPTY);
		}
	}

	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container)
	{
		return new InventoryMenuEnderExecutor(containerId, playerInventory, container, this);
	}
	
	// Inventory end

	// AI
	
	public boolean doBeamAttack = true;
	
	public void enderExecutorAiStep()
	{
		if (!this.level.isClientSide)
		{
			if (this.isAlive() && !this.isNoAi() && this.level.getDifficulty().getId() > 1 && ModConfigs.cachedServer.ENDER_EXECUTOR_BEAM_ATTACK /* Added */ && doBeamAttack)
			{
				LivingEntity target = this.getTarget();

				if (target != null && target.isAlive())
				{
					double d0 = this.distanceToSqr(target);

					if (this.hasLineOfSight(target) && d0 > 1.0D * 1.0D && d0 <= 24.0D * 24.0D)
					{
						int i = this.getAttackingTime();
						++i;

						if (i == 0)
						{
							this.setAttackingTime(i);
							this.setActiveAttackTarget(target.getId());
						}
						else if (i >= this.getAttackDuration())
						{
							if (this.getActiveAttackTarget() != null)
							{
								if (this.attackEntityWithBeamAttack(this.getActiveAttackTarget(), 6.0F) && this.random.nextInt(10) == 0)
								{
									this.teleport();
								}
							}

							this.setAttackingTime(-(10 + this.random.nextInt(6)));
							this.setActiveAttackTarget(0);
						}
						else
						{
							this.setAttackingTime(i);
						}
					}
					else
					{
						this.setAttackingTime(-20);
						this.setActiveAttackTarget(0);
					}
				}
				else
				{
					this.setAttackingTime(-20);
				}
			}
		}
		else
		{
			if (this.isAlive() && this.hasActiveAttackTarget())
			{
				if (this.clientAttackTime < this.getAttackDuration())
				{
					++this.clientAttackTime;
				}

				LivingEntity target = this.getActiveAttackTarget();

				if (target != null)
				{
					this.getLookControl().setLookAt(target, 90.0F, 90.0F);
					this.getLookControl().tick();
				}
			}
			else
			{
				this.clientAttackTime = 0;
			}
		}
	}

	// Adjusted
	public boolean hurt(DamageSource source, float amount)
	{
		{
			if (this.isInvulnerableTo(source))
			{
				return false;
			}
			else
			{
				float f = amount;

				if (ModConfigs.cachedServer.ENDER_EXECUTOR_REDUCE_DAMAGE)
				{
					if (!(source.getEntity() != null && source.isCreativePlayer()) && source != DamageSource.OUT_OF_WORLD && f > 10.0F)
					{
						
							f = 10.0F + (f - 10.0F) * 0.1F;
						
					}
				}

				if (source instanceof IndirectEntityDamageSource)
				{
					for (int i = 0; i < 64; ++i)
					{
						if (this.teleport())
						{
							return true;
						}
					}

					return false;
				}
				else
				{
					return super.hurt(source, f);
				}
			}
		}
	}
	
	public void aiStep()
	{
		enderExecutorAiStep();
		super.aiStep();

	}
	
	@Override
	public void setTarget(@Nullable LivingEntity livingEntity)
	{
		if (livingEntity == null)
		{
			this.setActiveAttackTarget(0);
		}

		super.setTarget(livingEntity);
	}
	public int getAttackingTime()
	{
		return this.entityData.get(ATTACKING_TIME);
	}

	private void setAttackingTime(int value)
	{
		this.entityData.set(ATTACKING_TIME, value);
	}

	@Override
	public boolean randomTeleport(double x, double y, double z, boolean flag)
	{
		if (super.randomTeleport(x, y, z, flag))
		{
			if (this.hasActiveAttackTarget())
			{
				this.setActiveAttackTarget(0);
			}

			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public void onSyncedDataUpdated(EntityDataAccessor<?> key)
	{
		super.onSyncedDataUpdated(key);

		if (ATTACK_TARGET.equals(key))
		{
			this.clientAttackTime = 0;
			this.targetedEntity = null;
		}
	}

	@Override
	public int getAttackDuration()
	{
		return 40;
	}

	@Override
	public boolean attackEntityWithBeamAttack(LivingEntity target, float damage)
	{
		if (!this.isSilent())
		{
			this.level.playSound((Player)null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENCHANTMENT_TABLE_USE, this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.2F + 0.9F);
		}

		float f = damage;
			f += 2.0F;
			
		return target.hurt(DamageSource.indirectMagic(this, this), f);
	}

	@Override
	public void setActiveAttackTarget(int entityId)
	{
		this.entityData.set(ATTACK_TARGET, entityId);
	}

	@Override
	public boolean hasActiveAttackTarget()
	{
		return entityData.get(ATTACK_TARGET).intValue() != 0;
	}

	@Nullable
	@Override
	public LivingEntity getActiveAttackTarget()
	{
		if (!this.hasActiveAttackTarget())
		{
			return null;
		}
		else if (this.level.isClientSide)
		{
			if (this.targetedEntity != null)
			{
				return this.targetedEntity;
			}
			else
			{
				Entity entity = this.level.getEntity(this.entityData.get(ATTACK_TARGET));

				if (entity instanceof LivingEntity)
				{
					this.targetedEntity = (LivingEntity)entity;
					return this.targetedEntity;
				}
				else
				{
					return null;
				}
			}
		}
		else
		{
			return this.getTarget();
		}
	}

	@Override
	public float getAttackAnimationScale(float f)
	{
		return (this.clientAttackTime + f) / this.getAttackDuration();
	}

	@Override
	public boolean teleport(double pX, double pY, double pZ)
	{
		// Don't teleport over 100 blocks away from owner
		if (getOwner() != null && getOwner().distanceToSqr(pX, pY, pZ) > 10000.d)
			return false;
		else return super.teleport(pX, pY, pZ);
	}
	
	// save&load

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		/* Add more save data... */
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		/* Add more save data... */
		this.setInit();
	}

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
	public Player getOwner() {
		return getOwnerUUID() != null ? level.getPlayerByUUID(getOwnerUUID()) : null;
	}

	@Override
	public void setOwner(Player owner) {
		entityData.set(DATA_OWNERUUID, Optional.of(owner.getUUID()));
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
