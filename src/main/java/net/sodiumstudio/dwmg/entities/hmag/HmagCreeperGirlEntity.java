package net.sodiumstudio.dwmg.entities.hmag;

import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.Ocelot;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.BefriendedBlockActionGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.move.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.befriendmobs.entity.ai.goal.preset.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.capability.HealingItemTable;
import net.sodiumstudio.befriendmobs.entity.vanillapreset.creeper.AbstractBefriendedCreeper;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventory;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryMenu;
import net.sodiumstudio.befriendmobs.inventory.BefriendedInventoryWithEquipment;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.entities.ai.goals.BefriendedCreeperGirlExplosionAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.BefriendedCreeperGirlMeleeAttackGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.DwmgBefriendedCreeperFollowOwnerGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.entities.ai.goals.target.DwmgBefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.inventory.InventoryMenuCreeper;
import net.sodiumstudio.dwmg.registries.DwmgHealingItems;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.sounds.DwmgSoundPresets;
import net.sodiumstudio.dwmg.util.DwmgEntityHelper;
import net.sodiumstudio.nautils.math.LinearColor;

// Rewritten from HMaG CreeperGirlEntity
public class HmagCreeperGirlEntity extends AbstractBefriendedCreeper implements IDwmgBefriendedMob
{

	protected static final EntityDataAccessor<Integer> DATA_VARIANT_ID = 
			SynchedEntityData.defineId(HmagCreeperGirlEntity.class, EntityDataSerializers.INT);
	
	public boolean canAutoBlowEnemy = true;
	public int blowEnemyCooldown = 0;
	// If owner is closer than this distance, explosion will stop
	public double explodeSafeDistance = 3.0f;
	protected boolean isPlayerIgnited = false; 

	// Initialization
	
