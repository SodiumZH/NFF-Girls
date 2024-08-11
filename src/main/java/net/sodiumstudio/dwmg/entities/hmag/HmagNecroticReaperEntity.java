package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.NecroticReaperEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
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
import net.sodiumstudio.dwmg.inventory.InventoryMenuNecroticReaper;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.NaMiscUtils;

public class HmagNecroticReaperEntity extends NecroticReaperEntity implements IDwmgBefriendedSunSensitiveMob
{

	/* Initialization */

	public HmagNecroticReaperEntity(EntityType<? extends HmagNecroticReaperEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new FloatGoal(this));
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0d, true));
		// To control a NecroticReaper player must have a Necromancer's Hat or a Necromancer's Wand
		goalSelector.addGoal(4, new DwmgBefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
		{
			@Override
			public boolean checkCanUse()
			{
				return super.checkCanUse() && ((HmagNecroticReaperEntity)mob).controllable();
			}
		}
				.avoidSunCondition(DwmgEntityHelper::isSunSensitive));
		goalSelector.addGoal(5, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d)
		{
			@Override
			public boolean checkCanUse()
			{
				return super.checkCanUse() 
						&& !(((HmagNecroticReaperEntity)mob).controllable() && mob.getAIState() == BefriendedAIState.FOLLOW);
			}
		}
		.allowState(BefriendedAIState.FOLLOW));		
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));

		// Without a Necromancer's Hat, NecroticReaper will not 
		targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this)
		{
			@Override
			public boolean checkCanUse()
			{
				return super.checkCanUse() && ((HmagNecroticReaperEntity)mob).controllable();
			}
		});
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new DwmgBefriendedOwnerHurtTargetGoal(this)
		{
			@Override
			public boolean checkCanUse()
			{
				return super.checkCanUse() && ((HmagNecroticReaperEntity)mob).controllable();
			}
		});
		targetSelector.addGoal(5, new DwmgNearestHostileToSelfTargetGoal(this));
		targetSelector.addGoal(6, new DwmgNearestHostileToOwnerTargetGoal(this));
	}

	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HealingItemTable getHealingItems()
	{
		return DwmgHealingItems.UNDEAD;
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
					if (getOwner() == null)
					{
						throw new RuntimeException("Mob \"" + this.getName().getString() + "\" missing owner.");
					}
					if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					// The function above returns PASS when the items are not correct. So when not PASS it should stop here
					// Print a notice info when trying to use commanding wand
					else if (hand == InteractionHand.MAIN_HAND
							&& DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
					{
						NaMiscUtils.printToScreen(InfoHelper.createTranslatable("info.dwmg.necrotic_reaper_using_commanding_wand"), player);
					}
					else if (hand == InteractionHand.MAIN_HAND
							&& DwmgEntityHelper.isOnEitherHand(player, DwmgItems.NECROMANCER_WAND.get()))
					{
						if (this.controllable())
						{
							switchAIState();
						}
						else
						{
							NaMiscUtils.printToScreen(InfoHelper.createTranslatable("info.dwmg.necrotic_reaper_not_controllable"), getOwner());
						}
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
				if (player.getUUID().equals(getOwnerUUID())) {		
					if (hand == InteractionHand.MAIN_HAND && DwmgEntityHelper.isOnEitherHand(player, DwmgItems.NECROMANCER_WAND.get()))
					{
						BefriendedHelper.openBefriendedInventory(player, this);
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					}
				}
			}
		} 
		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}
	
	/* Inventory */

	@Override
	public BefriendedInventory createAdditionalInventory() {
		// TODO Auto-generated method stub
		return new BefriendedInventoryWithHandItems(6, this);
	}
	
	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuNecroticReaper(containerId, playerInventory, container, this);
	}

	/* Save and Load */
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt) {
		super.addAdditionalSaveData(nbt);
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
	// IBaubleEquipable interface
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		HashMap<String, ItemStack> map = new HashMap<String, ItemStack>();
		map.put("0", this.getAdditionalInventory().getItem(2));
		map.put("1", this.getAdditionalInventory().getItem(3));
		map.put("2", this.getAdditionalInventory().getItem(4));
		map.put("3", this.getAdditionalInventory().getItem(5));
		map.put("main_hand", this.getAdditionalInventory().getItem(0));
		return map;
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.NECROTIC_REAPER;
	}
	*/
	// IBefriendedSunSensitiveMob interface
	
	/*@Override
	public void setupSunImmunityRules() {
		this.getSunImmunity().putOptional("soul_amulet", mob -> ((IDwmgBefriendedMob)mob).hasDwmgBauble("soul_amulet"));
		this.getSunImmunity().putOptional("resis_amulet", mob -> ((IDwmgBefriendedMob)mob).hasDwmgBauble("resistance_amulet"));
	}*/
	// Misc
	
	// Indicates which mod this mob belongs to
	@Override
	public String getModId() {
		return Dwmg.MOD_ID;
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.zombieAmbient(super.getAmbientSound());
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSource)
	{
		return DwmgSoundPresets.zombieHurt(super.getHurtSound(damageSource));
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return DwmgSoundPresets.zombieDeath(super.getDeathSound());
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
	
	// Util
	// If player can control this mob
	public boolean controllable()
	{
		return this.getOwner().getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.NECROMANCER_HAT.get())
				|| this.getOwner().getItemBySlot(EquipmentSlot.MAINHAND).is(DwmgItems.NECROMANCER_WAND.get())
				|| this.getOwner().getItemBySlot(EquipmentSlot.OFFHAND).is(DwmgItems.NECROMANCER_WAND.get())
				|| this.getFavorabilityHandler().getFavorability() >= 90f;
	}
	
	// Get how many Necrotic Reapers is <8 blocks away from owner 
	public static int countNearby(Player player)
	{
		Vec3 center = player.position();
		AABB area = new AABB(center.x - 8d, center.y - 8d, center.z - 8d, center.x + 8d, center.y + 8d, center.z + 8d);
		List<Entity> entityList = player.level.getEntities(player, area);
		int count = 0;
		for (Entity e: entityList)
		{
			if (e instanceof HmagNecroticReaperEntity nr
					&& nr.getOwnerUUID() != null // On Necrotic Reaper befriended, a nullptr exception occured here
					&& nr.getOwnerUUID().equals(player.getUUID())
					&& nr.distanceToSqr(player) <= 64d)
			{
				count++;
			}
		}
		return count;
	}

}
