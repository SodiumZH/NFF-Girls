package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModSoundEvents;
import com.github.mechalopa.hmag.world.entity.JiangshiEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithHandItems;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedLeapAtOwnerGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedLeapAtTargetGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedSunSensitiveMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.inventory.InventoryMenuJiangshi;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.ReflectHelper;

public class HmagJiangshiEntity extends JiangshiEntity implements IDwmgBefriendedSunSensitiveMob
{

	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(HmagJiangshiEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(HmagJiangshiEntity.class, EntityDataSerializers.INT);

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

	public HmagJiangshiEntity(EntityType<? extends HmagJiangshiEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	@Override
	public void onInit(UUID playerUUID, Mob from)
	{
		if (from instanceof JiangshiEntity js)
		{
			this.setVariant(js.getVariant());
		}
	}
	
	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(2, new HmagJiangshiEntity.LeapAtTargetGoal(this));
		goalSelector.addGoal(3, new BefriendedMeleeAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(4, new HmagJiangshiEntity.LeapAtOwnerGoal(this));
		goalSelector.addGoal(5, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));
	}

	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HealingItemTable getHealingItems()
	{
		return DwmgHealingItems.UNDEAD;
	}

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
					/* if (....)
					 {
					 	....
					 }
					else */if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					// The function above returns PASS when the items are not correct. So when not PASS it should stop here
					else if (hand == InteractionHand.MAIN_HAND
							&& DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}
					// Here it's main hand but no interaction. Return pass to enable off hand interaction.
					else return InteractionResult.PASS;
					// Interacted
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
				else return InteractionResult.PASS;
				
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
	protected BefriendedInventoryWithHandItems additionalInventory = new BefriendedInventoryWithHandItems(getInventorySize(), this);

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	@Override
	public int getInventorySize()
	{
		// 0, 1-main/offhand (not added); 2-talisman(controlling, not added) 3-6 baubles
		return 7;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuJiangshi(containerId, playerInventory, container, this);
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
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		return ContainerHelper.mapOf(
				MapPair.of("0", this.getAdditionalInventory().getItem(3)),
				MapPair.of("1", this.getAdditionalInventory().getItem(4)),
				MapPair.of("2", this.getAdditionalInventory().getItem(5)),
				MapPair.of("3", this.getAdditionalInventory().getItem(6)));
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}*/
	
	// Misc
	
	// TODO: Removing the talisman on its head will make it mad and attack everything
	public boolean isMad()
	{
		return false;
	}
	
	public void onThunderHit()
	{
		EntityHelper.addEffectSafe(this, new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60 * 20, 2));
		EntityHelper.addEffectSafe(this, new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60 * 20, 2));
		EntityHelper.addEffectSafe(this, new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60 * 20, 1));
		EntityHelper.addEffectSafe(this, new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 60 * 20));
		this.getLevelHandler().addExp(20);
	}
	
	/*@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((IDwmgBefriendedMob)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((IDwmgBefriendedMob)mob).hasDwmgBauble("resistance_amulet"));
	}*/
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return Dwmg.MOD_ID;
	}
	
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.generalAmbient(super.getAmbientSound());
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

	
	
	// =============== AI ================== //
	
	protected static class LeapAtTargetGoal extends BefriendedLeapAtTargetGoal
	{

		protected HmagJiangshiEntity jiangshi;
		
		public LeapAtTargetGoal(HmagJiangshiEntity mob)
		{
			super(mob, 0.4F, 0.2F, 8.0F, 12);
			this.mob = mob;
			jiangshi = (HmagJiangshiEntity)mob;
		}

		@Override
		public boolean checkCanUse()
		{
			return super.canUse() && this.mob.asMob().hasLineOfSight(this.mob.asMob().getTarget());
		}

		@Override
		public void onStart()
		{
			super.onStart();
			this.mob.asMob().playSound(ModSoundEvents.JIANGSHI_JUMP.get(), 0.8F, 1.0F);
		}

		@Override
		public float getMaxAttackDistance()
		{
			return super.getMaxAttackDistance() - 3.0F * ((float)jiangshi.getSpeedBonus() / (float)JiangshiEntity.SPEED_BONUS_MAX);
		}

		@Override
		public double getXZSpeed()
		{
			return super.getXZSpeed() + 0.3D * ((double)jiangshi.getSpeedBonus() / (double)JiangshiEntity.SPEED_BONUS_MAX);
		}
	}
	
	protected static class LeapAtOwnerGoal extends BefriendedLeapAtOwnerGoal
	{

		protected HmagJiangshiEntity jiangshi;
		
		public LeapAtOwnerGoal(HmagJiangshiEntity mob)
		{
			super(mob, 0.4F, 0.2F, 8.0F, 12);
			this.mob = mob;
			jiangshi = (HmagJiangshiEntity)mob;
		}

		@Override
		public boolean checkCanUse()
		{
			return super.checkCanUse() && this.mob.asMob().getTarget() != null && this.mob.asMob().hasLineOfSight(this.mob.asMob().getTarget());
		}

		@Override
		public void onStart()
		{
			super.onStart();
			this.mob.asMob().playSound(ModSoundEvents.JIANGSHI_JUMP.get(), 0.8F, 1.0F);
		}

		@Override
		public float getMaxAttackDistance()
		{
			return super.getMaxAttackDistance() - 3.0F * ((float)jiangshi.getSpeedBonus() / (float)JiangshiEntity.SPEED_BONUS_MAX);
		}

		@Override
		public double getXZSpeed()
		{
			return super.getXZSpeed() + 0.3D * ((double)jiangshi.getSpeedBonus() / (double)JiangshiEntity.SPEED_BONUS_MAX);
		}
	}
	
}

