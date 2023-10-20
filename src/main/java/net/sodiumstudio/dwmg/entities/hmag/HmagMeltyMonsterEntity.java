package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.MeltyMonsterEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.EntityHelper;

public class HmagMeltyMonsterEntity extends MeltyMonsterEntity implements IDwmgBefriendedMob {

	protected static final UUID MODIFIER_OWNER_SPEED_UP_IN_LAVA_UUID = UUID.fromString("17e617b0-964e-42b3-86a8-c264de6e19d8");
	protected static final UUID MODIFIER_SELF_SPEED_UP_IN_LAVA_UUID = UUID.fromString("2f2f7447-a39f-435c-a21a-29ea47298f3e");
	protected static final UUID MODIFIER_SELF_SPEED_UP_ON_GROUND_UUID = UUID.fromString("6bf67d57-5068-4f28-b433-a55fc232d679");
	/** Handled in {@link DwmgEntityEvents} */
	public static final AttributeModifier MODIFIER_OWNER_SPEED_UP_IN_LAVA = new AttributeModifier(MODIFIER_OWNER_SPEED_UP_IN_LAVA_UUID,
			"speed_up_in_lava", 4d, AttributeModifier.Operation.ADDITION);
	public static final AttributeModifier MODIFIER_SELF_SPEED_UP_IN_LAVA = new AttributeModifier(MODIFIER_SELF_SPEED_UP_IN_LAVA_UUID,
			"speed_up_in_lava", 4d, AttributeModifier.Operation.ADDITION);
	public static final AttributeModifier MODIFIER_SELF_SPEED_UP_ON_GROUND = new AttributeModifier(MODIFIER_SELF_SPEED_UP_ON_GROUND_UUID,
			"speed_up_in_lava", 4d, AttributeModifier.Operation.ADDITION);
	
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
	
	/* Behavior */

	@Override
	protected void registerGoals() {
		// Add goals here
		// Generally target goals can be preset below. Change if it needs to modify.
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
	}
	
	@Override
	protected void customServerAiStep()
	{
		if (this.isInLava() && this.isOwnerPresent() && this.getOwner().isInLava() && this.distanceToSqr(this.getOwner()) <= 64d)
		{
			EntityHelper.addEffectSafe(this.getOwner(), MobEffects.FIRE_RESISTANCE, 19);
			
		}
	}
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HashMap<Item, Float> getHealingItems()
	{
		return ContainerHelper.mapOf(
				// MapPair.of({item}, {healing_amount})
				);
		
	}
	
	// Set of items that can heal the mob WITHOUT CONSUMING.
	// Leave it empty if not needed.
	@Override
	public HashSet<Item> getNonconsumingHealingItems()
	{
		return ContainerHelper.setOf(
				// items....
				);
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
	protected BefriendedInventoryWithEquipment additionalInventory = new BefriendedInventoryWithEquipment(getInventorySize(), this);

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	@Override
	public int getInventorySize()
	{
		return 8;
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
		return null; // new YourInventoryMenuClass(containerId, playerInventory, container, this);
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
		/* Set here */
		return null;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		/* Set here */
		return null;
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

}
