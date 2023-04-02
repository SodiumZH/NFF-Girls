package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import net.minecraft.world.entity.LivingEntity;

public abstract class CHealingHandlerImpl implements CHealingHandler
{

	LivingEntity owner;
	int cooldown = 0;
	
	public CHealingHandlerImpl(LivingEntity owner)
	{
		this.owner = owner;
	}
	
	@Override
	public LivingEntity getOwner() {
		return owner;
	}

	@Override
	public void setOwner(LivingEntity owner) {
		this.owner = owner;
	}	
	
	@Override
	public int getHealingCooldownTicks() 
	{
		return 40;
	}

	@Override
	public int getCooldown() {
		return cooldown;
	}

	@Override
	public void setCooldown(int value) {
		cooldown = value;
	}

}
