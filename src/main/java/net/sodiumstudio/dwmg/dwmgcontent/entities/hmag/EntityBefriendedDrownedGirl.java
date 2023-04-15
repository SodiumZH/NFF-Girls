package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
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
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
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
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.BefriendedAmphibiousGoals;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.BefriendedZombieAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedRandomSwimGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedRestrictSunGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventory;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventoryWithEquipment;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.befriendmobs.util.ItemHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.IBefriendedAmphibious;
import net.sodiumstudio.dwmg.dwmgcontent.entities.IBefriendedUndeadMob;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedDrownedTridentAttackGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedInWaterFollowOwnerGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedSunAvoidingFollowOwnerGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuZombie;
import net.sodiumstudio.dwmg.dwmgcontent.registries.DwmgEntityTypes;

public class EntityBefriendedDrownedGirl extends DrownedGirlEntity implements IBefriendedMob, IBefriendedUndeadMob, IBaubleHolder, IBefriendedAmphibious
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

		goalSelector.addGoal(1, new BefriendedAmphibiousGoals.GoToWaterGoal(this, 1.0D));
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedDrownedTridentAttackGoal(this, 1.0D, 40, 10.0F));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(4, new BefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false).avoidSun().amphibious());
		//goalSelector.addGoal(4, new BefriendedInWaterFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f));
		goalSelector.addGoal(5, new BefriendedAmphibiousGoals.GoToBeachGoal(this, 1.0D));
		goalSelector.addGoal(6, new BefriendedAmphibiousGoals.SwimUpGoal(this, 1.0D, this.level.getSeaLevel()));
		goalSelector.addGoal(7, new BefriendedRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new BefriendedRandomSwimGoal(this, 1.0d, 120));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));

	}
	
	@Override
	public void tick()
	{
		// This affects Drowned::wantsToSwim(),
		// if searching-for-land is false and it doesn't have a target
		// the drowned cannot swim
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
	public HashMap<Item, Float> getHealingItems()
	{
		HashMap<Item, Float> map = new HashMap<Item, Float>();
		map.put(ModItems.SOUL_POWDER.get(), 5.0f);
		map.put(ModItems.SOUL_APPLE.get(), 15.0f);
		return map;
	}
	
	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {
		if (player.getUUID().equals(getOwnerUUID())) {
			if (!player.level.isClientSide() && hand == InteractionHand.MAIN_HAND) 
			{
				// If this zombie is converted from a husk,
				// it can be converted back by using a sponge to it
				if (player.getItemInHand(hand).is(Items.SPONGE) && isFromZombie) {
					ItemHelper.consumeOne(player.getItemInHand(hand));
					this.spawnAtLocation(new ItemStack(Items.WET_SPONGE, 1));
					EntityBefriendedZombieGirl z = this.convertToZombie();
					z.isFromHusk = this.isFromHusk;
				} 
				else if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
				{}
				else if (hand == InteractionHand.MAIN_HAND)
				{
					switchAIState();
				}	
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

	@Override
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
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt, Dwmg.MOD_ID);
		nbt.put("is_from_husk", ByteTag.valueOf(isFromHusk));
		nbt.put("is_from_zombie", ByteTag.valueOf(isFromZombie));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt, Dwmg.MOD_ID);
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
	
	/* IBefriendedUndeadMob interface */

	public boolean sunSensitive = true;
	@Override
	protected boolean isSunSensitive() {
		return sunSensitive;
	}
	@Override
	public void setSunSensitive(boolean value) {
		sunSensitive = value;		
	}

	/* IBaubleHolder interface */

	@Override
	public HashSet<ItemStack> getBaubleStacks() {
		HashSet<ItemStack> set = new HashSet<ItemStack>();
		set.add(this.getAdditionalInventory().getItem(6));
		set.add(this.getAdditionalInventory().getItem(7));
		return set;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.DROWNED;
	}	
	protected HashMap<AttributeModifier, Attribute> baubleModifierMap = new HashMap<AttributeModifier, Attribute>();	
	@Override
	public HashMap<AttributeModifier, Attribute> getExistingBaubleModifiers() {
		return baubleModifierMap;
	}

	/* IBefriendedAmphibious interface */

	@Override
	public WaterBoundPathNavigation getWaterNav() {
		return this.waterNavigation;
	}

	@Override
	public GroundPathNavigation getGroundNav() {
		return this.groundNavigation;
	}

	@Override
	public PathNavigation getAppliedNav()
	{
		return this.navigation;
	}
	
	@Override
	public void switchNav(boolean isWaterNav) {
		this.navigation = isWaterNav ? this.waterNavigation : this.groundNavigation;
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
