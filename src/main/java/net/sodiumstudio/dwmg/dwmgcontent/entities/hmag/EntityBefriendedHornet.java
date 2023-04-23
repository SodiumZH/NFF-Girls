package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.HornetEntity;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.dwmg.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.dwmg.befriendmobs.inventory.BefriendedInventoryWithHandItems;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.dwmg.befriendmobs.template.TemplateBefriendedMobPreset;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.HmagFlyingGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuHandItemsTwoBaubles;

public class EntityBefriendedHornet extends HornetEntity implements IBaubleHolder, IBefriendedMob
{
	public EntityBefriendedHornet(EntityType<? extends HornetEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

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
		this.goalSelector.addGoal(4, new BefriendedMeleeAttackGoal(this, 1d, false));
		this.goalSelector.addGoal(6, new HmagFlyingGoal.FollowOwnerGoal(this));
		this.goalSelector.addGoal(8, new HmagFlyingGoal.MoveRandomGoal(this).heightLimit(10));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
	}
	
	/* Combat */
	public static final int ADD_POISON_LEVEL_DEFAULT = 1;
	public static final int ADD_POISON_TICKS_DEFAULT = 200;
	public int addPoisonLevel = 1;
	public int addPoisonTicks = 200;	// 10s, equal to hornet poisoning time in hard mode
	
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
					level.getDifficulty() == Difficulty.NORMAL ? 100 : (
					level.getDifficulty() == Difficulty.HARD ? 200 : 0);	
			// If the poison is no stronger than the super class given effect, remove it
			if (instance != null && instance.getAmplifier() <= 1 && instance.getDuration() <= superExpectedDuration)
			{
				living.getActiveEffectsMap().remove(MobEffects.POISON);
				instance = null;
			}
			// Add when don't have poison effect, or have lower level than this mob's adding level, or have the same level but with a shorter duration time 
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
		// Porting from 1.18-s7 & 1.19-s8 bug: missing owner uuid in nbt. Generally this shouldn't be called
		if (getOwner() == null)
			this.setOwner(player);
		// Porting solution end
		if (!player.isShiftKeyDown())
		{
			if (player.getUUID().equals(getOwnerUUID())) {
				if (!player.level.isClientSide()) 
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
							if (!player.addItem(new ItemStack(Items.GLASS_BOTTLE, 1)))
								player.spawnAtLocation(new ItemStack(Items.GLASS_BOTTLE, 1));
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					}
					// The function above returns PASS when the items are not correct. So when not PASS it should stop here
					else if (hand == InteractionHand.MAIN_HAND)
					{
						switchAIState();
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					}
					// Here it's main hand but no interaction. Return pass to enable off hand interaction.
					else return InteractionResult.PASS;
				}
				// Interacted
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			} 
			else return InteractionResult.PASS;
		}
		
		else
		{
			if (player.getUUID().equals(getOwnerUUID())) {
				// Open inventory and GUI
				BefriendedHelper.openBefriendedInventory(player, this);
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			return InteractionResult.PASS;
		}
	}
	
	@Deprecated
	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {
		return false;
	}

	@Deprecated
	@Override
	public boolean onInteractionShift(Player player, InteractionHand hand) {
		return false;
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
		if (!this.level.isClientSide) {
			additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuHandItemsTwoBaubles(containerId, playerInventory, container, this);
	}

	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt, BefriendMobs.MOD_ID);
		// Add other data to save here
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt, BefriendMobs.MOD_ID);
		// Add other data reading here
		setInit();
	}

	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(EntityBefriendedHornet.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(EntityBefriendedHornet.class, EntityDataSerializers.BYTE);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_OWNERUUID, Optional.empty());
		entityData.define(DATA_AISTATE, (byte) 0);
	}
	
	/* IBaubleHolder interface */
	
	@Override
	public HashSet<ItemStack> getBaubleStacks() {
		HashSet<ItemStack> stacks = new HashSet<ItemStack>();
		stacks.add(this.getAdditionalInventory().getItem(2));
		stacks.add(this.getAdditionalInventory().getItem(3));
		return stacks;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}

	protected HashMap<AttributeModifier, Attribute> existingBaubleModifiers = new HashMap<AttributeModifier, Attribute>();
	@Override
	public HashMap<AttributeModifier, Attribute> getExistingBaubleModifiers()
	{
		return existingBaubleModifiers;
	}
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //

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

	protected Vec3 anchorPos = new Vec3(0, 0, 0);	// This is not important as we initial it again in init()
	@Override
	public Vec3 getAnchorPos() {return anchorPos;}
	
	@Override
	public void setAnchorPos(Vec3 pos) {anchorPos = new Vec3(pos.x, pos.y, pos.z);}
	
	@Override
	public double getAnchoredStrollRadius()  {return 64.0d;}
	
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
