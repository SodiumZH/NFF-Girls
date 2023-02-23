package com.sodium.dwmg.entities;

import java.util.UUID;

import com.github.mechalopa.hmag.world.entity.ZombieGirlEntity;
import com.sodium.dwmg.entities.befriending.AIState;
import com.sodium.dwmg.util.Debug;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ZombieGirlFriendly extends ZombieGirlEntity implements IBefriendedMob {

	public ZombieGirlFriendly(EntityType<? extends ZombieGirlFriendly> pEntityType, Level pLevel) {
		super(pEntityType, pLevel);
		this.xpReward = 0;
	}

	protected UUID ownerUUID = null;
	//public boolean sunlightImmune = true;
	public AIState aiState = AIState.WAIT;
	
	@Override
	protected void registerGoals()
	{
	}
	
	public static Builder createAttributes() {
		return ZombieGirlEntity.createAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.MOVEMENT_SPEED, 0.28D).add(Attributes.ATTACK_DAMAGE, 4.0D).add(Attributes.ARMOR, 4.0D);
	}
	
	// IBefriendedMob interface
	
	@Override
	public IBefriendedMob init(Player player, LivingEntity befriendedFrom) 
	{
		ownerUUID = player.getUUID();
		if(!player.level.isClientSide())
		{
			this.setHealth(befriendedFrom.getHealth());
		}
		return this;
	}

	@Override
	public Player getOwner()
	{
		return ownerUUID == null ? null : this.level.getPlayerByUUID(ownerUUID);
	}

	@Override
	public IBefriendedMob setOwner(Player owner) 
	{
		this.ownerUUID = owner.getUUID();
		return this;
	}

	@Override
	public boolean onInteraction(Player player) {
		if(player.getUUID() == ownerUUID)
		{
			if (player.level.isClientSide())
				Debug.printToScreen("Friendly Zombie Girl right clicked", player, this);
			return true;
		}
		else return false;
	}

	@Override
	public boolean onInteractionShift(Player player) {
		if(player.getUUID() == ownerUUID)
		{
			if (player.level.isClientSide())
				Debug.printToScreen("Friendly Zombie Girl shift + right clicked", player, this);
			return true;
		}
		else return false;
	}

	@Override
	public UUID getOwnerUUID() {
		return ownerUUID == null ? null : ownerUUID;
	}
}
