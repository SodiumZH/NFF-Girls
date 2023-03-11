package net.sodiumstudio.befriendmobs.example;

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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entitiy.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entitiy.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla.BefriendedFleeSunGoal;
import net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla.BefriendedFollowOwnerGoal;
import net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla.BefriendedRestrictSunGoal;
import net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla.BefriendedZombieAttackGoal;
import net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.befriendmobs.util.Debug;
import net.sodiumstudio.befriendmobs.util.NbtHelper;

/**
 * @author SodiumZH
 * This example shows how to make a friendly (tamed) Zombie with BefriendMobs.
 * Some functions from IBefriended interface are using the default implementation. To see 
 * those functions, turn to the comment in IBefriendedMob.
 */
public class EXAMPLE_BefriendedZombie extends EXAMPLE_BefriendableZombie implements IBefriendedMob {
	
	/* Initialization */
	
	// Construction
	public EXAMPLE_BefriendedZombie(EntityType<? extends Zombie> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		// Mandatory: initialize inventory
		createInventory();
		// Recommended
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 2.0f);
		Arrays.fill(this.handDropChances, 2.0f);

	}

	// Works as any other mobs
	public static Builder createAttributes() {
		return Zombie.createAttributes().add(Attributes.MAX_HEALTH, 30.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ARMOR, 4.0D);
	}

	/* AI */

	/** As in AI classes the IBefriendedMob instances are commonly needed,
	/* Many vanilla goals are rewritten inheriting BefriendedGoal or BefriendedTargetGoal
	 * and added extra features to adapt IBefriendedMob. 
	 * These rewritten AI goals are all added "Befriended" prefix.
	 * If there is an overwritten goal, use it instead of the vanilla one.
	 */
	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(4, new BefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(5, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));

	}

	/* Interaction */

	/** Handle player interaction here.
	 * This function will be called 4 times on player right click the mob,
	 * on Client+Mainhand, Server+Mainhand, Client+Offhand, Server+Offhand subsequently.
	 * If this function returns true, the whole right click event will be set handled
	 * and may cancel some other event listeners.
	 */
	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {

		if (player.getUUID().equals(getOwnerUUID())) {
			if (player.level.isClientSide()) {}
			else {
				switchAIState();
			}
			return true;
		} else if (!player.level.isClientSide()) {
			Debug.printToScreen("Owner UUID: " + getOwnerUUID(), player, this);
			Debug.printToScreen("Player UUID: " + player.getUUID(), player, this);
		}
		return false;

	}

	// Called when player Shift+RightClick on the mob. Similar mechanism as above.
	@Override
	public boolean onInteractionShift(Player player, InteractionHand hand) {
		if (player.getUUID().equals(getOwnerUUID())) {
			if (hand.equals(InteractionHand.MAIN_HAND))
				// This method opens inventory GUI on server. It won't do anything on client.
				BefriendedHelper.openBefriendedInventory(player, this);
			return true;
		} 
		return false;
	}

	/* Inventory */
	
	/**
	 * In this example the armor and hands slots are included in the inventory,
	 * together with 2 extra "bauble" slots (see getBauble function for detail).
	 */
	@Override
	public int getInventorySize()
	{
		return 8;
	}

	/** Executed only on server to update mob state from inventory.
	 *  In this example, armor and hand items are synced. If the inventory doesn't include these 6 vanilla slots it's not needed.
	 */
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
	public void setInventoryFromMob() {
		if (!this.level.isClientSide) {
			getInventory().setItem(0, this.getItemBySlot(EquipmentSlot.HEAD));
			getInventory().setItem(1, this.getItemBySlot(EquipmentSlot.CHEST));
			getInventory().setItem(2, this.getItemBySlot(EquipmentSlot.LEGS));
			getInventory().setItem(3, this.getItemBySlot(EquipmentSlot.FEET));
			getInventory().setItem(4, this.getMainHandItem());
			getInventory().setItem(5, this.getOffhandItem());
		}
	}
	
	
	/** Get bauble from the given bauble slot.
	 * "Bauble" refers to extra item slots that are easy to access and provide some extra features.
	 * the input of getBauble() is the "Bauble index". For example, if there are 2 bauble slots, the indexes should be 0 or 1.
	 * The map from Bauble index and Inventory index are defined in this implementation.
	 * To add into bauble slots, the item must have befriendmobs:
	 */
	@Override
	public ItemStack getBauble(int index) {
		if (index == 0)
			return getInventory().getItem(6);
		else if (index == 1)
			return getInventory().getItem(7);
		else
			return null;
	}

	/**	Set Bauble item stack.
	 * Similarly, the input index is "Bauble index", not the Inventory index.
	 */
	@Override
	public void setBauble(ItemStack item, int index) {
		if (item == null || item.isEmpty())
			return;
		if (index == 0)
			inventory.setItem(6, item);
		else if (index == 1)
			inventory.setItem(7, item);
		else
			throw new IndexOutOfBoundsException("Befriended mob bauble index out of bound.");
		updateFromInventory();
	}

	/** Make inventory menu.
	 * This method should return a new menu instance.
	 */
	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new EXAMPLE_MenuZombie(containerId, playerInventory, container, this);
	}

	/* Save and Load */

	// Save extra data for this mob.
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		// Recommended. This function automatically saves the owner and AI state information.
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		// Save extra Bauble items. Baubles need to be manually saved.
		NbtHelper.saveItemStack(inventory.getItem(6), nbt, "bauble_0");
		NbtHelper.saveItemStack(inventory.getItem(7), nbt, "bauble_1");
	}

	// Read extra data. Similar to those above.
	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		setBauble(NbtHelper.readItemStack(nbt, "bauble_0"), 0);
		setBauble(NbtHelper.readItemStack(nbt, "bauble_1"), 1);
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

	SimpleContainer inventory = null;

	@Override
	public SimpleContainer getInventory() {
		return inventory;
	}

	@Override
	public void setInventory(SimpleContainer container) {
		inventory = container;
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
