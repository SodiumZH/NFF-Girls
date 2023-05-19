package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
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
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
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
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedAmphibious;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedAmphibiousGoals;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedZombieAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFollowOwnerGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRandomSwimGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRestrictSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.befriendmobs.util.ItemHelper;
import net.sodiumstudio.befriendmobs.util.MiscUtil;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.BefriendedDrownedTridentAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.inventory.InventoryMenuEquipmentTwoBaubles;
import net.sodiumstudio.dwmg.registries.DwmgEntityTypes;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class EntityBefriendedDrownedGirl extends DrownedGirlEntity implements IDwmgBefriendedMob, IBefriendedUndeadMob, IBefriendedAmphibious
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

	@Override
	public EntityDataAccessor<Optional<UUID>> getOwnerUUIDAccessor() {
		return DATA_OWNERUUID;
	}

	@Override
	public EntityDataAccessor<Byte> getAIStateData() {
		return DATA_AISTATE;
	}
	
	/* AI */

	@Override
	protected void registerGoals() {

		goalSelector.addGoal(1, new BefriendedAmphibiousGoals.GoToWaterGoal(this, 1.0D));
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedDrownedTridentAttackGoal(this, 1.0D, 40, 10.0F));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0D, false));
		goalSelector.addGoal(4, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false).amphibious()
				.avoidSunCondition(mob -> {return ((EntityBefriendedDrownedGirl)mob).isSunSensitive();}));
		//goalSelector.addGoal(4, new BefriendedInWaterFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f));
		goalSelector.addGoal(5, new BefriendedAmphibiousGoals.GoToBeachGoal(this, 1.0D));
		goalSelector.addGoal(6, new BefriendedAmphibiousGoals.SwimUpGoal(this, 1.0D, this.level.getSeaLevel()));
		goalSelector.addGoal(7, new BefriendedRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new BefriendedRandomSwimGoal(this, 1.0d, 120));
		goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new DwmgBefriendedOwnerHurtTargetGoal(this));

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
	public void performRangedAttack(LivingEntity pTarget, float pDistanceFactor) {
		ThrownTrident throwntrident = new ThrownTrident(this.level, this, new ItemStack(Items.TRIDENT));
		double d0 = pTarget.getX() - this.getX();
		double d1 = pTarget.getY(0.3333333333333333D) - throwntrident.getY();
		double d2 = pTarget.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		throwntrident.shoot(d0, d1 + d3 * 0.2F, d2, 1.6F, 2.0F);	// Inaccuracy is fixed at hard mode (i.e. 2.0)
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
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (getOwner() == null)
		{			
			throw new RuntimeException("Mob \"" + this.getName().getString() + "\" missing owner.");
		}
		// Porting solution end
		if (!player.isShiftKeyDown())
		{
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
					{
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					}
					else if (hand == InteractionHand.MAIN_HAND)
					{
						switchAIState();
					}	
				}
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			} 
			return InteractionResult.PASS;
		}
		else
		{
			if (player.getUUID().equals(getOwnerUUID()))
			{
				if (!player.level.isClientSide)
					BefriendedHelper.openBefriendedInventory(player, this);
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			} 
			return InteractionResult.PASS;
		}
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

	protected BefriendedInventoryWithEquipment additionalInventory = new BefriendedInventoryWithEquipment(
			getInventorySize(), this);

	@Override
	public BefriendedInventory getAdditionalInventory() {
		return additionalInventory;
	}
	
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
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuEquipmentTwoBaubles(containerId, playerInventory, container, this);
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
	
	/* IBefriendedUndeadMob interface */

	@Override
	public void setupSunImmunityRules() {
		this.sunImmuneConditions().put("sunhat", () -> this.getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.SUNHAT.get()));
		this.sunImmuneConditions().put("soul_amulet", () -> this.hasBaubleItem(DwmgItems.SOUL_AMULET.get()));
		this.sunImmuneConditions().put("resis_amulet", () -> this.hasBaubleItem(DwmgItems.RESISTANCE_AMULET.get()));
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
		return DwmgBaubleHandlers.DROWNED;
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
