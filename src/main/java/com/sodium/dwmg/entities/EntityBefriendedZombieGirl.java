package com.sodium.dwmg.entities;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;
import com.sodium.dwmg.client.gui.screens.AbstractGuiBefriended;
import com.sodium.dwmg.client.gui.screens.GuiVanillaUndead;
import com.sodium.dwmg.client.gui.screens.GuiZombieGirl;
import com.sodium.dwmg.entities.ai.BefriendedAIState;
import com.sodium.dwmg.entities.ai.goals.*;
import com.sodium.dwmg.entities.ai.goals.target.*;
import com.sodium.dwmg.inventory.AbstractInventoryMenuBefriended;
import com.sodium.dwmg.inventory.InventoryMenuZombieGirl;
import com.sodium.dwmg.util.Debug;
import com.sodium.dwmg.util.NbtHelper;

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

public class EntityBefriendedZombieGirl extends ZombieGirlEntity implements IBefriendedMob, ContainerListener {

	/* Initialization */

	public EntityBefriendedZombieGirl(EntityType<? extends EntityBefriendedZombieGirl> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 2.0f);
		Arrays.fill(this.handDropChances, 2.0f);
		createInventory();
	}

	public static Builder createAttributes() {
		return ZombieGirlEntity.createAttributes().add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ARMOR, 4.0D);
	}

	@Override
	public IBefriendedMob init(@Nonnull UUID playerUUID, LivingEntity befriendedFrom) {
		setOwnerUUID(playerUUID);
		if (!((LivingEntity) this).level.isClientSide() && befriendedFrom != null) {
			this.setHealth(befriendedFrom.getHealth());
		}
		return this;
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

	@Override
	public boolean wantsToAttack(LivingEntity target) {
		return BefriendedHelper.wantsToAttackDefault(this, target);
	}

	/* Interaction */

	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {

		if (player.getUUID().equals(getOwnerUUID())) {
			if (player.level.isClientSide())
				Debug.printToScreen("Friendly Zombie Girl right clicked", player, this);
			else {
				switchAIState();
				Debug.printToScreen(getAIState().toString(), player, this);
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
			try {
				BefriendedHelper.openBefriendedInventory(player, this);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return true;
		} else
			Debug.printToScreen("Owner UUID: " + getOwnerUUID(), player, this);
		return false;
	}

	/* Inventory */

	SimpleContainer inventory = null;
	
	@Override
	public SimpleContainer getInventory() {
		return inventory;
	}

	public void createInventory()
	{
		  SimpleContainer simplecontainer = this.inventory;
	      this.inventory = new SimpleContainer(8);
	      if (simplecontainer != null) {
	         simplecontainer.removeListener(this);
	         int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

	         for(int j = 0; j < i; ++j) {
	            ItemStack itemstack = simplecontainer.getItem(j);
	            if (!itemstack.isEmpty()) {
	               this.inventory.setItem(j, itemstack.copy());
	            }
	         }
	      }

	      this.inventory.addListener(this);
	      this.updateFromInventory();
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			setItemSlot(EquipmentSlot.HEAD, getInventory().getItem(0));
			setItemSlot(EquipmentSlot.CHEST, getInventory().getItem(1));
			setItemSlot(EquipmentSlot.LEGS, getInventory().getItem(2));
			setItemSlot(EquipmentSlot.FEET, getInventory().getItem(3));
			setItemInHand(InteractionHand.MAIN_HAND, getInventory().getItem(4));
			setItemInHand(InteractionHand.OFF_HAND, getInventory().getItem(5));
		}
	}

	@Override
	public ItemStack getBauble(int index) 
	{
		if (index == 0)
			return getInventory().getItem(6);
		else if (index == 1)
			return getInventory().getItem(7);
		else
			return null;
	}

	@Override
	public void setBauble(ItemStack item, int index)
	{
		if (item == null || item.isEmpty())
			return;
		if (index == 0)
			inventory.setItem(6, item);
		else if (index == 1)
			inventory.setItem(7, item);
		else throw new IndexOutOfBoundsException("Befriended mob bauble index out of bound.");
		updateFromInventory();
	}
	
	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container)
	{
		return new InventoryMenuZombieGirl(containerId, playerInventory, container, this);
	}
	
	@Override
	public void containerChanged(Container pContainer)
	{
		this.updateFromInventory();
	}
	
	/* Save and Load */ 
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		NbtHelper.saveItemStack(inventory.getItem(6), nbt, "bauble_0");
		NbtHelper.saveItemStack(inventory.getItem(7), nbt, "bauble_1");
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		setBauble(NbtHelper.readItemStack(nbt, "bauble_0"), 0);		
		setBauble(NbtHelper.readItemStack(nbt, "bauble_1"), 1);
	}

	/* GUI */
	
	@Override
	public AbstractGuiBefriended makeGui(AbstractInventoryMenuBefriended menu, Inventory playerInventory, Component title)
	{
		return new GuiZombieGirl(menu, playerInventory, title, this);
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

	// Owner related
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

	@Override
	public BefriendedAIState switchAIState() {
		entityData.set(DATA_AISTATE, getAIState().defaultSwitch().id());
		return getAIState();
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
