package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.Arrays;
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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedFleeSunGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedRestrictSunGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedZombieAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventoryWithEquipment;
import net.sodiumstudio.dwmg.befriendmobs.registry.RegItems;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.ItemHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.*;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.BaubleEffectTable;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuZombie;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEntityTypes;

public class EntityBefriendedZombieGirl extends ZombieGirlEntity implements IBefriendedMob {
/*
	public static final BaubleEffectTable BAUBLE_EFFECTS = new BaubleEffectTable()
			.add(Items.DIAMOND, Attributes.MAX_HEALTH, 5.0d, AttributeModifier.Operation.ADDITION);
	public static final UUID BAUBLE_EFFECT_UUID_1 = UUID.fromString("16648397-2011-F7E0-D1F4-72E8231FDA89");
	public static final UUID BAUBLE_EFFECT_UUID_2 = UUID.fromString("2C0A7910-E72B-3BC8-B330-7F759D8648A1");
	*/
	/* Initialization */

	public EntityBefriendedZombieGirl(EntityType<? extends EntityBefriendedZombieGirl> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);

	}

	public static Builder createAttributes() {
		return ZombieGirlEntity.createAttributes();
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(4, new BefriendedSunAvoidingFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(5, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));

	}

	
	/* Combat */
	
	// In Demo-1 only because Drowned is not finished
	@Override
	protected boolean convertsInWater()
	{
		return false;
	}
	
	
	@Override
	public boolean doHurtTarget(Entity target)
	{
		// Occupy the main hand to block the ignition action in super.doHurtTarget
		// See Zombie class
		if (this.getMainHandItem().isEmpty())
			this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RegItems.DUMMY_ITEM.get(), 1));
		boolean res = super.doHurtTarget(target);
		// Remove dummy item
		if (!this.getMainHandItem().isEmpty() && this.getMainHandItem().is(RegItems.DUMMY_ITEM.get()))
			this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);		
		// Overwrite ignition here
		if (this.getMainHandItem().isEmpty() && this.isOnFire() && this.random.nextFloat() < 0.8f)
			target.setSecondsOnFire(10);

		return res;
	}
	
	@Override
	public void updateAttributes()
	{
		//this.getattri
	}
	
	/* Interaction */

	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {

		if (player.getUUID().equals(getOwnerUUID())) {
			if (player.level.isClientSide()) {
			}
			// Debug.printToScreen("Friendly Zombie Girl right clicked", player, this);
			else {
				// If this zombie is converted from a husk,
				// it can be converted back by using a sponge to it
				if (player.getItemInHand(hand).is(Items.SPONGE) && isFromHusk) {
					ItemHelper.consumeOne(player.getItemInHand(hand));
					if (!player.addItem(new ItemStack(Items.WET_SPONGE, 1))) {
						this.spawnAtLocation(new ItemStack(Items.WET_SPONGE, 1));
					}
					this.convertToHusk();
				} else if (player.getItemInHand(hand).is(ModItems.SOUL_POWDER.get())) {
					ItemHelper.consumeOne(player.getItemInHand(hand));
					this.heal(5);
				} else if (player.getItemInHand(hand).is(ModItems.SOUL_APPLE.get())) {
					ItemHelper.consumeOne(player.getItemInHand(hand));
					this.heal(15);
				} else
					switchAIState();
				// Debug.printToScreen(getAIState().toString(), player, this);
			}
			return true;
		} else if (!player.level.isClientSide()) {
			Debug.printToScreen("Owner UUID: " + getOwnerUUID(), player, this);
			Debug.printToScreen("Player UUID: " + player.getUUID(), player, this);
		}
		return false;

	}

	@Override
	public boolean onInteractionShift(Player player, InteractionHand hand) {
		if (player.getUUID().equals(getOwnerUUID())) {

			BefriendedHelper.openBefriendedInventory(player, this);

			return true;
		} //else
			//Debug.printToScreen("Owner UUID: " + getOwnerUUID(), player, this);
		return false;
	}

	/* Inventory */

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

	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuZombie(containerId, playerInventory, container, this);
	}

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
	
	public EntityBefriendedHuskGirl convertToHusk()
	{
		EntityBefriendedHuskGirl newMob = (EntityBefriendedHuskGirl)BefriendedHelper.convertToOtherBefriendedType(this, DwmgEntityTypes.BEF_HUSK_GIRL.get());
		newMob.setInit();
		return newMob;
	}
	
	public EntityBefriendedDrownedGirl convertToDrowned()
	{
		EntityBefriendedDrownedGirl newMob = (EntityBefriendedDrownedGirl)BefriendedHelper.convertToOtherBefriendedType(this, DwmgEntityTypes.BEF_DROWNED_GIRL.get());
		newMob.isFromHusk = this.isFromHusk;
		newMob.isFromZombie = true;
		newMob.setInit();
		return newMob;
	}

	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //

	// ------------------ Data sync ------------------ //

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(EntityBefriendedZombieGirl.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(EntityBefriendedZombieGirl.class, EntityDataSerializers.BYTE);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_OWNERUUID, Optional.empty());
		entityData.define(DATA_AISTATE, (byte) 0);
	}

	// ------------------ Data sync end ------------------ //

	// ------------------ IBefriendedMob interface ------------------ //

	/* Init */
	
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
	
	/* Ownership */
	
	@Override
	public Player getOwner() {
		return level.getPlayerByUUID(getOwnerUUID());
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

	/* AI */
	
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

	protected AdditionalInventoryWithEquipment additionalInventory = new AdditionalInventoryWithEquipment(getInventorySize(), this);

	@Override
	public AdditionalInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	// ------------------ IBefriendedMob interface end ------------------ //

	// ------------------ Misc ------------------ //

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
