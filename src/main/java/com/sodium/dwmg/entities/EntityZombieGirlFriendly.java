package com.sodium.dwmg.entities;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import javax.annotation.Nonnull;

import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;
import com.sodium.dwmg.entities.ai.BefriendedAIState;
import com.sodium.dwmg.entities.ai.goals.*;
import com.sodium.dwmg.entities.ai.goals.target.*;
import com.sodium.dwmg.util.Debug;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class EntityZombieGirlFriendly extends ZombieGirlEntity implements IBefriendedMob {

	public EntityZombieGirlFriendly(EntityType<? extends EntityZombieGirlFriendly> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
	    Arrays.fill(this.armorDropChances, 1f);
	    Arrays.fill(this.handDropChances, 1f);
	}

	@Override
	protected void registerGoals()
	{		
		goalSelector.addGoal(1, new BefriendedRestrictSunGoal(this));
		goalSelector.addGoal(2, new BefriendedFleeSunGoal(this, 1));
		goalSelector.addGoal(3, new BefriendedZombieAttackGoal(this, 1.0d, true));
		goalSelector.addGoal(6, new BefriendedFollowOwnerGoal(this, 1.0d, 10.0f, 2.0f, false));
		goalSelector.addGoal(7, new BefriendedWaterAvoidingRandomStrollGoal(this, 1.0d));
	    goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
	    goalSelector.addGoal(9, new RandomLookAroundGoal(this));
		targetSelector.addGoal(1, new BefriendedOwnerHurtByTargetGoal(this));
		targetSelector.addGoal(2, new BefriendedHurtByTargetGoal(this));
		targetSelector.addGoal(3, new BefriendedOwnerHurtTargetGoal(this));

	}
	
	public static Builder createAttributes() {
		return ZombieGirlEntity.createAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ARMOR, 4.0D);
	}
	
	@Override
	public IBefriendedMob init(@Nonnull UUID playerUUID, LivingEntity befriendedFrom) 
	{
		setOwnerUUID(playerUUID);
		if(!((LivingEntity)this).level.isClientSide() && befriendedFrom != null)
		{
			this.setHealth(befriendedFrom.getHealth());
		}
		return this;
	}
	
	// Adjusted from vanilla wolf
	@Override
	public boolean wantsToAttack(LivingEntity pTarget, LivingEntity pOwner)
	{
		// Don't attack creeper or ghast
		if (!(pTarget instanceof Creeper) && !(pTarget instanceof Ghast)) 
			return false;
		// For tamable mobs: attack untamed or (others' mobs if pvp allowed)
		else if (pTarget instanceof TamableAnimal tamable) 
			return !tamable.isTame() || (tamable.getOwner() != pOwner && ((Player)pOwner).canHarmPlayer((Player)(tamable.getOwner())));
		// For players: attack if pvp allowed
		else if (pTarget instanceof Player) 
			return ((Player)pOwner).canHarmPlayer((Player)pOwner);
		// For IBefriendedMob: similar to tamable mobs
		else if (pTarget instanceof IBefriendedMob bef) 
			return bef.getOwner() != pOwner && ((Player)pOwner).canHarmPlayer((Player)(bef.getOwner()));
		// For horses: attack untamed only
		else if (pTarget instanceof AbstractHorse && ((AbstractHorse) pTarget).isTamed()) 
			return false;
		// Can attack other
		else return true;
	}
	
	// ==================================================================== //
	// ========================= General Settings ========================= //
	// ==================================================================== //
	//  Generally these can be copy-pasted to other IBefriendedMob classes  //
	// ==================================================================== //
	
	// ------------------ Data sync ------------------ //

	protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID = SynchedEntityData.defineId(EntityZombieGirlFriendly.class, EntityDataSerializers.OPTIONAL_UUID);
	protected static final EntityDataAccessor<Byte> DATA_AISTATE = SynchedEntityData.defineId(EntityZombieGirlFriendly.class, EntityDataSerializers.BYTE);

	@Override
	protected void defineSynchedData() 
	{
		super.defineSynchedData();
		entityData.define(DATA_OWNERUUID, Optional.empty());
		entityData.define(DATA_AISTATE, (byte)0);	
	}
	
	// ------------------ Data sync end ------------------ //
	
	// ------------------ Serialization ------------------ //
	
	UUID playerUUID = null;
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt)
	{
	      super.addAdditionalSaveData(nbt);
	     playerUUID = this.getOwnerUUID();
	      if (this.getOwnerUUID() != null) 
	         nbt.putUUID("owner", this.getOwnerUUID());
	      else throw new IllegalStateException("Writing befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?");
	      
	      nbt.putByte("ai_state", this.getAIState().id());		
	}
	
	public void readAdditionalSaveData(CompoundTag nbt) {
	   super.readAdditionalSaveData(nbt);
	      
	   UUID uuid = nbt.getUUID("owner");
	   if (uuid == null)
	 	  throw new IllegalStateException("Reading befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?");      
	   setOwnerUUID(uuid);
	   init(getOwnerUUID(), null);
	   setAIState(BefriendedAIState.fromID(nbt.getByte("ai_state"))); 
	   }
	
	// ------------------ Serialization End ------------------ //
	   
	// ------------------ IBefriendedMob interface ------------------ //

	// Owner related
	@Override
	public Player getOwner()
	{
		return level.getPlayerByUUID(getOwnerUUID());
	}

	@Override
	public void setOwner(Player owner) 
	{
		entityData.set(DATA_OWNERUUID, Optional.of(owner.getUUID()));
	}

	@Override
	public UUID getOwnerUUID() 
	{
		return entityData.get(DATA_OWNERUUID).orElse(null);
	}

	@Override
	public void setOwnerUUID(UUID ownerUUID) {
		entityData.set(DATA_OWNERUUID, Optional.of(ownerUUID));
	}

	@Override
	public BefriendedAIState getAIState()
	{
		return BefriendedAIState.fromID(entityData.get(DATA_AISTATE));
	}
	
	@Override
	public void setAIState(BefriendedAIState state)
	{
		entityData.set(DATA_AISTATE, state.id());
	}
	
	@Override
	public BefriendedAIState switchAIState()
	{
		entityData.set(DATA_AISTATE, getAIState().defaultSwitch().id());
		return getAIState();
	}	
	
	protected LivingEntity PreviousTarget = null;
	
	@Override
	public LivingEntity getPreviousTarget()
	{
		return PreviousTarget;
	}
	
	@Override
	public void setPreviousTarget(LivingEntity target)
	{
		PreviousTarget = target;
	}
	
	@Override
	public boolean onInteraction(Player player, InteractionHand hand) {
		
		if(player.getUUID().equals(getOwnerUUID()))
		{
			if (player.level.isClientSide())
				Debug.printToScreen("Friendly Zombie Girl right clicked", player, this);
			else 
				{
					switchAIState();
					Debug.printToScreen(getAIState().toString(), player, this);
				}
			return true;
		}
		else 
			if (!player.level.isClientSide())
			{
				Debug.printToScreen("Owner UUID: " + getOwnerUUID(), player, this);
				Debug.printToScreen("Player UUID: "+ player.getUUID(), player, this);
			}
		return false;
		
	}
	
	@Override
	public boolean onInteractionShift(Player player, InteractionHand hand) {
		if(player.getUUID().equals(getOwnerUUID()))
		{
			if (!player.level.isClientSide())
				switchAIState();
			return true;
		}
		else
			Debug.printToScreen("Owner UUID: " + getOwnerUUID(), player, this);
		return false;
	}

	// ------------------ IBefriendedMob interface end ------------------ //
	
	// ------------------ Misc ------------------ //
	
	@Override
	public boolean isPreventingPlayerRest(Player pPlayer) {
	   return false;
	}

	// ========================= General Settings end ========================= //
	// ======================================================================== //
	
	
	
	
}
