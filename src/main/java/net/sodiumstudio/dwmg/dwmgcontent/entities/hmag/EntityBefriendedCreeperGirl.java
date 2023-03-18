package net.sodiumstudio.dwmg.dwmgcontent.entities.hmag;

import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.github.mechalopa.hmag.registry.ModItems;
import com.github.mechalopa.hmag.world.entity.CreeperGirlEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.network.NetworkHooks;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.BefriendedHelper;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.creeper.AbstractBefriendedCreeper;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AbstractInventoryMenuBefriended;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventoryWithEquipment;
import net.sodiumstudio.dwmg.befriendmobs.util.ItemHelper;
import net.sodiumstudio.dwmg.dwmgcontent.inventory.InventoryMenuCreeper;
import net.sodiumstudio.dwmg.befriendmobs.inventory.AdditionalInventoryWithEquipment;

// Rewritten from HMaG CreeperGirlEntity
public class EntityBefriendedCreeperGirl extends AbstractBefriendedCreeper
{

	private static final EntityDataAccessor<Integer> DATA_VARIANT_ID = 
			SynchedEntityData.defineId(EntityBefriendedCreeperGirl.class, EntityDataSerializers.INT);
	
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
	public void init(UUID playerUUID, Mob from)
	{
		super.init(playerUUID, from);
		if (from != null && from instanceof CreeperGirlEntity c)
			this.setVariant(c.getVariant());
		else if (from != null)
			this.setVariant(this.getRandom().nextInt(3));
	}
	
	public static Builder createAttributes() {
		return CreeperGirlEntity.createAttributes();
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
	public void tick()
	{
		if (additionalInventory.getItem(6).is(Items.GUNPOWDER))
			this.explosionRadius = 3;
		else if (additionalInventory.getItem(6).is(Items.TNT))
			this.explosionRadius = 4;
		else if (!additionalInventory.getItem(6).isEmpty())
			throw new IllegalStateException("Befriended Creeper Girl explosive type error");
		
		this.canExplode = !this.getAdditionalInventory().getItem(6).isEmpty();
		// Powered explosion requires 2 explosive items
		if (this.getAdditionalInventory().getItem(6).getCount() == 1 && this.isPowered())
			this.canExplode = false;
		this.canIgnite = this.canExplode;
		super.tick();
	}
	
	@Override
	public boolean onInteraction(Player player, InteractionHand hand)
	{
		if (player.getUUID().equals(getOwnerUUID()))
		{
			// Power with a lightning particle
			if (player.getItemInHand(hand).is(ModItems.LIGHTNING_PARTICLE.get()) && !this.isPowered())
			{
				if (!this.level.isClientSide)
				{
					this.setPowered(true);
					ItemHelper.consumeOne(player.getItemInHand(hand));
				}
			}
			// Unpower with empty hand and get a lightning particle
			else if (player.getItemInHand(hand).isEmpty() && this.isPowered())
			{
				if (!this.level.isClientSide)
				{
					this.setPowered(false);
					player.setItemInHand(hand, new ItemStack(ModItems.LIGHTNING_PARTICLE.get(), 1));
				}
			}
		}
		return super.onInteraction(player, hand);
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
