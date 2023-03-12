package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.SkeletonGirlEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedFleeSunGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedRestrictSunGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.util.Debug;
import net.sodiumstudio.dwmg.befriendmobs.util.InventoryTag;
import net.sodiumstudio.dwmg.befriendmobs.util.InventoryTagWithEquipment;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedSkeletonMeleeAttackGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedSkeletonRangedBowAttackGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedSunAvoidingFollowOwnerGoal;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuSkeletonGirl;

public class EntityBefriendedSkeletonGirl extends SkeletonGirlEntity implements IBefriendedMob
{

	
	public EntityBefriendedSkeletonGirl(EntityType<? extends EntityBefriendedSkeletonGirl> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	public static Builder createAttributes() 
	{
		return SkeletonGirlEntity.createAttributes();
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedSkeletonRangedBowAttackGoal(this, 1.0D, 20, 15.0F));
		goalSelector.addGoal(4, new BefriendedSkeletonMeleeAttackGoal(this, 1.2d, true));
		goalSelector.addGoal(5, new BefriendedSunAvoidingFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));

	}

	/* Bow shooting related */

	@Override
	public void aiStep() {
		super.aiStep();
		
		if (this.getTarget() != null) {
			// When too close, switch to melee mode if possible
			if (this.distanceTo(this.getTarget()) < 2.5) {
				if (inventoryTag.get(4).is(Items.BOW) && inventoryTag.get(7).getItem() instanceof TieredItem) {
					inventoryTag.swapItem(4, 7);
					updateFromInventory();
				}
			}
			// When run out arrows, try taking weapon from backup-weapon slot
			if (inventoryTag.get(4).is(Items.BOW) && inventoryTag.get(7).getItem() instanceof TieredItem
					&& inventoryTag.get(8).isEmpty()) {
				inventoryTag.swapItem(4, 7);
				updateFromInventory();
			}
			// When too far and having a bow on backup-weapon, switch to bow mode
			// Don't switch if don't have arrows
			else if (this.distanceTo(this.getTarget()) > 4) {
				if (!inventoryTag.get(4).is(Items.BOW) && inventoryTag.get(7).is(Items.BOW)
						&& !inventoryTag.get(8).isEmpty()) {
					inventoryTag.swapItem(4, 7);
					updateFromInventory();
				}
			}
		}
	}
	
	/* Bow shooting end */
	
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
		}
		return false;
	}

	/* Inventory */


	protected InventoryTagWithEquipment inventoryTag = new InventoryTagWithEquipment(getInventorySize());

	@Override
	public InventoryTag getInventoryTag()
	{
		return inventoryTag;
	}
	
	@Override
	public SimpleContainer makeContainerFromInventory() {
		return inventoryTag.toContainer();
	}

	@Override
	public void saveInventory(SimpleContainer container)
	{
		inventoryTag.setFromContainer(container);
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
			inventoryTag.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob() {
		if (!this.level.isClientSide) {
			inventoryTag.getFromMob(this);
		}
	}
	
	@Override
	public ItemStack getBauble(int index) {
		if (index == 0)
			return inventoryTag.get(6);
		else
			return null;
	}

	@Override
	public void setBauble(ItemStack item, int index) {
		if (item == null || item.isEmpty())
			return;
		if (index == 0)
			inventoryTag.put(item, 6);
		else
			throw new IndexOutOfBoundsException("Befriended mob bauble index out of bound.");
		updateFromInventory();
	}	
	
	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuSkeletonGirl(containerId, playerInventory, container, this);
	}
	
	/* Save and Load */

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		inventoryTag.saveTo(nbt, "inventory_tag");
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		inventoryTag.readFrom(nbt.getCompound("inventory_tag"));
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
