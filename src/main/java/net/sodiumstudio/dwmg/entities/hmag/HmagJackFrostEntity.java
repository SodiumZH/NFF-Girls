package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;
import com.github.mechalopa.hmag.world.entity.projectile.HardSnowballEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PlayMessages.SpawnEntity;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.befriendmobs.entity.capability.wrapper.ILivingDelayedActions;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedRangedAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoalLegacy;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoalLegacy;
import net.sodiumstudio.dwmg.events.hooks.DwmgHooks;
import net.sodiumstudio.dwmg.inventory.InventoryMenuFourBaubles;
import net.sodiumstudio.dwmg.registries.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.subsystem.baublesystem.DwmgBaubleStatics;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.function.MutablePredicate;
import net.sodiumstudio.nautils.math.GeometryUtil;
import net.sodiumstudio.nautils.math.RndUtil;

public class HmagJackFrostEntity extends JackFrostEntity implements IDwmgBefriendedMob, ILivingDelayedActions {

	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(HmagJackFrostEntity.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Integer> DATA_AISTATE = SynchedEntityData
			.defineId(HmagJackFrostEntity.class, EntityDataSerializers.INT);

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

	@Override
	public void onInit(UUID playerUUID, Mob from)
	{
		this.immuneToHotBiomes.putOptional("resistance_amulet", 
				jf -> DwmgBaubleStatics.countBaubles(jf, new ResourceLocation("dwmg:resistance_amulet")) > 0);
	}
	/* Initialization */

	public HmagJackFrostEntity(EntityType<? extends HmagJackFrostEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}
	
	/* Behavior */

	public final MutablePredicate<HmagJackFrostEntity> immuneToHotBiomes = new MutablePredicate<>();
	
	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(4, new DwmgBefriendedRangedAttackGoal(this, 1.0D, 3 * 20, 15.0F).setSkipChance(0.5d));
		goalSelector.addGoal(5, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(8, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));
	}
	
	protected HardSnowballEntity getNewSnowball()
	{
		return new BefriendedJackFrostSnowball(this.level(), this);
	}
	
	protected int getThrowLevel()
	{
		return this.getLevelHandler().getExpectedLevel() < 15 ? 0 : (
				this.getLevelHandler().getExpectedLevel() < 30 ? 1 : (
				this.getLevelHandler().getExpectedLevel() < 60 ? 2 : 3));
	}
	
	@Override
	public void performRangedAttack(LivingEntity target, float distance)
	{
		Consumer<Vec3> action = (offset) -> 
		{
			this.throwSnowballTo(target.getEyePosition().add(offset));
			this.playThrowingSound();
		};
		Runnable action1 = () -> action.accept(Vec3.ZERO);
		int i = getThrowLevel();
		switch (i)
		{
		case 0: 
		{
			action1.run();
			break;
		}
		case 1:
		{
			action1.run();
			this.addMultipleDelayedActions(action1, 4, 8);
			break;
		}
		case 2:
		{
			Runnable action2 = () -> {
				action1.run();
				for (int j = 0; j < 3; ++j)
					action.accept(GeometryUtil.randomVector().scale(RndUtil.rndRangedDouble(0, 2)));
			};
			action2.run();
			this.addMultipleDelayedActions(action2, 3, 6, 9, 12);
			break;
		}
		case 3:
		{
			Runnable action3 = () -> {
				action1.run();
				for (int j = 0; j < 6; ++j)
					action.accept(GeometryUtil.randomVector().scale(RndUtil.rndRangedDouble(0, 2)));
			};
			action3.run();
			this.addMultipleDelayedActions(action3, 3, 6, 9, 12, 15, 18);
			break;
		}
		default: 
		{
			throw new RuntimeException();
		}
		}
	}
	
	protected float getShootInaccuracy()
	{
		return 5f;
	}
	
	protected float getShootDamage()
	{
		return 3f + (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
	}
	
	public void throwSnowballTo(Vec3 targetPos)
	{
		HardSnowballEntity snowball = getNewSnowball();
		double deltaX = targetPos.x() - snowball.getX();
		double deltaY = targetPos.y() - snowball.getY();
		double deltaZ = targetPos.z() - snowball.getZ();
		double yOffset = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ) * 0.05D;
		snowball.shoot(deltaX, deltaY + yOffset, deltaZ, getShootSpeed(), getShootInaccuracy());
		snowball.setDamage(getShootDamage());
		this.level().addFreshEntity(snowball);
	}
	
	public void throwSnowball(Vec3 direction)
	{
		Vec3 n = direction.normalize();
		//double yOffset = Math.sqrt(n.x * n.x + n.z * n.z) * 0.1D;
		HardSnowballEntity snowball = getNewSnowball();
		snowball.shoot(n.x, n.y, n.z, getShootSpeed(), getShootInaccuracy());
		snowball.setDamage(getShootDamage());
		this.level().addFreshEntity(snowball);
	}
	
	public void playThrowingSound()
	{
		this.playSound(SoundEvents.SNOW_GOLEM_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
	}
	
	protected float getShootSpeed()
	{
		return 1.5f;
	}
	
	@SubscribeEvent
	public static void onCheckMeltingBiome(DwmgHooks.JackFrostCheckMeltingBiomeEvent event)
	{
		if (event.getEntity() instanceof HmagJackFrostEntity jf && jf.hasDwmgBauble("courage_amulet"))
			event.setCanceled(true);
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HealingItemTable getHealingItems()
	{
		return DwmgHealingItems.SNOWMAN;
	}

	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (player.getUUID().equals(getOwnerUUID())) {
			// For normal interaction
			if (!player.isShiftKeyDown())
			{
				if (!player.level().isClientSide()) 
				{
					/* Put checks before healing item check */
					/* if (....)
					 {
					 	....
					 }
					else */if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level().isClientSide);
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
				return InteractionResult.sidedSuccess(player.level().isClientSide);
			}
			// For interaction with shift key down
			else
			{
				// Open inventory and GUI
				if (hand == InteractionHand.MAIN_HAND && DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
				{
					BefriendedHelper.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level().isClientSide);
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
		return 4;
	}

	@Override
	public void updateFromInventory() {
		if (!this.level().isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			//additionalInventory.setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level().isClientSide) {
			// Sync inventory with mob equipments. If it's not BefriendedInventoryWithEquipment, remove it
			//additionalInventory.getFromMob(this);
		}
		return;
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuFourBaubles(containerId, playerInventory, container, this);
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
/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		return this.continuousBaubleSlots(0, 4);
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.GENERAL;
	}*/

	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.generalAmbient(super.getAmbientSound());
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

	public static class BefriendedJackFrostSnowball extends HardSnowballEntity
	{

		public BefriendedJackFrostSnowball(Level level, LivingEntity thrower)
		{
			super(level, thrower);
		}

		@Override
		public void onHitEntity(EntityHitResult result)
		{
			if (this.getOwner() instanceof HmagJackFrostEntity jf)
			{
				if (jf == result.getEntity())
					return;
				if (jf.isOwnerPresent() && jf.getOwnerUUID().equals(result.getEntity().getUUID()))
					return;
				if (result.getEntity() instanceof IBefriendedMob bm && bm.getOwnerUUID().equals(jf.getOwnerUUID()))
					return;
				if (result.getEntity() instanceof TamableAnimal ta && ta.isTame() && ta.getOwnerUUID().equals(jf.getOwnerUUID()))
					return;
			}
			super.onHitEntity(result);
		}
	}

}


