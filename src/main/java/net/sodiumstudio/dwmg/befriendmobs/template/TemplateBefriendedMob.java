package net.sodiumstudio.dwmg.befriendmobs.template;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.example.EXAMPLE_BefriendedZombie;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventoryWithEquipment;

// This is a template for befriended mob class.
// You may copy-paste the code below to your class and modify at places labeled by /*...*/.
public class TemplateBefriendedMob /* Your mob class */ extends Mob /* Your mob superclass */ implements IBefriendedMob
{
	
	// Initialization
	
	public TemplateBefriendedMob/* Your mob class */ (EntityType<? extends Mob /* Your mob class */ > pEntityType, Level pLevel) {
		super(pEntityType, pLevel);	
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0f);
		Arrays.fill(this.handDropChances, 0f);
		/* Initialization */
	}

	public static Builder createAttributes() {
		return null; /* Change to your attribute */
	}

	@Override
	protected void registerGoals() {
		/* Add register goals */
	}

	// Initialization end
	
	// Attributes
	

	@Override
	public void updateAttributes() {
		/* Update attributes here */
		/* It will auto-called on initialization and container update. */
		
	}
	
	// Interaction
	
	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {

		if (player.getUUID().equals(getOwnerUUID())) {
			if (!player.level.isClientSide()) 
			{
				switchAIState();
			}
			return true;
		}
			/* Other actions */
		return false;
	}

	@Override
	public boolean onInteractionShift(Player player, InteractionHand hand) 
	{
		if (player.getUUID().equals(getOwnerUUID())) {
	
			if (hand.equals(InteractionHand.MAIN_HAND))
				BefriendedHelper.openBefriendedInventory(player, this);
			return true;
		}
		/* Other actions... */
		return false;
	}

	// Interaction end
	
	// Inventory related
	// Generally no need to modify unless noted
	
	AdditionalInventory additionalInventory = new AdditionalInventoryWithEquipment(getInventorySize());

	@Override
	public AdditionalInventory getAdditionalInventory()
	{
		return additionalInventory;
	}

	@Override
	public int getInventorySize()
	{
		/* Change to your size */
		return 8;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			/* If mob's properties (e.g. equipment, HP, etc.) needs to sync with inventory, set here */
		}
	}

	@Override
	public void setInventoryFromMob() {
		if (!this.level.isClientSide) {
			/* If inventory needs to be set from mob's properties on initialization, set here */
		}
	}
	
	@Override
	public ItemStack getBauble(int index) {
		/* Customize here */
		/* Example:
		if (index == 0)
			return getAdditionalInventory().getItem(6);
		else if (index == 1)
			return getAdditionalInventory().getItem(7);
		else
			return null;
		*/
		return null;
	}

	/**	Set Bauble item stack.
	 * Similarly, the input index is "Bauble index", not the Inventory index.
	 */
	@Override
	public void setBauble(ItemStack item, int index) {
		/* Customize here */
		/* Example:
		if (item == null || item.isEmpty())
			return;
		if (index == 0)
			additionalInventory.setItem(6, item);
		else if (index == 1)
			additionalInventory.setItem(7, item);
		else
			throw new IndexOutOfBoundsException("Befriended mob bauble index out of bound.");
		*/
		updateFromInventory();
	}
	 
	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container) {
		return null; /* return new YourMenuClass(containerId, playerInventory, container, this) */
	}
	
	// Inventory end

	// save&load

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		/* Add more save data... */
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		/* Add more save data... */
		this.setInit();
	}

	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //

	// ------------------ Data sync ------------------ //

	// By default owner uuid and ai state need to sync
	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(EXAMPLE_BefriendedZombie.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(EXAMPLE_BefriendedZombie.class, EntityDataSerializers.BYTE);

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

	// AI related
	
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

	// ------------------ IBefriendedMob interface end ------------------ //

	// ------------------ Misc ------------------ //

	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	/* add @Override annotation if inheriting Monster class */
	/* @Override */
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
