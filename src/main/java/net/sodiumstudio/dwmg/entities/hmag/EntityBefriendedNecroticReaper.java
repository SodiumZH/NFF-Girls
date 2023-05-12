package net.sodiumstudio.dwmg.entities.hmag;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.NecroticReaperEntity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.entity.ai.IBefriendedUndeadMob;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedZombieAttackGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFleeSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedFollowOwnerGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedRestrictSunGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithHandItems;
import net.sodiumstudio.befriendmobs.item.baublesystem.BaubleHandler;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.befriendmobs.registry.BefMobItems;
import net.sodiumstudio.befriendmobs.template.TemplateBefriendedMobPreset;
import net.sodiumstudio.befriendmobs.util.InfoHelper;
import net.sodiumstudio.befriendmobs.util.MiscUtil;
import net.sodiumstudio.befriendmobs.util.exceptions.UnimplementedException;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleHandlers;
import net.sodiumstudio.dwmg.inventory.InventoryMenuNecroticReaper;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class EntityBefriendedNecroticReaper extends NecroticReaperEntity implements IBefriendedMob, IBaubleHolder, IBefriendedUndeadMob
{

	/* Data sync */

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData
			.defineId(EntityBefriendedNecroticReaper.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData
			.defineId(EntityBefriendedNecroticReaper.class, EntityDataSerializers.BYTE);

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

	/* Initialization */

	public EntityBefriendedNecroticReaper(EntityType<? extends EntityBefriendedNecroticReaper> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
		Arrays.fill(this.armorDropChances, 0);
		Arrays.fill(this.handDropChances, 0);
	}

	public static Builder createAttributes() {
		return Monster.createMonsterAttributes()
				.add(Attributes.MAX_HEALTH, 60.0D)
				.add(Attributes.MOVEMENT_SPEED, 0.31D)
				.add(Attributes.ATTACK_DAMAGE, 9.0D)
				.add(Attributes.ARMOR, 5.0D)
				.add(Attributes.KNOCKBACK_RESISTANCE, 0.25D)
				.add(Attributes.FOLLOW_RANGE, 24.0D);
	}

	/* AI */

	@Override
	protected void registerGoals() {
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0d, true));
		// To control a NecroticReaper player must have a Necromancer's Hat or a Necromancer's Wand
		goalSelector.addGoal(4, new BefriendedFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false)
		{
			@Override
			public boolean canUse()
			{
				return super.canUse() && ((EntityBefriendedNecroticReaper)mob).controllable();
			}
		}
				.avoidSunCondition(mob -> {return ((EntityBefriendedNecroticReaper)mob).isSunSensitive;}));
		goalSelector.addGoal(5, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d)
		{
			@Override
			public boolean canUse()
			{
				return super.canUse() 
						&& !(((EntityBefriendedNecroticReaper)mob).controllable() && mob.getAIState() == BefriendedAIState.FOLLOW);
			}
		}
		.allowState(BefriendedAIState.FOLLOW));		
		goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
		goalSelector.addGoal(7, new RandomLookAroundGoal(this));

		// Without a Necromancer's Hat, NecroticReaper will not 
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this)
		{
			@Override
			public boolean canUse()
			{
				return super.canUse() && ((EntityBefriendedNecroticReaper)mob).controllable();
			}
		});
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this)
		{
			@Override
			public boolean canUse()
			{
				return super.canUse() && ((EntityBefriendedNecroticReaper)mob).controllable();
			}
		});
	}
	
	@Override
	public void aiStep()
	{
		if (!isSunSensitive)
		{
			ItemStack head = this.getItemBySlot(EquipmentSlot.HEAD);
			this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(BefMobItems.DUMMY_ITEM.get()));
			super.aiStep();
			this.setItemSlot(EquipmentSlot.HEAD, head);
		}
		else
		{
			super.aiStep();
		}
	}
	
	/* Combat */
	@SubscribeEvent
	public static void onAttack(LivingHurtEvent event)
	{/*
		if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof EntityBefriendedNecroticReaper nr)
		{
			if (!nr.getAdditionalInventory().getItem(0).isEmpty() && nr.getAdditionalInventory().getItem(0).getItem() instanceof HoeItem hoe)
			{
				if (nr.level.random.nextDouble() < 1d / EnchantmentHelper.getItemEnchantmentLevel(Enchantments., null))
			}
		}*/
	}
	
	/* Interaction */

	// Map items that can heal the mob and healing values here.
	// Leave it empty if you don't need healing features.
	@Override
	public HashMap<Item, Float> getHealingItems()
	{
		HashMap<Item, Float> map = new HashMap<Item, Float>();
		map.put(ModItems.SOUL_POWDER.get(), 5f);
		map.put(ModItems.SOUL_APPLE.get(), 15f);
		return map;
	}
	
	// Set of items that can heal the mob WITHOUT CONSUMING.
	// Leave it empty if not needed.
	@Override
	public HashSet<Item> getNonconsumingHealingItems()
	{
		HashSet<Item> set = new HashSet<Item>();
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
						if (this.controllable())
						{
							switchAIState();
						}
						else
						{
						MiscUtil.printToScreen(InfoHelper.createTrans("info.dwmg.necrotic_reaper_not_controllable"), getOwner());
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
				// Open inventory and GUI
				if (this.controllable())
				{
					BefriendedHelper.openBefriendedInventory(player, this);					
				}
				else
				{
					MiscUtil.printToScreen(InfoHelper.createTrans("info.dwmg.necrotic_reaper_not_controllable_inventory"), player);
				}
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
		} 
		// Always pass when not owning this mob
		return InteractionResult.PASS;
	}
	
	/* Inventory */

	// This enables mob armor and hand items by default.
	// If not needed, use BefriendedInventory class instead.
	protected BefriendedInventoryWithHandItems additionalInventory = new BefriendedInventoryWithHandItems(getInventorySize(), this);

	@Override
	public BefriendedInventory getAdditionalInventory()
	{
		return additionalInventory;
	}
	
	@Override
	public int getInventorySize()
	{
		return 6;
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
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container) {
		return new InventoryMenuNecroticReaper(containerId, playerInventory, container, this);
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

	// IBaubleHolder interface
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
	
	// IBefriendedUndeadMob interface
	
	protected boolean isSunSensitive = true;
	@Override
	public void setSunSensitive(boolean value) {
		isSunSensitive = value;
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
	
	// Util
	// If player can control this mob
	public boolean controllable()
	{
		return this.getOwner().getItemBySlot(EquipmentSlot.HEAD).is(DwmgItems.NECROMANCER_HAT.get())
				|| this.getOwner().getItemBySlot(EquipmentSlot.MAINHAND).is(DwmgItems.NECROMANCER_WAND.get())
				|| this.getOwner().getItemBySlot(EquipmentSlot.OFFHAND).is(DwmgItems.NECROMANCER_WAND.get());
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
			if (e instanceof EntityBefriendedNecroticReaper nr
					&& nr.getOwnerUUID().equals(player.getUUID())
					&& nr.distanceToSqr(player) <= 64d)
			{
				count++;
			}
		}
		return count;
	}
	
}
