package net.sodiumstudio.dwmg.entities.hmag;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.ModConfigs;
import com.github.mechalopa.hmag.world.entity.IBeamAttackMob;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedNearestAttackableTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.befriendmobs.entity.vanillapreset.enderman.AbstractBefriendedEnderMan;
import net.sodiumstudio.befriendmobs.entity.vanillapreset.enderman.BefriendedEnderManGoals;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.ICarriesBlock;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.inventory.EnderExecutorInventory;
import net.sodiumstudio.dwmg.inventory.InventoryMenuEnderExecutor;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;

// Adjusted from EnderExcutor in HMaG
public class HmagEnderExecutorEntity extends AbstractBefriendedEnderMan implements IBeamAttackMob, IDwmgBefriendedMob, ICarriesBlock
{

	protected static final EntityDataAccessor<Integer> ATTACKING_TIME = SynchedEntityData.defineId(HmagEnderExecutorEntity.class, EntityDataSerializers.INT);
	protected static final EntityDataAccessor<Integer> ATTACK_TARGET = SynchedEntityData.defineId(HmagEnderExecutorEntity.class, EntityDataSerializers.INT);
	protected LivingEntity targetedEntity;
	protected int clientAttackTime;
	public boolean reduceDamage = true;
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(ATTACKING_TIME, -20);
		entityData.define(ATTACK_TARGET, 0);
	}
	
	public HmagEnderExecutorEntity(EntityType<? extends HmagEnderExecutorEntity> type, Level worldIn)
	{
		super(type, worldIn);
		this.xpReward = 0;
		this.modId = Dwmg.MOD_ID;
	}

	@Override
	protected void registerGoals() {
	      this.goalSelector.addGoal(1, new FloatGoal(this));
	      this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
	      this.goalSelector.addGoal(4, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
	      this.goalSelector.addGoal(7, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0D, 0.0F));
	      this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
	      this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
	      this.goalSelector.addGoal(10, new BefriendedEnderManGoals.LeaveBlockGoal(this));
	      this.goalSelector.addGoal(11, new BefriendedEnderManGoals.TakeBlockGoal(this));
	      this.targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this));
	      this.targetSelector.addGoal(2, new BefriendedNearestAttackableTargetGoal<Endermite>(this, Endermite.class, true, false).allowAllStates().asGoal());
	      this.targetSelector.addGoal(3, new BefriendedHurtByTargetGoal(this));
	      this.targetSelector.addGoal(4, new DwmgBefriendedOwnerHurtTargetGoal(this));
	      targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
	      targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));

	}

	// Initialization end

	// Interaction
	
	@Override
	public HealingItemTable getHealingItems()
	{
		return DwmgHealingItems.ENDERMAN;
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (!player.isShiftKeyDown())
		{
			if (player.getUUID().equals(getOwnerUUID())) {
				if (!player.level.isClientSide() && hand == InteractionHand.MAIN_HAND) 
				{
					if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
					{}
					else if (hand == InteractionHand.MAIN_HAND
							&& DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}	
					else return InteractionResult.PASS;
				}
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			return InteractionResult.PASS;
		}
		else
		{
			if (player.getUUID().equals(getOwnerUUID())) {		
				if (hand == InteractionHand.MAIN_HAND && DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
				{
					BefriendedHelper.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
			/* Other actions... */
			return InteractionResult.PASS;
		}
	}


	// Interaction end

	// No armor, hand items(0, 1), holding block(2) and 2 baubles(3, 4)

	@Override
	public BefriendedInventory createAdditionalInventory() {
		return new EnderExecutorInventory(5, this);
	}
	/*
	@Override
	public void updateFromInventory() {
		super.updateFromInventory();
		if (!this.level.isClientSide) {
			setItemSlot(EquipmentSlot.MAINHAND, getAdditionalInventory().getItem(0));
			setItemSlot(EquipmentSlot.OFFHAND, getAdditionalInventory().getItem(1));
			
			updateHoldingBlock();
			
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
*/

	@Override
	public void setCarryingBlock(BlockState newBlock) {
		this.setCarriedBlock(newBlock);
		
	}

	@Override
	public BlockState getCarryingBlock() {
		return this.getCarriedBlock();
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container)
	{
		return new InventoryMenuEnderExecutor(containerId, playerInventory, container, this);
	}
	
	protected void updateHoldingBlock()
	{
		if (getAdditionalInventory().getItem(2).isEmpty())
		{
			this.setCarriedBlock(null);
		}			
		else if (getAdditionalInventory().getItem(2).getItem() instanceof BlockItem bi)
		{
			this.setCarriedBlock(bi.getBlock().defaultBlockState());
		}
		else this.setCarriedBlock(null);
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
								if (doBeamAttack 
										&& this.attackEntityWithBeamAttack(this.getActiveAttackTarget(), 8f + 0.1f * (float)(this.getLevelHandler().getExpectedLevel())) 
										&& this.teleportNotOnHurtByWater 
										&& this.random.nextInt(10) == 0)
								{
									this.tryTeleportInOtherCases(64);
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
	@Override
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

				if (reduceDamage)
				{
					if (!(source.getEntity() != null && source.isCreativePlayer()) && source != DamageSource.OUT_OF_WORLD && f > 10.0F)
					{
						
							f = 10.0F + (f - 10.0F) * 0.1F;
						
					}
				}
/*
				if (source instanceof IndirectEntityDamageSource)
				{
					for (int i = 0; i < 64; ++i)
					{
						if (!this.teleportToAvoidProjectile && this.teleport())
						{
							return true;
						}
					}

					return false;
				}
				else*/
				
					return super.hurt(source, f);
				
			}
		}
	}
	
	@Override
	public void aiStep()
	{
		enderExecutorAiStep();
		super.aiStep();
	}
	
	@Override
	public void customServerAiStep()
	{
		updateHoldingBlock();
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
	
	public boolean teleportToOwnerInRain(int tryTimes)
	{
		if (this.isInWaterOrRain() && !this.isInWater())
		{
			for (int i = 0; i < tryTimes; ++i)
			{
				if (this.teleportTowards(this.getOwner())
					&& !this.level.canSeeSky(this.blockPosition()))
					return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean tryTeleportOnWaterHurt(int tryTimes)
	{
		if (this.teleportToOwnerInRain(tryTimes))
			return true;
		else return super.tryTeleportOnWaterHurt(tryTimes);
	}
	
	// save&load

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		/* Add more save data... */
		this.setInit();
	}
	
	/* IBaubleEquipable interface */
/*	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(3));
		map.put("1", this.getAdditionalInventory().getItem(4));
		return map;
	}
	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}*/
	
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.generalAmbient(super.getAmbientSound());
	}
	
	// ------------------ Misc ------------------ //
	/*
	@Override
	public String getModId() {
		return Dwmg.MOD_ID;
	}
		*/
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

}
