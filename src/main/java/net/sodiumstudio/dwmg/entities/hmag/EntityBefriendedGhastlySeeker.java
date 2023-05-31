package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.GhastlySeekerEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.LargeFireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.BefriendedGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedMeleeAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFollowOwnerGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithHandItems;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.util.ReflectHelper;
import net.sodiumstudio.befriendmobs.util.exceptions.UnimplementedException;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedFlyingFollowOwnerGoal;
import net.sodiumstudio.dwmg.befriendmobs.entity.ai.goal.preset.move.BefriendedFlyingRandomFloatGoal;
import net.sodiumstudio.dwmg.entities.DwmgBMStatics;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.HmagFlyingGoal;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.inventory.InventoryMenuGhastlySeeker;
import net.sodiumstudio.dwmg.inventory.InventoryMenuNecroticReaper;

/**
 * NOT IMPLEMENTED YET
 */

public class EntityBefriendedGhastlySeeker extends GhastlySeekerEntity implements IDwmgBefriendedMob
{


	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(EntityBefriendedGhastlySeeker.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(EntityBefriendedGhastlySeeker.class, EntityDataSerializers.INT);

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

	public EntityBefriendedGhastlySeeker(EntityType<? extends EntityBefriendedGhastlySeeker> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	public static Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 60.0D)
				.add(Attributes.ARMOR, 2.0D)
				.add(Attributes.ATTACK_DAMAGE, 0d)
				.add(Attributes.FOLLOW_RANGE, 64.0D);
	}

	public void setExplosionPower(int value)
	{
		ReflectHelper.forceSet(this, GhastlySeekerEntity.class, "explosionPower", value);
	}
	
	/* AI */

	@Override
	protected void registerGoals() {				
		this.goalSelector.addGoal(6, new BefriendedFlyingFollowOwnerGoal(this));
		this.goalSelector.addGoal(7, new FireballAttackGoal(this));
		this.goalSelector.addGoal(8, new BefriendedFlyingRandomFloatGoal(this));
		this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
		this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HashMap<Item, Float> getHealingItems()
	{
		return DwmgBMStatics.UNDEAD_DEFAULT_HEALING_ITEMS;
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
					else if (hand == InteractionHand.MAIN_HAND)
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
				BefriendedHelper.openBefriendedInventory(player, this);
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
		} 
		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}
	
	/* Inventory */

	// This enables mob armor and hand items by default.
	protected BefriendedInventory additionalInventory = new BefriendedInventory(getInventorySize(), this);

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	@Override
	public int getInventorySize()
	{
		return 5;	// 0-3 baubles, 4-fireball	
	}

	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			
		}
		return;
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuGhastlySeeker(containerId, playerInventory, container, this); 
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

	/** IBaubleHandler interface */

	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(0));
		map.put("1", this.getAdditionalInventory().getItem(1));
		map.put("2", this.getAdditionalInventory().getItem(2));
		map.put("3", this.getAdditionalInventory().getItem(3));
		return map;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.NECROTIC_REAPER;
	}

	
	// Misc
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return "dwmg";
	}
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// Generally these can be copy-pasted to other IBefriendedMob classes //

	@Override
	public boolean isPersistenceRequired() {
		return true;
	}

	/*@Override
	public boolean isPreventingPlayerRest(Player pPlayer) {
		return false;
	}*/

	@Override
	protected boolean shouldDespawnInPeaceful() {
		return false;
	}

	// ========================= General Settings end ========================= //
	// ======================================================================== //

	/*** AI Goals ***/
	
	public static class FireballAttackGoal extends BefriendedGoal implements HmagFlyingGoal
	{
		private final EntityBefriendedGhastlySeeker parent;
		public int attackTimer;

		public FireballAttackGoal(EntityBefriendedGhastlySeeker mob)
		{
			super(mob);
			this.parent = mob;
			this.allowAllStatesExceptWait();
		}

		@Override
		public boolean canUse()
		{
			// It consumes fire charges
			return this.parent.getTarget() != null && mob.getAdditionalInventory().getItem(4).is(Items.FIRE_CHARGE);
		}

		@Override
		public boolean canContinueToUse()
		{
			return canUse();
		}
		
		@Override
		public void start()
		{
			this.attackTimer = 0;
		}

		@Override
		public void stop()
		{
			ReflectHelper.forceInvoke(parent, GhastlySeekerEntity.class, "setAttackingTime", 
					Integer.class, -1);
		}

		@Override
		public void tick()
		{
			LivingEntity target = this.parent.getTarget();
			double d0 = 24.0D;

			if ((target.distanceToSqr(this.parent) < d0 * d0 || this.attackTimer > 10) && this.parent.hasLineOfSight(target))
			{
				Level world = this.parent.level;
				++this.attackTimer;

				if (this.attackTimer == 10 && !this.parent.isSilent())
				{
					world.levelEvent((Player)null, 1015, this.parent.blockPosition(), 0);
				}

				if (this.attackTimer == 20)
				{
					double d1 = 4.0D;
					Vec3 vec3 = this.parent.getViewVector(1.0F);
					double d2 = target.getX() - (this.parent.getX() + vec3.x * d1);
					double d3 = target.getY() + target.getEyeHeight() * 0.5D - this.parent.getY(0.5D) + 0.25D;
					double d4 = target.getZ() - (this.parent.getZ() + vec3.z * d1);

					if (!this.parent.isSilent())
					{
						world.levelEvent((Player)null, 1016, this.parent.blockPosition(), 0);
					}

					LargeFireball largefireball = new LargeFireball(world, this.parent, d2, d3, d4, this.parent.getExplosionPower());
					largefireball.setPos(this.parent.getX() + vec3.x * 0.5D, this.parent.getY(0.5D) + 0.25D, largefireball.getZ() + vec3.z * 0.5D);
					// Check again to prevent firing without ammo
					if (mob.getAdditionalInventory().getItem(4).is(Items.FIRE_CHARGE))
					{
						world.addFreshEntity(largefireball);
						mob.getAdditionalInventory().getItem(4).shrink(1);
					}
					this.attackTimer = -50;
				}
			}
			else if (this.attackTimer > 0)
			{
				--this.attackTimer;
			}
			ReflectHelper.forceInvoke(parent, GhastlySeekerEntity.class, "setAttackingTime", 
					Integer.class, this.attackTimer < 0 ? -1 : this.attackTimer);
		}
	}

	
}
