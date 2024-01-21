package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.SkeletonGirlEntity;

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
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedSunSensitiveMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRestrictSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleEquipable;
import net.sodiumstudio.befriendmobs.registry.BMItems;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.target.BefriendedNearestUnfriendlyMobTargetGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedSunSensitiveMob;
import net.sodiumstudio.dwmg.entities.ai.goals.BefriendedSkeletonMeleeAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.BefriendedSkeletonRangedBowAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoalLegacy;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoalLegacy;
import net.sodiumstudio.dwmg.inventory.InventoryMenuSkeleton;
import net.sodiumstudio.dwmg.registries.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.registries.DwmgConfigs;
import net.sodiumstudio.dwmg.registries.DwmgEntityTypes;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;

public class HmagSkeletonGirlEntity extends SkeletonGirlEntity implements IDwmgBefriendedSunSensitiveMob
{

	
	public HmagSkeletonGirlEntity(EntityType<? extends HmagSkeletonGirlEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	@Deprecated
	public static Builder createAttributes() 
	{
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 3.25D).add(Attributes.ARMOR, 1.0D);
	}
	
	// ------------------ Data sync ------------------ //

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(HmagSkeletonGirlEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(HmagSkeletonGirlEntity.class, EntityDataSerializers.INT);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_OWNERUUID, Optional.empty());
		entityData.define(DATA_AISTATE, 1);
	}

	@Override
	public EntityDataAccessor<Optional<UUID>> getOwnerUUIDAccessor() {
		return DATA_OWNERUUID;
	}

	@Override
	public EntityDataAccessor<Integer> getAIStateData() {
		return DATA_AISTATE;
	}
	
	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedSkeletonRangedBowAttackGoal(this, 1.0D, 20, 15.0F));
		goalSelector.addGoal(4, new BefriendedSkeletonMeleeAttackGoal(this, 1.2d, true));
		goalSelector.addGoal(5, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
				.avoidSunCondition(DwmgEntityHelper::isSunSensitive));
		goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new DwmgBefriendedOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoalLegacy(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoalLegacy(this));
		
	}
	
	/* Bow shooting related */
	
	private boolean justShot = false;

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		
		// Filter again to avoid it shoot without arrow
		if (this.getAdditionalInventory().getItem(8).isEmpty())
			return;
		
		// Copied from vanilla skeleton, removed difficulty factor
		ItemStack itemstack = this.getProjectile(this.getItemInHand(
				ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
		AbstractArrow abstractarrow = this.getArrow(itemstack, pVelocity);
		if (this.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem)
			abstractarrow = ((net.minecraft.world.item.BowItem) this.getMainHandItem().getItem())
					.customArrow(abstractarrow);
		double d0 = pTarget.getX() - this.getX();
		double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
		double d2 = pTarget.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		abstractarrow.setBaseDamage(abstractarrow.getBaseDamage() * this.getAttributeValue(Attributes.ATTACK_DAMAGE) / this.getAttributeBaseValue(Attributes.ATTACK_DAMAGE));
		boolean canPickUp = this.getAdditionalInventory().getItem(4).getEnchantmentLevel(Enchantments.INFINITY_ARROWS) <= 0;
		abstractarrow.pickup = canPickUp ? AbstractArrow.Pickup.ALLOWED : AbstractArrow.Pickup.DISALLOWED;
		abstractarrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, 2.0F);
		this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.level.addFreshEntity(abstractarrow);
		
		justShot = true;
	}

	@Override
	public void aiStep() {
		super.aiStep();
		/* Handle combat AI */
		if (!this.level.isClientSide)
		{
			if (justShot)
			{
				if (this.getAdditionalInventory().getItem(4).getEnchantmentLevel(Enchantments.INFINITY_ARROWS) <= 0)
					this.getAdditionalInventory().consumeItem(8);
				justShot = false;
			}
			
			if (this.getTarget() != null) {
				// When too close, switch to melee mode if possible
				if (this.distanceTo(this.getTarget()) < 2.5) {
					if (additionalInventory.getItem(4).is(Items.BOW) && additionalInventory.getItem(7).getItem() instanceof TieredItem) {
						additionalInventory.swapItem(4, 7);
						updateFromInventory();
					}
				}
				// When run out arrows, try taking weapon from backup-weapon slot
				if (additionalInventory.getItem(4).is(Items.BOW) && additionalInventory.getItem(7).getItem() instanceof TieredItem
						&& additionalInventory.getItem(8).isEmpty()) {
					additionalInventory.swapItem(4, 7);
					updateFromInventory();
				}
				// When too far and having a bow on backup-weapon, switch to bow mode
				// Don't switch if don't have arrows
				else if (this.distanceTo(this.getTarget()) > 4) {
					if (!additionalInventory.getItem(4).is(Items.BOW) && getAdditionalInventory().getItem(7).is(Items.BOW)
							&& !additionalInventory.getItem(8).isEmpty()) {
						additionalInventory.swapItem(4, 7);
						updateFromInventory();
					}
				}
				// When in melee mode without a weapon but having one on backup slot, change to it
				else if (!this.getInventoryItemStack(4).is(Items.BOW)
						&& !this.getInventoryItemStack(7).is(Items.BOW)
						&& (this.getInventoryItemStack(4).isEmpty() || !(this.getInventoryItem(4) instanceof TieredItem))
						&& !this.getInventoryItemStack(7).isEmpty()
						&& (this.getInventoryItem(7) instanceof TieredItem)
						)
				{
					additionalInventory.swapItem(4, 7);
					updateFromInventory();
				}
			}
		}
	}
	
	// It's not needed here
	@Override
	public void reassessWeaponGoal() 
	{}
	
	/* Bow shooting end */


	/* Interaction */

	@Override
	public HealingItemTable getHealingItems()
	{
		return DwmgHealingItems.UNDEAD;
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
			return InteractionResult.PASS;
		}
	}

	/* Inventory */


	protected BefriendedInventoryWithEquipment additionalInventory = new BefriendedInventoryWithEquipment(getInventorySize());

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	// 6->bauble, 7->backup weapon 8->arrow
	@Override
	public int getInventorySize()
	{
		return 9;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob() {
		if (!this.level.isClientSide) {
			additionalInventory.getFromMob(this);
		}
	}
	
	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuSkeleton(containerId, playerInventory, container, this);
	}
	
	/* Save and Load */

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		setInit();
	}
	
	/* Conversion */
	
	@Override
	public boolean isFreezeConverting()
	{
		return super.isFreezeConverting() && DwmgConfigs.ValueCache.Interaction.ALLOW_VANILLA_CONVERSION;
	}
	
	@Override
	protected void doFreezeConversion() {
		this.convertToStray();
		if (!this.isSilent())
		{
			this.level.levelEvent((Player) null, 1041, this.blockPosition(), 0);
		}
	}	
	
	public void forceFreezeConversion()
	{
		this.doFreezeConversion();
	}
	
	// Called when convert to stray
	protected HmagStrayGirlEntity convertToStray()
	{
		HmagStrayGirlEntity newMob = (HmagStrayGirlEntity)BefriendedHelper.convertToOtherBefriendedType(this, DwmgEntityTypes.HMAG_STRAY_GIRL.get());
		newMob.isFromSkeleton = true;
		newMob.setInit();
		return newMob;
	}

	/* IBefriendedSunSensitiveMob interface */

	// Implementation is in aiStep()
	@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("sunhat", mob -> mob.getMob().getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.SUNHAT.get()));
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((IDwmgBefriendedMob)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((IDwmgBefriendedMob)mob).hasDwmgBauble("resistance_amulet"));
	}
	
	
	/* IBaubleEquipable interface */
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(6));
		return map;
	}
	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}
*/
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.skeletonAmbient(super.getAmbientSound());
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return DwmgSoundPresets.skeletonHurt(super.getHurtSound(damageSource));
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return DwmgSoundPresets.skeletonDeath(super.getDeathSound());
	}	
	
	// ------------------ Misc ------------------ //
	
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
	
	
	
}
