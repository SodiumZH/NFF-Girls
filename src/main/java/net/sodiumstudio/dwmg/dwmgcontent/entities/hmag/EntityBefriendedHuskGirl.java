package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.swing.text.html.parser.Entity;

import com.github.mechalopa.hmag.world.entity.HuskGirlEntity;
import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.dwmg.befriendmobs.client.gui.screens.AbstractGuiBefriended;
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
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.NbtHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.befriendmobs.util.exceptions.UnimplementedException;
import net.sodiumstudio.dwmg.dwmgcontent.client.gui.screens.GuiZombieGirl;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.*;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuZombie;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEntityTypes;

public class EntityBefriendedHuskGirl extends HuskGirlEntity implements IBefriendedMob {

	/* Initialization */

	public EntityBefriendedHuskGirl(EntityType<? extends EntityBefriendedHuskGirl> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);

	}

	public static Builder createAttributes() {
		return HuskGirlEntity.createAttributes();
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

	/* Interaction */

	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {

		if (player.getUUID().equals(getOwnerUUID())) {
			if (player.level.isClientSide()) {}
				//Debug.printToScreen("Friendly Zombie Girl right clicked", player, this);
			else {
				switchAIState();
				//Debug.printToScreen(getAIState().toString(), player, this);
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

	protected AdditionalInventoryWithEquipment additionalInventory = new AdditionalInventoryWithEquipment(getInventorySize(), this);

	@Override
	public AdditionalInventory getAdditionalInventory()
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

	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			additionalInventory.getFromMob(this);
		}
	}
	
	@Override
	public ItemStack getBauble(int index) {
		if (index == 0)
			return additionalInventory.getItem(6);
		else if (index == 1)
			return additionalInventory.getItem(7);
		else
			return null;
	}

	@Override
	public void setBauble(ItemStack item, int index) {
		ItemStack itemCpy = item;
		if (item == null || item.isEmpty())
			itemCpy = ItemStack.EMPTY;
		if (index == 0)
			additionalInventory.setItem(6, itemCpy);
		else if (index == 1)
			additionalInventory.setItem(7, itemCpy);
		else
			throw new IndexOutOfBoundsException("Befriended mob bauble index out of bound.");
		updateFromInventory();
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
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		this.setInit();
	}

	/* Conversion */
	
	@Override
	protected void doUnderWaterConversion() {
		this.convertToZombie();
		if (!this.isSilent())
		{
			this.level.levelEvent((Player) null, 1041, this.blockPosition(), 0);
		}
	}	
	
	public void forceUnderWaterConversion()
	{
		this.doUnderWaterConversion();
	}
	
	// Called when convert to zombie
	protected EntityBefriendedZombieGirl convertToZombie()
	{
		EntityBefriendedZombieGirl newMob = (EntityBefriendedZombieGirl)BefriendedHelper.convertToOtherBefriendedType(this, DwmgEntityTypes.BEF_ZOMBIE_GIRL.get());
		newMob.isFromHusk = true;
		newMob.setInit();
		return newMob;
	}
	
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //

	// ------------------ Data sync ------------------ //

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(EntityBefriendedHuskGirl.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(EntityBefriendedHuskGirl.class, EntityDataSerializers.BYTE);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_OWNERUUID, Optional.empty());
		entityData.define(DATA_AISTATE, (byte) 0);
	}

	// ------------------ Data sync end ------------------ //

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
