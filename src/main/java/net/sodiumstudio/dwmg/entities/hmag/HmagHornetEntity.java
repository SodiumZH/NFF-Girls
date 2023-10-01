package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.HornetEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithHandItems;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleEquipable;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedFlyingLandGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedFlyingRandomMoveGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.target.BefriendedNearestUnfriendlyMobTargetGoal;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFlyingFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.HmagFlyingGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.movecontrol.BefriendedFlyingMoveControl;
import net.sodiumstudio.dwmg.inventory.InventoryMenuHandItemsTwoBaubles;
import net.sodiumstudio.dwmg.registries.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.ItemHelper;
public class HmagHornetEntity extends HornetEntity implements IDwmgBefriendedMob
{
	public HmagHornetEntity(EntityType<? extends HornetEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
		this.moveControl = new BefriendedFlyingMoveControl(this);
	}

	@Deprecated
	public static Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 60.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.28D)
				.add(Attributes.ATTACK_DAMAGE, 5.0D)
				.add(Attributes.FOLLOW_RANGE, 24.0D);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(0, new FloatGoal(this));
		this.goalSelector.addGoal(4, new HmagFlyingGoal.ChargeAttackGoal(this, 0.5D, 1.5F, 6));
		//this.goalSelector.addGoal(4, new BefriendedMeleeAttackGoal(this, 1d, false));
		this.goalSelector.addGoal(5, new BefriendedFlyingLandGoal(this));
		this.goalSelector.addGoal(6, new DwmgBefriendedFlyingFollowOwnerGoal(this));
		this.goalSelector.addGoal(8, new BefriendedFlyingRandomMoveGoal(this).heightLimit(10));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new DwmgBefriendedOwnerHurtTargetGoal(this));
		this.targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));
	}
	
	/* Combat */
	public static final int ADD_POISON_LEVEL_DEFAULT = 1;
	public static final int ADD_POISON_TICKS_DEFAULT = 200;
	public int addPoisonLevel = 1;
	public int addPoisonTicks = 10 * 20;	// 10s, equal to hornet poisoning time in hard mode
	
	@Override
	public boolean doHurtTarget(Entity entity)
	{
		if (!super.doHurtTarget(entity))
			return false;
		// Remove old poison effect and add new one
		if (entity instanceof LivingEntity living)
		{
			MobEffectInstance instance = living.getActiveEffectsMap().get(MobEffects.POISON);
			// The expected duration of poison added in super class
			int superExpectedDuration =	 
					level().getDifficulty() == Difficulty.NORMAL ? 100 : (
					level().getDifficulty() == Difficulty.HARD ? 200 : 0);	
			// If the poison is no stronger than the super class given effect, remove it
			if (instance != null && instance.getAmplifier() <= 1 && instance.getDuration() <= superExpectedDuration)
			{
				living.getActiveEffectsMap().remove(MobEffects.POISON);
				instance = null;
			}
			// Add when don't have poison effect, or have lower level() than this mob's adding level(), or have the same level() but with a shorter duration time 
			if (instance == null 
					|| instance.getAmplifier() == addPoisonLevel && instance.getDuration() < addPoisonTicks
					|| instance.getAmplifier() < addPoisonLevel)
			{
				// Don't add poison to undead mobs as it will heal them
				if (!(living instanceof Mob) || ((Mob)living).getMobType() != MobType.UNDEAD)
					// Add poison based on this mob's properties
					living.removeEffect(MobEffects.POISON);
					living.addEffect(new MobEffectInstance(MobEffects.POISON, addPoisonTicks, addPoisonLevel));
			}
		}
		return true;
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HashMap<Item, Float> getHealingItems()
	{
		HashMap<Item, Float> map = new HashMap<Item, Float>();
		map.put(Items.HONEY_BOTTLE, 5.0f);
		map.put(Items.HONEYCOMB, 10.0f);
		map.put(Items.HONEY_BLOCK, 15.0f);
		map.put(ModItems.MYSTERIOUS_PETAL.get(), this.getMaxHealth());	// Mysterious Petal heals to the max
		return map;
	}
	
	// Set of items that can heal the mob WITHOUT CONSUMING.
	// Leave it empty if not needed.
	@Override
	public HashSet<Item> getNonconsumingHealingItems()
	{
		HashSet<Item> set = new HashSet<Item>();
		// set.put(YOUR_ITEM_TYPE);
		return set;
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (!player.isShiftKeyDown())
		{
			if (player.getUUID().equals(getOwnerUUID())) {
				if (!player.level().isClientSide()) 
				{
					/* Put checks before healing item check */
					/* if (....)
					 {
					 	....
					 }
					else */
					boolean isHoneyBottle = (player.getItemInHand(hand).is(Items.HONEY_BOTTLE));
					if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS) 
					{
						if (isHoneyBottle)
							ItemHelper.giveOrDropDefault(player, Items.GLASS_BOTTLE);
						return InteractionResult.sidedSuccess(player.level().isClientSide);
					}
					// The function above returns PASS when the items are not correct. So when not PASS it should stop here
					else if (hand == InteractionHand.MAIN_HAND
							&& DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
					{
						switchAIState();
						return InteractionResult.sidedSuccess(player.level().isClientSide);
					}
					else return InteractionResult.PASS;
				}
				// Interacted
				return InteractionResult.sidedSuccess(player.level().isClientSide);
			} 
			else return InteractionResult.PASS;
		}
		
		else
		{
			if (player.getUUID().equals(getOwnerUUID())) {		
				if (hand == InteractionHand.MAIN_HAND && DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
				{
					BefriendedHelper.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level().isClientSide);
				}
			}
			return InteractionResult.PASS;
		}
	}

	/* Inventory */

	// This enables mob armor and hand items by default.
	// If not needed, use BefriendedInventory class instead.
	protected BefriendedInventoryWithHandItems additionalInventory 
		= new BefriendedInventoryWithHandItems(getInventorySize(), this);

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	@Override
	public int getInventorySize()
	{
		return 4;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level().isClientSide) {
			additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level().isClientSide) {
			additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuHandItemsTwoBaubles(containerId, playerInventory, container, this);
	}

	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		// Add other data to save here
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		// Add other data reading here
		setInit();
	}

	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(HmagHornetEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(HmagHornetEntity.class, EntityDataSerializers.INT);

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

	/* IBaubleEquipable interface */

	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(2));
		map.put("1", this.getAdditionalInventory().getItem(3));
		return map;
	}
	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.HORNET;
	}

	// ------------------ Misc ------------------ //
	
	@Override
	public String getModId() {
		return Dwmg.MOD_ID;
	}
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //
	
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
