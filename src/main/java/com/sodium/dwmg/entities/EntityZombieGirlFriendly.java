package com.sodium.dwmg.entities;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;
import com.sodium.dwmg.entities.ai.BefriendedAIState;
import com.sodium.dwmg.entities.befriending.AIState;
import com.sodium.dwmg.util.Debug;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
	}
	
	public static Builder createAttributes() {
		return ZombieGirlEntity.createAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ARMOR, 4.0D);
	}
	
	@Override
	public IBefriendedMob init(Player player, LivingEntity befriendedFrom) 
	{
		setOwner(player);
		if(!player.level.isClientSide() && befriendedFrom != null)
		{
			this.setHealth(befriendedFrom.getHealth());
		}
		return this;
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
	
	@Override
	public void addAdditionalSaveData(CompoundTag nbt)
	{
	      super.addAdditionalSaveData(nbt);
	     
	      if (this.getOwnerUUID() != null && this.getOwner() != null) 
	         nbt.putUUID("owner", this.getOwnerUUID());
	      else throw new IllegalStateException("Writing befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?");
	      
	      nbt.putByte("ai_state", this.getAIState().id());		
	}
	
	public void readAdditionalSaveData(CompoundTag nbt) {
	   super.readAdditionalSaveData(nbt);
	      
	   UUID uuid = nbt.getUUID("owner");
	   if (level.getPlayerByUUID(uuid) == null)
	 	  throw new IllegalStateException("Reading befriended mob data error: invalid owner. Was IBefriendedMob.init() not called?");      
	   setOwnerUUID(uuid);
	   init(getOwner(), null);
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
	
	
	@Override
	public boolean onInteraction(Player player) {
		if(player.getUUID() == getOwnerUUID())
		{
			if (player.level.isClientSide())
				Debug.printToScreen("Friendly Zombie Girl right clicked", player, this);
			return true;
		}
		else return false;
	}
	
	@Override
	public boolean onInteractionShift(Player player) {
		if(player.getUUID() == getOwnerUUID())
		{
			if (!player.level.isClientSide())
				switchAIState();
			return true;
		}
		else return false;
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
