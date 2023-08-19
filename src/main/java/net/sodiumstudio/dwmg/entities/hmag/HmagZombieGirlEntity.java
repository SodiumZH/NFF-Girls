package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedZombieAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRestrictSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.befriendmobs.registry.BMItems;
import net.sodiumstudio.nautils.ItemHelper;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.target.BefriendedNearestUnfriendlyMobTargetGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.inventory.InventoryMenuEquipmentTwoBaubles;
import net.sodiumstudio.dwmg.registries.DwmgEntityTypes;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;

public class HmagZombieGirlEntity extends ZombieGirlEntity implements IDwmgBefriendedMob, IBefriendedUndeadMob {

	/* Initialization */

	public HmagZombieGirlEntity(EntityType<? extends HmagZombieGirlEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);

	}

	public static Builder createAttributes() {
		return ZombieGirlEntity.createAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ARMOR, 4.0D);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(4, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
				.avoidSunCondition(DwmgEntityHelper::isSunSensitive));
		goalSelector.addGoal(5, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new DwmgBefriendedOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));
	}

	
	/* Combat */
	
	@Override
	public boolean doHurtTarget(Entity target)
	{
		// Occupy the main hand to block the ignition action in super.doHurtTarget
		// See Zombie class
		if (this.getMainHandItem().isEmpty())
			this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(BMItems.DUMMY_ITEM.get(), 1));
		boolean res = super.doHurtTarget(target);
		// Remove dummy item
		if (!this.getMainHandItem().isEmpty() && this.getMainHandItem().is(BMItems.DUMMY_ITEM.get()))
			this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);		
		// Overwrite ignition here
		if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < 0.8f)
			target.setSecondsOnFire(10);

		return res;
	}

	/* Interaction */

	@Override
	public HashMap<Item, Float> getHealingItems()
	{
		HashMap<Item, Float> map = new HashMap<Item, Float>();
		map.put(ModItems.SOUL_POWDER.get(), 5.0f);
		map.put(ModItems.SOUL_APPLE.get(), 15.0f);
		return map;
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (!player.isShiftKeyDown())
		{
			if (player.getUUID().equals(getOwnerUUID())) {
				if (!player.level.isClientSide() && hand == InteractionHand.MAIN_HAND) 
				{
					if (player.getItemInHand(hand).is(Items.SPONGE) && isFromHusk) {
						player.getItemInHand(hand).shrink(1);
						this.spawnAtLocation(new ItemStack(Items.WET_SPONGE, 1));
						this.convertToHusk();
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					} 
					else if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS) 
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
			additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuEquipmentTwoBaubles(containerId, playerInventory, container, this);
	}

	// Fix an unknown bug that mob spawned from 
	@Override
	protected void populateDefaultEquipmentSlots(DifficultyInstance pDifficulty) {}
	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		nbt.put("is_from_husk", ByteTag.valueOf(isFromHusk));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		isFromHusk = nbt.getBoolean("is_from_husk");
		setInit();
	}

	/* Convertions */
		
	@Override
	protected boolean convertsInWater()
	{
		return true;
	}
	
	public boolean isFromHusk = false;	
	
	@Override
	protected void doUnderWaterConversion() {
		this.convertToDrowned();
		if (!this.isSilent())
		{
			this.level.levelEvent((Player) null, 1041, this.blockPosition(), 0);
		}
	}	
	
	public void forceUnderWaterConversion()
	{
		this.doUnderWaterConversion();
	}
	
	public HmagHuskGirlEntity convertToHusk()
	{
		HmagHuskGirlEntity newMob = (HmagHuskGirlEntity)BefriendedHelper.convertToOtherBefriendedType(this, DwmgEntityTypes.HMAG_HUSK_GIRL.get());
		newMob.setInit();
		return newMob;
	}
	
	public HmagDrownedGirlEntity convertToDrowned()
	{
		HmagDrownedGirlEntity newMob = (HmagDrownedGirlEntity)BefriendedHelper.convertToOtherBefriendedType(this, DwmgEntityTypes.HMAG_DROWNED_GIRL.get());
		newMob.isFromHusk = this.isFromHusk;
		newMob.isFromZombie = true;
		newMob.setInit();
		return newMob;
	}


	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(HmagZombieGirlEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(HmagZombieGirlEntity.class, EntityDataSerializers.INT);

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

	/* IBefriendedUndeadMob interface */

	@Override
	public void setupSunImmunityRules() {
		this.sunImmuneConditions().put("sunhat", () -> this.getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.SUNHAT.get()));
		this.sunImmuneConditions().put("soul_amulet", () -> this.hasDwmgBauble("soul_amulet"));
		this.sunImmuneConditions().put("resis_amulet", () -> this.hasDwmgBauble("resistance_amulet"));
	}

	@Override
	protected boolean isSunSensitive()
	{
		return !this.isSunImmune();
	}	

	/* IBaubleHolder interface */

	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(6));
		map.put("1", this.getAdditionalInventory().getItem(7));
		return map;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}

	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //

	// ------------------ IBefriendedMob interface ------------------ //


	
	// ------------------ IBefriendedMob interface end ------------------ //
	
	// ------------------ Misc ------------------ //
	
	@Override
	public String getModId() {
		return Dwmg.MOD_ID;
	}
	
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
