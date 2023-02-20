package com.sodium.dwmg.entities;

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

	Player owner = null;
	boolean sunlightImmune = true;
	AIState aiState = AIState.WAIT;
	
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
		owner = player;
		return this;
	}

	@Override
	public Player getOwner()
	{
		return owner;
	}

	@Override
	public IBefriendedMob setOwner(Player owner) 
	{
		this.owner = owner;
		return this;
	}

	@Override
	public boolean setAiState(AIState to, AIState from) {
		aiState = to;
		return true;
	}

	@Override
	public void onRightClicked(Player player) {
		if(player == owner)
		{
			Debug.printToScreen("Friendly Zombie Girl right clicked", player, this);
		}
	}

	@Override
	public void onShiftRightClicked(Player player) {
		if(player == owner)
		{
			Debug.printToScreen("Friendly Zombie Girl shift + right clicked", player, this);
		}
		
	}
}
