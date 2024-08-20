package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.CursedDollEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedZombieAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRestrictSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithHandItems;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedSunSensitiveMob;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToOwnerTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgNearestHostileToSelfTargetGoal;
import net.sodiumstudio.dwmg.inventory.InventoryMenuHandItemsSixBaubles;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.nautils.NaParticleUtils;

public class HmagCursedDollEntity extends CursedDollEntity implements IDwmgBefriendedSunSensitiveMob {

	/* Initialization */

	public HmagCursedDollEntity(EntityType<? extends HmagCursedDollEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		
		Arrays.fill(this.handDropChances, 0);
	}
	@Override
	public void onInit(UUID playerUUID, Mob from)
	{
		if (from instanceof CursedDollEntity c)
		{
			this.setVariant(c.getVariant());
		}
	}
	
	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(4, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
				.avoidSunCondition(DwmgEntityHelper::isSunSensitive));
		goalSelector.addGoal(5, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new DwmgBefriendedOwnerHurtTargetGoal(this));
		targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));
	}
	
	@Override
	public void aiStep()
	{
		super.aiStep();
		if (enhancingCooldown > 0)
			enhancingCooldown--;
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HealingItemTable getHealingItems()
	{
		return DwmgHealingItems.CLOTH_DOLL;

	}

	protected int enhancingCooldown = 0;
	protected static final HashSet<Item> ENHANCING_ITEMS = NaContainerUtils.setOf(
			Items.ORANGE_WOOL, Items.YELLOW_WOOL, Items.RED_WOOL, Items.PURPLE_WOOL,
			Items.MAGENTA_WOOL, Items.LIGHT_BLUE_WOOL, Items.BLUE_WOOL, Items.PINK_WOOL,
			Items.CYAN_WOOL, Items.LIME_WOOL, Items.GREEN_WOOL);
	
	/**
	 * Some colors of wool can give the mob specific effects.
	 * orange - Fire resistance 30s
	 * yellow - Glowing 30s
	 * red - Strength 30s
	 * purple - Strength II 10s
	 * lime - Regeneration I 30s
	 * green - Regeneration II 10s
	 * light blue - Speed I 30s
	 * blue - Speed II 15s
	 * cyan - Speed III 15s, Weakness I 15s
	 * magenta - Strength III 15s, Slowness I 15s
	 * pink - remove negative effects
	 * @return Whether handled.
	 */
	protected boolean giveEnhancingItems()
	{
		if (level().isClientSide)
			return false;
		ItemStack handItem = getOwner().getMainHandItem();	
		if (ENHANCING_ITEMS.contains(handItem.getItem()))
		{
			if (enhancingCooldown > 0)
			{
				NaParticleUtils.sendSmokeParticlesToEntityDefault(this);
				return true;
			}
			else
			{
				if (handItem.is(Items.ORANGE_WOOL))
					this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 30 * 20));
				else if (handItem.is(Items.YELLOW_WOOL))
					EntityHelper.addEffectSafe(this, MobEffects.GLOWING, 30 * 20);
				else if (handItem.is(Items.RED_WOOL))
					EntityHelper.addEffectSafe(this, MobEffects.DAMAGE_BOOST, 30 * 20);
				else if (handItem.is(Items.PURPLE_WOOL))
					EntityHelper.addEffectSafe(this, MobEffects.DAMAGE_BOOST, 10 * 20, 1);
				else if (handItem.is(Items.LIME_WOOL))
					EntityHelper.addEffectSafe(this, MobEffects.REGENERATION, 30 * 20);
				else if (handItem.is(Items.GREEN_WOOL))
					EntityHelper.addEffectSafe(this, MobEffects.REGENERATION, 10 * 20, 1);
				else if (handItem.is(Items.LIGHT_BLUE_WOOL))
					EntityHelper.addEffectSafe(this, MobEffects.MOVEMENT_SPEED, 30 * 20);
				else if (handItem.is(Items.BLUE_WOOL))
					EntityHelper.addEffectSafe(this, MobEffects.MOVEMENT_SPEED, 10 * 20, 1);
				else if (handItem.is(Items.CYAN_WOOL))
				{
					EntityHelper.addEffectSafe(this, MobEffects.MOVEMENT_SPEED, 15 * 20, 2);
					EntityHelper.addEffectSafe(this, MobEffects.WEAKNESS, 15 * 20);
				}
				else if (handItem.is(Items.MAGENTA_WOOL))
				{
					EntityHelper.addEffectSafe(this, MobEffects.DAMAGE_BOOST, 15 * 20, 2);
					EntityHelper.addEffectSafe(this, MobEffects.WEAKNESS, 15 * 20);
				}
				else if (handItem.is(Items.PINK_WOOL))
				{
					HashSet<MobEffect> removal = new HashSet<>();
					for (MobEffect effect: this.getActiveEffectsMap().keySet())
					{
						if (effect.getCategory() == MobEffectCategory.HARMFUL)
							removal.add(effect);
					}
					for (MobEffect effect: removal)
					{
						this.removeEffect(effect);
					}
				}
				else throw new IllegalArgumentException("Missing enhancing wool type.");
				getOwner().getMainHandItem().shrink(1);
				enhancingCooldown = 15 * 20;
				return true;
			}
		}
		else return false;
	}
	
	@SuppressWarnings("resource")
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
					if (giveEnhancingItems())
						return InteractionResult.sidedSuccess(player.level().isClientSide);
					else if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
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

	@Override
	public BefriendedInventory createAdditionalInventory()
	{
		return new BefriendedInventoryWithHandItems(8, this);
	}
	
	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuHandItemsSixBaubles(containerId, playerInventory, container, this);
	}

	/* Save and Load */
	
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
		return this.continuousBaubleSlots(2, 8);
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.UNDEAD;
	}
	*/
	// IBefriendedSunSensitiveMob interface

	// Misc
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return Dwmg.MOD_ID;
	}
	
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.generalAmbient(super.getAmbientSound());
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
