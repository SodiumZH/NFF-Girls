package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.StrayGirlEntity;

import net.minecraft.core.BlockPos;
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
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.sodiumstudio.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFollowOwnerGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRestrictSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.befriendmobs.registry.BefMobItems;
import net.sodiumstudio.befriendmobs.util.ItemHelper;
import net.sodiumstudio.befriendmobs.util.MiscUtil;
import net.sodiumstudio.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.ai.goals.BefriendedSkeletonMeleeAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.BefriendedSkeletonRangedBowAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.BefriendedSunAvoidingFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.inventory.InventoryMenuSkeleton;
import net.sodiumstudio.dwmg.registries.DwmgEntityTypes;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class EntityBefriendedStrayGirl extends StrayGirlEntity implements IBefriendedMob, IBefriendedUndeadMob, IBaubleHolder
{

	
	public EntityBefriendedStrayGirl(EntityType<? extends EntityBefriendedStrayGirl> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	public static Builder createAttributes() 
	{
		 return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 3.25D).add(Attributes.ARMOR, 1.0D);
	}

	// ------------------ Data sync ------------------ //

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(EntityBefriendedStrayGirl.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(EntityBefriendedStrayGirl.class, EntityDataSerializers.BYTE);

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
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedSkeletonRangedBowAttackGoal(this, 1.0D, 20, 15.0F));
		goalSelector.addGoal(4, new BefriendedSkeletonMeleeAttackGoal(this, 1.2d, true));
		goalSelector.addGoal(5, new BefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
				.avoidSunCondition(mob -> {return ((EntityBefriendedStrayGirl)mob).sunSensitive;}));
		goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
	}
	
	/* Bow shooting related */
	
	private boolean justShot = false;

	@Override
	public void performRangedAttack(LivingEntity pTarget, float pVelocity) {
		
		// Filter again to avoid it shoot without arrow
		if (this.getAdditionalInventory().getItem(8).isEmpty())
			return;
		
		// Copied from vanilla skeleton, removed difficulty factor
		ItemStack itemstack = this.getProjectile(this.getItemInHand(
				ProjectileUtil.getWeaponHoldingHand(this, item -> item instanceof net.minecraft.world.item.BowItem)));
		AbstractArrow abstractarrow = this.getArrow(itemstack, pVelocity);
		if (this.getMainHandItem().getItem() instanceof net.minecraft.world.item.BowItem)
			abstractarrow = ((net.minecraft.world.item.BowItem) this.getMainHandItem().getItem())
					.customArrow(abstractarrow);
		double d0 = pTarget.getX() - this.getX();
		double d1 = pTarget.getY(0.3333333333333333D) - abstractarrow.getY();
		double d2 = pTarget.getZ() - this.getZ();
		double d3 = Math.sqrt(d0 * d0 + d2 * d2);
		abstractarrow.shoot(d0, d1 + d3 * (double) 0.2F, d2, 1.6F, 2.0F);
		this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.level.addFreshEntity(abstractarrow);
		
		justShot = true;
	}

	@Override
	public void aiStep() {

		// Handle sun sensitivity
		if (!this.sunSensitive)
		{
			ItemStack headItem = this.getItemBySlot(EquipmentSlot.HEAD);
			this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(BefMobItems.DUMMY_ITEM.get()));
			super.aiStep();
			this.setItemSlot(EquipmentSlot.HEAD, headItem);
		}
		else 
		{
			super.aiStep();
		}
		
		/* Handle combat AI */
		if (justShot)
		{
			if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, this.getAdditionalInventory().getItem(4)) > 0)
				this.getAdditionalInventory().consumeItem(8);
			justShot = false;
		}
		
		if (this.getTarget() != null) {
			
			// Handle sun sensitivity
			if (!this.sunSensitive)
			{
				ItemStack headItem = this.getItemBySlot(EquipmentSlot.HEAD);
				this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(BefMobItems.DUMMY_ITEM.get()));
				super.aiStep();
				this.setItemSlot(EquipmentSlot.HEAD, headItem);
			}
			else 
			{
				super.aiStep();
			}
			
			/* Handle combat AI */			
			// When too close, switch to melee mode if possible
			if (this.distanceTo(this.getTarget()) < 2.5) {
				if (additionalInventory.getItem(4).is(Items.BOW) && additionalInventory.getItem(7).getItem() instanceof TieredItem) {
					additionalInventory.swapItem(4, 7);
					updateFromInventory();
				}
			}
			// When run out arrows, try taking weapon from backup-weapon slot
			if (additionalInventory.getItem(4).is(Items.BOW) && additionalInventory.getItem(7).getItem() instanceof TieredItem
					&& additionalInventory.getItem(8).isEmpty()) {
				additionalInventory.swapItem(4, 7);
				updateFromInventory();
			}
			// When too far and having a bow on backup-weapon, switch to bow mode
			// Don't switch if don't have arrows
			else if (this.distanceTo(this.getTarget()) > 4) {
				if (!additionalInventory.getItem(4).is(Items.BOW) && getAdditionalInventory().getItem(7).is(Items.BOW)
						&& !additionalInventory.getItem(8).isEmpty()) {
					additionalInventory.swapItem(4, 7);
					updateFromInventory();
				}
			}
			// When in melee mode without a weapon but having one on backup slot, change to it
			else if (!this.getInventoryItemStack(4).is(Items.BOW)
					&& !this.getInventoryItemStack(7).is(Items.BOW)
					&& (this.getInventoryItemStack(4).isEmpty() || !(this.getInventoryItem(4) instanceof TieredItem))
					&& !this.getInventoryItemStack(7).isEmpty()
					&& (this.getInventoryItem(7) instanceof TieredItem)
					)
			{
				additionalInventory.swapItem(4, 7);
				updateFromInventory();
			}
			
		}
	}
	
	/* Bow shooting end */
	
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
		// Porting from 1.18-s7 & 1.19-s8 bug: missing owner uuid in nbt. Generally this shouldn't be called
		if (getOwner() == null)
		{
			MiscUtil.printToScreen("Mob " + asMob().getName().getString() + " missing owner, set " + player.getName().getString() + " as owner.", player);
			this.setOwner(player);
		}
		// Porting solution end
		if (player.getUUID().equals(getOwnerUUID())) {
			if (!player.level.isClientSide() && hand == InteractionHand.MAIN_HAND) 
			{
				if (player.getItemInHand(hand).is(Items.FLINT_AND_STEEL) && isFromSkeleton)
				{
					// Use flint&steel
					this.level.playSound(player, this.getX(), this.getY(), this.getZ(), SoundEvents.FLINTANDSTEEL_USE,
							this.getSoundSource(), 1.0F, this.random.nextFloat() * 0.4F + 0.8F);
					if (!this.level.isClientSide)
					{
						player.getItemInHand(hand).hurtAndBreak(1, player, (p) ->
						{
							p.broadcastBreakEvent(hand);
						});
					}
					// and convert
					this.convertToSkeleton();
					return true;
				} 
				else if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
				{}
				else
					switchAIState();
			}
			return true;
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


	protected BefriendedInventoryWithEquipment additionalInventory = new BefriendedInventoryWithEquipment(getInventorySize());

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
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
			additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob() {
		if (!this.level.isClientSide) {
			additionalInventory.getFromMob(this);
		}
	}
	
	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuSkeleton(containerId, playerInventory, container, this);
	}
	
	/* Save and Load */

	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
		BefriendedHelper.addBefriendedCommonSaveData(this, nbt);
		nbt.put("is_from_skeleton", ByteTag.valueOf(isFromSkeleton));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag nbt) {
		super.readAdditionalSaveData(nbt);
		BefriendedHelper.readBefriendedCommonSaveData(this, nbt);
		isFromSkeleton = nbt.getBoolean("is_from_skeleton");
		setInit();
	}
	
	/* Conversion */
	
	public boolean isFromSkeleton = false;	

	public EntityBefriendedSkeletonGirl convertToSkeleton()
	{
		EntityBefriendedSkeletonGirl newMob = (EntityBefriendedSkeletonGirl)BefriendedHelper.convertToOtherBefriendedType(this, DwmgEntityTypes.HMAG_SKELETON_GIRL.get());
		newMob.setInit();
		return newMob;
	}
	
	/* IBefriendedUndeadMob interface */
	
	public boolean sunSensitive = true;
	
	// Implementation is in aiStep()
	@Override
	public void setSunSensitive(boolean value) {
		sunSensitive = value;		
	}
	
	/* IBaubleHolder interface */

	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(6));
		return map;
	}
	
	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.VANILLA_UNDEAD;
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
