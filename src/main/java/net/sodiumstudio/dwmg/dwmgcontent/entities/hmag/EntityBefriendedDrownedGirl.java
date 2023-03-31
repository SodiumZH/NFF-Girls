package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.DrownedGirlEntity;

import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.event.sound.SoundEvent;
import net.sodiumstudio.dwmg.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedFleeSunGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedRestrictSunGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.BefriendedZombieAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.vanilla.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventoryWithEquipment;
import net.sodiumstudio.dwmg.befriendmobs.util.ItemHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedDrownedGoals;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedInWaterFollowOwnerGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedSunAvoidingFollowOwnerGoal;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuZombie;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEntityTypes;

public class EntityBefriendedDrownedGirl extends DrownedGirlEntity implements IBefriendedMob
{

	/* Initialization */

	public EntityBefriendedDrownedGirl(EntityType<? extends EntityBefriendedDrownedGirl> pEntityType, Level pLevel)
	{
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);

	}

	public static Builder createAttributes() {
		return Zombie.createAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.245D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ARMOR, 3.0D);
	}

	/* AI */

	@Override
	protected void registerGoals() {

		goalSelector.addGoal(1, new BefriendedDrownedGoals.GoToWaterGoal(this, 1.0D));
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedDrownedGoals.TridentAttackGoal(this, 1.0D, 40, 10.0F));
		goalSelector.addGoal(3, new BefriendedDrownedGoals.AttackGoal(this, 1.0D, false));
		goalSelector.addGoal(4, new BefriendedSunAvoidingFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(4, new BefriendedInWaterFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(5, new BefriendedDrownedGoals.GoToBeachGoal(this, 1.0D));
		goalSelector.addGoal(6, new BefriendedDrownedGoals.SwimUpGoal(this, 1.0D, this.level.getSeaLevel()));
		goalSelector.addGoal(7, new BefriendedRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));

	}
	
	@Override
	public void tick()
	{
		setSearchingForLand(true);
		super.tick();
	}
	
	/* Combat */

	@Override
	public void updateAttributes() {
		// TODO: actions
	}

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
		ThrownTrident throwntrident = new ThrownTrident(this.level, this, new ItemStack(Items.TRIDENT));
		double d0 = pTarget.getX() - this.getX();
		double d1 = pTarget.getY(0.3333333333333333D) - throwntrident.getY();
		double d2 = pTarget.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		throwntrident.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, 2.0F);	// Inaccuracy is fixed at hard mode (2)
		this.playSound(SoundEvents.DROWNED_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.level.addFreshEntity(throwntrident);
	}
	
	/* Interaction */

	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {

		if (player.getUUID().equals(getOwnerUUID())) {
			if (!player.level.isClientSide()) {
				// If this drowned is converted from a zombie,
				// it can be converted back by using a sponge to it
				if (player.getItemInHand(hand).is(Items.SPONGE) && isFromZombie) {
					ItemHelper.consumeOne(player.getItemInHand(hand));
					if (!player.addItem(new ItemStack(Items.WET_SPONGE, 1))) {
						this.spawnAtLocation(new ItemStack(Items.WET_SPONGE, 1));
					}
					this.convertToZombie();
				} else if (player.getItemInHand(hand).is(ModItems.SOUL_POWDER.get())) {
					ItemHelper.consumeOne(player.getItemInHand(hand));
					this.heal(5);
				} else if (player.getItemInHand(hand).is(ModItems.SOUL_APPLE.get()) && this.getHealth() != this.getMaxHealth()) {
					ItemHelper.consumeOne(player.getItemInHand(hand));
					this.heal(15);
				} else if (hand.equals(InteractionHand.MAIN_HAND))
					switchAIState();
				// Debug.printToScreen(getAIState().toString(), player, this);
			}
			return true;
		}
		return false;

	}

	@Override
	public boolean onInteractionShift(Player player, InteractionHand hand) {
		if (player.getUUID().equals(getOwnerUUID()))
		{

			BefriendedHelper.openBefriendedInventory(player, this);

			return true;
		} 
		return false;
	}

	/* Inventory */

	@Override
	public int getInventorySize() {
		return 8;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide)
		{
			additionalInventory.setMobEquipment(this);
		}
	}

	public void setInventoryFromMob() {
		if (!this.level.isClientSide)
		{
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
		nbt.put("is_from_zombie", ByteTag.valueOf(isFromZombie));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		isFromHusk = nbt.getBoolean("is_from_husk");
		isFromZombie = nbt.getBoolean("is_from_zombie");
		setInit();
	}

	/* Convertions */

	public boolean isFromHusk = false;
	public boolean isFromZombie = false;

	public EntityBefriendedZombieGirl convertToZombie() {
		EntityBefriendedZombieGirl newMob = (EntityBefriendedZombieGirl) BefriendedHelper
				.convertToOtherBefriendedType(this, DwmgEntityTypes.HMAG_ZOMBIE_GIRL.get());
		newMob.isFromHusk = isFromHusk;
		newMob.setInit();
		return newMob;
	}

	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //

	// ------------------ Data sync ------------------ //

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(EntityBefriendedDrownedGirl.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(EntityBefriendedDrownedGirl.class, EntityDataSerializers.BYTE);

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
	public boolean hasInit() {
		return initialized;
	}

	@Override
	public void setInit() {
		initialized = true;
	}

	/* Ownership */

	@Override
	public Player getOwner() {
		return getOwnerUUID() != null ? level.getPlayerByUUID(getOwnerUUID()) : null;
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

	protected AdditionalInventoryWithEquipment additionalInventory = new AdditionalInventoryWithEquipment(
			getInventorySize(), this);

	@Override
	public AdditionalInventory getAdditionalInventory() {
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
