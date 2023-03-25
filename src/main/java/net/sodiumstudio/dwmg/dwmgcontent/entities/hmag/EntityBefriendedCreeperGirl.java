package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.UUID;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedFollowOwnerGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedMeleeAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedWaterAvoidingRandomStrollGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.BefriendedZombieAttackGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedOwnerHurtByTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.ai.goal.vanilla.target.BefriendedOwnerHurtTargetGoal;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.vanillapreset.creeper.AbstractBefriendedCreeper;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.vanillapreset.creeper.BefriendedCreeperSwellGoal;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventoryWithEquipment;
import net.sodiumstudio.dwmg.befriendmobs.util.ItemHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.debug.Debug;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedCreeperFollowOwnerGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedCreeperGirlExplosionAttackGoal;
import net.sodiumstudio.dwmg.dwmgcontent.entities.ai.goals.BefriendedCreeperGirlMeleeAttackGoal;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuCreeper;

// Rewritten from HMaG CreeperGirlEntity
public class EntityBefriendedCreeperGirl extends AbstractBefriendedCreeper
{

	protected static final EntityDataAccessor<Integer> DATA_VARIANT_ID = 
			SynchedEntityData.defineId(EntityBefriendedCreeperGirl.class, EntityDataSerializers.INT);
	
	public boolean canAutoBlowEnemy = true;
	public int blowEnemyCooldown = 0;
	// If owner is closer than this distance, explosion will stop
	public double explodeSafeDistance = 3.0f;
	protected boolean isPlayerIgnited = false; 
	
	@Override
	protected void defineSynchedData()
	{
		super.defineSynchedData();
		this.entityData.define(DATA_VARIANT_ID, 0);
	}
	
	// Initialization
	
	public EntityBefriendedCreeperGirl(EntityType<? extends EntityBefriendedCreeperGirl> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);	
		additionalInventory = new AdditionalInventoryWithEquipment(7);
	}

	@Override
	protected void registerGoals() {
		this.goalSelector.addGoal(1, new FloatGoal(this));
		this.goalSelector.addGoal(2, new BefriendedCreeperSwellGoal(this));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Ocelot.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Cat.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.addGoal(3, new BefriendedCreeperGirlExplosionAttackGoal(this, 1.0D, false));
		this.goalSelector.addGoal(4, new BefriendedCreeperGirlMeleeAttackGoal(this, 1.0d, true));
		this.goalSelector.addGoal(5, new BefriendedCreeperFollowOwnerGoal(this, 1.0d, 5.0f, 2.0f, false));
		this.goalSelector.addGoal(6, new BefriendedWaterAvoidingRandomStrollGoal(this, 0.8D));
		this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
		this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
		this.targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		this.targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		this.targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));
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
	
	public static Builder createAttributes() {
		return CreeperGirlEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 0);
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
			if (additionalInventory.getItem(6).is(Items.GUNPOWDER))
			{
				this.explosionRadius = 3;
				this.shouldDestroyBlocks = false;
			}
			else if (additionalInventory.getItem(6).is(Items.TNT))
			{
				this.explosionRadius = 4;
				this.shouldDestroyBlocks = true;
			}
			else if (!additionalInventory.getItem(6).isEmpty())
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
	
	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {
		if (player.getUUID().equals(getOwnerUUID()))
		{
			if (!this.level.isClientSide)
			{
				// Power with a lightning particle
				if (player.getItemInHand(hand).is(ModItems.LIGHTNING_PARTICLE.get()) && !this.isPowered() && hand.equals(InteractionHand.MAIN_HAND))
				{
					this.setPowered(true);
					ItemHelper.consumeOne(player.getItemInHand(hand));
				}
				// Unpower with empty hand and get a lightning particle
				else if (player.getItemInHand(hand).isEmpty() && this.isPowered() && hand.equals(InteractionHand.MAIN_HAND))
				{
					this.setPowered(false);
					this.spawnAtLocation(new ItemStack(ModItems.LIGHTNING_PARTICLE.get(), 1));
				} 
				else if (player.getItemInHand(hand).is(Items.FLINT_AND_STEEL)
						&& this.canIgnite
						&& (!this.isPowered() || this.getAdditionalInventory().getItem(6).getCount() >= 2)
						&& this.getSwell() == 0)
				{
	
					this.playerIgniteDefault(player, hand);
					isPlayerIgnited = true;
				} 
				else if (hand == InteractionHand.MAIN_HAND)
				{
					switchAIState();
				}
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
	
	// Inventory
	
	@Override
	public int getInventorySize()
	{
		return 7;	// the 7rd slot is ammo slot ()
	}
	
	@Override
	public void updateFromInventory() {
		if (!this.level.isClientSide) {
			((AdditionalInventoryWithEquipment)additionalInventory).setMobEquipment(this);
		}
	}

	@Override
	public void setInventoryFromMob()
	{
		if (!this.level.isClientSide) {
			((AdditionalInventoryWithEquipment)additionalInventory).getFromMob(this);
		}
		return;
	}
	
	@Override
	public AbstractInventoryMenuBefriended makeMenu(int containerId, Inventory playerInventory, Container container)
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
			ItemHelper.consumeOne(this.getAdditionalInventory().getItem(6));
			if (this.isPowered())
			{
				ItemHelper.consumeOne(this.getAdditionalInventory().getItem(6));
				this.setPowered(false);
			}
			super.explodeCreeper();
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
	
	// Misc
	
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

	@Override
	protected void playStepSound(BlockPos pos, BlockState blockIn)
	{
		this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
	}

}
