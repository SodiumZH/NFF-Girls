package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.AlrauneEntity;

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
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedRangedAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedShootProjectileGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.entities.projectile.BefriendedAlrauneSeedEntity;
import net.sodiumstudio.dwmg.inventory.InventoryMenuThreeBaubles;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.containers.MapPair;

public class HmagAlrauneEntity extends AlrauneEntity implements IDwmgBefriendedMob {

	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(HmagAlrauneEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(HmagAlrauneEntity.class, EntityDataSerializers.INT);

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_OWNERUUID, Optional.empty());
		entityData.define(DATA_AISTATE, 0);
	}
	
	@Override
	public EntityDataAccessor<Optional<UUID>> getOwnerUUIDAccessor() {
		return DATA_OWNERUUID;
	}

	@Override
	public EntityDataAccessor<Integer> getAIStateData() {
		return DATA_AISTATE;
	}

	/* Initialization */

	public HmagAlrauneEntity(EntityType<? extends HmagAlrauneEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}
	
	/* AI */

	protected BefriendedMeleeAttackGoal meleeAttackGoal;
	
	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(3, meleeAttackGoal = new BefriendedMeleeAttackGoal(this, 1.2d, true));
		goalSelector.addGoal(4, new BefriendedRangedAttackGoal(this, 1.0D, 20, 15.0F));
		goalSelector.addGoal(4, new HmagAlrauneEntity.ShootHealingGoal(this, 1.0D, 10 * 20, 15.0F));
		goalSelector.addGoal(5, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		if (meleeAttackGoal != null)
		{
			if (this.getTarget() != null && this.hasLineOfSight(this.getTarget()) && this.distanceToSqr(this.getTarget()) >= 9d)
				meleeAttackGoal.block();
			else meleeAttackGoal.unblock();
		}
	}
	
	/* Combat */
	
	@Override
	public void performRangedAttack(LivingEntity target, float distanceFactor)
	{
		this.shootSeed(target, () -> new BefriendedAlrauneSeedEntity.PoisonSeed(level, this), distanceFactor);
	}
	
	public void shootSeed(LivingEntity target, Supplier<? extends BefriendedAlrauneSeedEntity> shotSupplier, float distanceFactor)
	{
		int c = this.getRandom().nextInt(3) + 1;

		for (int i = 0; i < c; ++i)
		{
			BefriendedAlrauneSeedEntity shot = shotSupplier.get();
			double d0 = target.getEyeY() - 1.1F;
			double d1 = target.getX() - this.getX();
			double d2 = d0 - shot.getY();
			double d3 = target.getZ() - this.getZ();
			double d4 = Math.sqrt(d1 * d1 + d3 * d3) * 0.15D;
			shot.shoot(d1, d2 + d4, d3, 1.5F, 10.0F);
			shot.setDamage(4.0F);
			this.level.addFreshEntity(shot);
		}

		this.playSound(SoundEvents.LLAMA_SPIT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.swing(InteractionHand.MAIN_HAND);
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HashMap<Item, Float> getHealingItems()
	{
		return ContainerHelper.mapOf(
			MapPair.of(Items.WHEAT_SEEDS, 2.0f),
			MapPair.of(Items.BONE_MEAL, 5.0f),
			MapPair.of(Items.SPORE_BLOSSOM, 15f),
			MapPair.of(ModItems.MYSTERIOUS_PETAL.get(), (float)this.getAttributeValue(Attributes.MAX_HEALTH)));
	}
	
	// Set of items that can heal the mob WITHOUT CONSUMING.
	// Leave it empty if not needed.
	@Override
	public HashSet<Item> getNonconsumingHealingItems()
	{
		HashSet<Item> set = new HashSet<Item>();
		// set.add(YOUR_ITEM_TYPE);
		return set;
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (player.getUUID().equals(getOwnerUUID())) {
			// For normal interaction
			if (!player.isShiftKeyDown())
			{
				if (!player.level.isClientSide()) 
				{
					/* Put checks before healing item check */
					/* if (....)
					 {
					 	....
					 }
					else */if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					// The function above returns PASS when the items are not correct. So when not PASS it should stop here
					else if (hand == InteractionHand.MAIN_HAND
							&& DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}
					// Here it's main hand but no interaction. Return pass to enable off hand interaction.
					else return InteractionResult.PASS;
				}
				// Interacted
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			// For interaction with shift key down
			else
			{
				// Open inventory and GUI
				if (hand == InteractionHand.MAIN_HAND && DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
				{
					BefriendedHelper.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
		} 
		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}
	
	/* Inventory */

	// This enables mob armor and hand items by default.
	// If not needed, use BefriendedInventory class instead.
	protected BefriendedInventory additionalInventory = new BefriendedInventory(getInventorySize(), this);

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	@Override
	public int getInventorySize()
	{
		return 3;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			//additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			//additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuThreeBaubles(containerId, playerInventory, container, this);
		// You can keep it null, but in this case never call openBefriendedInventory() or it will crash.
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

	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		return ContainerHelper.mapOf(
				MapPair.of("0", this.getAdditionalInventory().getItem(0)),
				MapPair.of("1", this.getAdditionalInventory().getItem(1)),
				MapPair.of("2", this.getAdditionalInventory().getItem(2)));
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}
	
	// Misc
	
	// Indicates which mod this mob belongs to
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

	protected static class ShootHealingGoal extends BefriendedShootProjectileGoal
	{
		
		public ShootHealingGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackInterval, float pAttackRadius)
		{
			super(mob, pSpeedModifier, pAttackInterval, pAttackRadius);
		}

		public ShootHealingGoal(IBefriendedMob mob, double pSpeedModifier, int pAttackIntervalMin,
				int pAttackIntervalMax, float pAttackRadius)
		{
			super(mob, pSpeedModifier, pAttackIntervalMin, pAttackIntervalMax, pAttackRadius);
		}

		@Override
		protected void performShooting(LivingEntity target, float velocity) 
		{
			((HmagAlrauneEntity)mob).shootSeed(target, () -> new BefriendedAlrauneSeedEntity.HealingSeed(mob.asMob().level, mob), velocity);
		}

		@Override
		protected LivingEntity updateTarget() {
			List<LivingEntity> visible = 
					mob.asMob().level.getEntitiesOfClass(LivingEntity.class, EntityHelper.getNeighboringArea(mob.asMob(), 8d))
					.stream().filter((LivingEntity living) -> mob.asMob().hasLineOfSight(living)).toList();
					
			List<LivingEntity> owner = visible.stream().filter((LivingEntity living) -> living.getUUID().equals(mob.getOwnerUUID())).toList();			
			if (!owner.isEmpty())
				return owner.get(0);			
			List<LivingEntity> allies = visible.stream().filter((LivingEntity living) -> BefriendedAlrauneSeedEntity.HealingSeed.canAffect(mob, living))
					.sorted(Comparator.comparingDouble((LivingEntity living) -> mob.asMob().distanceToSqr(living))).toList();
			if (!allies.isEmpty())
				return allies.get(0);
			else return null;
		}
		
	}
	
	
}