	public HmagCreeperGirlEntity(EntityType<? extends HmagCreeperGirlEntity> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);	
	}
	
	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		entityData.define(DATA_VARIANT_ID, 0);
	}
	
	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new BefriendedBlockActionGoal(this)
				{
					@Override
					public boolean blockCondition()
					{return ((HmagCreeperGirlEntity)mob).getSwell() > 0;}
				});
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(3, new BefriendedCreeperGirlExplosionAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(4, new BefriendedCreeperGirlMeleeAttackGoal(this, 1.0d, true));
		this.goalSelector.addGoal(5, new DwmgBefriendedCreeperFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		this.goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new DwmgBefriendedOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new DwmgBefriendedOwnerHurtTargetGoal(this));
	}
	
	@Override
	public void init(UUID playerUUID, Mob from)
	{
		super.init(playerUUID, from);
		if (from != null && from instanceof CreeperGirlEntity c)
			this.setVariant(c.getVariant());
		else if (from != null)
			this.setVariant(this.getRandom().nextInt(3));
	}
	
	@Deprecated
	public static Builder createAttributes() {
		return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.KNOCKBACK_RESISTANCE, 0.25D).add(Attributes.ATTACK_DAMAGE, 0);
	}
	
	public int getVariant()
	{
		return this.entityData.get(DATA_VARIANT_ID);
	}

	public void setVariant(int typeIn)
	{
		if (typeIn < 0 || typeIn >= 3)
		{
			typeIn = this.getRandom().nextInt(3);
		}

		this.entityData.set(DATA_VARIANT_ID, typeIn);
	}
	
	@Override
	public void readAdditionalSaveData(CompoundTag compound)
	{
		super.readAdditionalSaveData(compound);
		this.setVariant(compound.getInt("Variant"));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound)
	{
		super.addAdditionalSaveData(compound);
		compound.putInt("Variant", this.getVariant());
	}

	// Interaction
	
	@Override
	public void aiStep()
	{
		if (!level.isClientSide)
		{
			// Update explosion radius by ammo type
			if (this.getAdditionalInventory().getItem(6).is(Items.GUNPOWDER))
			{
				this.explosionRadius = 3;
				this.shouldDestroyBlocks = false;
			}
			else if (this.getAdditionalInventory().getItem(6).is(Items.TNT))
			{
				this.explosionRadius = 4;
				this.shouldDestroyBlocks = true;
			}
			else if (!this.getAdditionalInventory().getItem(6).isEmpty())
				throw new IllegalStateException("Befriended Creeper Girl explosive type error");		
			this.canExplode = !this.getAdditionalInventory().getItem(6).isEmpty();
			
			// Powered explosion requires 2 explosive items
			if (this.getAdditionalInventory().getItem(6).getCount() == 1 && this.isPowered())
				this.canExplode = false;
			this.canIgnite = this.canExplode && this.getSwell() == 0 && this.currentIgnitionCooldown == 0;
			if (this.getOwner() != null && this.distanceToSqr(this.getOwner()) < explodeSafeDistance * explodeSafeDistance && !this.isPlayerIgnited)
				this.setSwellDir(-1);
			if (blowEnemyCooldown > 0)
				blowEnemyCooldown --;
		}
		super.aiStep();
	}
	
	@Override
	public void tick()
	{
		super.tick();
		if (!level.isClientSide)
		{
			if (this.getSwell() == 0)
				isPlayerIgnited = false;
		}
	}
	
	/* Interaction */
	
	@Override
	public HealingItemTable getHealingItems()
	{
		return DwmgHealingItems.CREEPER;
	}
	
	@Override
	public InteractionResult mobInteract(Player player, InteractionHand hand)
	{
		if (!player.isShiftKeyDown())
		{
			if (player.getUUID().equals(getOwnerUUID()))
			{
				if (!this.level.isClientSide && hand == InteractionHand.MAIN_HAND)
				{
					if (player.getItemInHand(hand).is(Items.FLINT_AND_STEEL)
							&& this.canIgnite
							&& (!this.isPowered() || this.getAdditionalInventory().getItem(6).getCount() >= 2)
							&& this.getSwell() == 0)
					{
		
						this.playerIgniteDefault(player, hand);
						isPlayerIgnited = true;
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					} 
					else if (this.tryApplyHealingItems(player.getItemInHand(hand)) != InteractionResult.PASS)
						return InteractionResult.sidedSuccess(player.level.isClientSide);
					else if (hand == InteractionHand.MAIN_HAND
							&& DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
					{
						switchAIState();
					}	
					else return InteractionResult.PASS;
				}
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
			return InteractionResult.PASS;
		}
		else
		{
			if (player.getUUID().equals(getOwnerUUID())) {	
				// Power with a lightning particle
				if (player.getItemInHand(hand).is(ModItems.LIGHTNING_PARTICLE.get()) && !this.isPowered())
				{
					this.setPowered(true);
					player.getItemInHand(hand).shrink(1);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
				// Unpower with empty hand )and get a lightning particle
				else if (player.getItemInHand(hand).isEmpty() && this.isPowered() && hand.equals(InteractionHand.MAIN_HAND))
				{
					this.setPowered(false);
					this.spawnAtLocation(new ItemStack(ModItems.LIGHTNING_PARTICLE.get(), 1));
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				} 
				else if (hand == InteractionHand.MAIN_HAND && DwmgEntityHelper.isOnEitherHand(player, DwmgItems.COMMANDING_WAND.get()))
				{
					BefriendedHelper.openBefriendedInventory(player, this);
					return InteractionResult.sidedSuccess(player.level.isClientSide);
				}
			}
			return InteractionResult.PASS;
		}
	}
	
	// Inventory
	
	@Override
	public BefriendedInventory createAdditionalInventory() {
		return new BefriendedInventoryWithEquipment(7, this);
	}

	@Override
	public BefriendedInventoryMenu makeMenu(int containerId, Inventory playerInventory, Container container)
	{
		return new InventoryMenuCreeper(containerId, playerInventory, container, this);
	}
	
	// Combat
	
	@Override
	protected void explodeCreeper()
	{
		if (!level.isClientSide)
		{
			if (!hasEnoughAmmoToExplode())
			{
				this.resetExplosionProcess();
				return;
			}
			this.getAdditionalInventory().getItem(6).shrink(1);;
			if (this.isPowered())
			{
				this.getAdditionalInventory().getItem(6).shrink(1);
				this.setPowered(false);
			}
			super.explodeCreeper();
			if (isPlayerIgnited)
			{
				blowEnemyCooldown = 100;
			}
			else
			{
				blowEnemyCooldown = 300;
			}
			resetExplosionProcess();
			isPlayerIgnited = false;
			
		}
	}
	
	@Override
	public boolean doHurtTarget(Entity target)
	{
		return super.doHurtTarget(target);
	}
	
	public boolean hasEnoughAmmoToExplode()
	{
		return !(this.getAdditionalInventory().getItem(6).isEmpty() || this.getAdditionalInventory().getItem(6).getCount() == 1 && this.isPowered());
	}
	

	// IBaubleEquipable interface
	// Actually it doesn't have bauble
	/*
	@Override
	public HashMap<String, ItemStack> getBaubleSlots() {
		return new HashMap<String, ItemStack>();
	}

	@Override
	public BaubleHandler getBaubleHandler() {
		return DwmgBaubleHandlers.EMPTY;
	}
	*/
	// Sounds
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return DwmgSoundPresets.generalAmbient(super.getAmbientSound());
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
		this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
	}

	// Misc
	
	@Override
	public String getModId() {
		return Dwmg.MOD_ID;
	}
	
	@Override
	public double getMyRidingOffset()
	{
		return -0.45D;
	}

	@Override
	protected float getStandingEyeHeight(Pose poseIn, EntityDimensions sizeIn)
	{
		return 1.74F;
	}

}
