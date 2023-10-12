package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.DullahanEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedSunSensitiveMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedZombieAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRestrictSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithHandItems;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.registry.BMItems;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.inventory.InventoryMenuHandItemsFourBaubles;
import net.sodiumstudio.dwmg.inventory.InventoryMenuHandItemsFourBaublesDefault;
import net.sodiumstudio.dwmg.registries.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.registries.DwmgMiscReg;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.containers.MapPair;

/**
 * This is a template with more preset
 */
public class HmagDullahanEntity extends DullahanEntity implements IDwmgBefriendedMob, IBefriendedSunSensitiveMob
{

	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(HmagDullahanEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(HmagDullahanEntity.class, EntityDataSerializers.INT);

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

	public HmagDullahanEntity(EntityType<? extends HmagDullahanEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(2, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(3, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(4, new BefriendedMeleeAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(5, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
				.avoidSunCondition(DwmgEntityHelper::isSunSensitive));
		goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new DwmgBefriendedOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));
	}
	
	@Override
	public void aiStep() {
		if (!this.level().isClientSide)
			DwmgEntityHelper.setMobEquipmentWithoutSideEffect(this, EquipmentSlot.HEAD, this.isSunImmune() ? BMItems.DUMMY_ITEM.get().getDefaultInstance() : ItemStack.EMPTY);
		super.aiStep();
		if (!this.level().isClientSide)
			DwmgEntityHelper.setMobEquipmentWithoutSideEffect(this, EquipmentSlot.HEAD, ItemStack.EMPTY);
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HashMap<Item, Float> getHealingItems()
	{
		return DwmgMiscReg.HEALING_ITEMS_UNDEAD;
	}
	
	// Set of items that can heal the mob WITHOUT CONSUMING.
	// Leave it empty if not needed.
	@Override
	public HashSet<Item> getNonconsumingHealingItems()
	{
		HashSet<Item> set = new HashSet<Item>();
		// set.add(YOUR_ITEM_TYPE);
		return set;
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (player.getUUID().equals(getOwnerUUID())) {
			// For normal interaction
			if (!player.isShiftKeyDown())
			{
				if (!player.level().isClientSide()) 
				{
					/* Put checks before healing item check */
					/* if (....)
					 {
					 	....
					 }
					else */if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level().isClientSide);
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
				return InteractionResult.sidedSuccess(player.level().isClientSide);
			}
			// For interaction with shift key down
			else
			{
				// Open inventory and GUI
				if (hand == InteractionHand.MAIN_HAND && DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
				{
					BefriendedHelper.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level().isClientSide);
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
		// Hand items + 4 baubles
		return 6;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level().isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level().isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuHandItemsFourBaublesDefault(containerId, playerInventory, container, this);
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
		return ContainerHelper.mapOf(
				MapPair.of("0", getAdditionalInventory().getItem(2)),
				MapPair.of("1", getAdditionalInventory().getItem(3)),
				MapPair.of("2", getAdditionalInventory().getItem(4)),
				MapPair.of("3", getAdditionalInventory().getItem(5)));
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}
	
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.generalAmbient(super.getAmbientSound());
	}
	
	// Misc
	
	@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((IDwmgBefriendedMob)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((IDwmgBefriendedMob)mob).hasDwmgBauble("resistance_amulet"));
	}
	
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

}
