package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.INBTSerializable;
import net.sodiumstudio.dwmg.befriendmobs.util.EntityHelper;
import net.sodiumstudio.dwmg.befriendmobs.util.ItemHelper;

public interface CHealingHandler extends INBTSerializable<IntTag>
{

	public LivingEntity getOwner();

	public void setOwner(LivingEntity owner);
	
	@Override
	public default IntTag serializeNBT() 
	{
		IntTag tag = IntTag.valueOf(getCooldown());
		return tag;
	}
	
	@Override
	public default void deserializeNBT(IntTag tag)
	{
		this.setCooldown(tag.getAsInt());
	}
	
	public default boolean applyHealingItem(ItemStack stack, float value, boolean consume)
	{
		if (getOwner().getHealth() < getOwner().getMaxHealth() && getCooldown() == 0)
		{
			if (consume)
			{
				ItemHelper.consumeOne(stack);
			}
			getOwner().heal(value);
			sendParticlesOnSuccess();
			setCooldown(getHealingCooldownTicks());
			return true;
		}
		else
		{
			sendParticlesOnFailure();
			return false;
		}
	}
	
	public default void sendParticlesOnSuccess()
	{
		EntityHelper.sendGlintParticlesToLivingDefault(getOwner());
	}
	
	public default void sendParticlesOnFailure()
	{
		EntityHelper.sendSmokeParticlesToLivingDefault(getOwner());
	}
	
	// Get expected cooldown time each healing 
	public int getHealingCooldownTicks();
	
	// Get the current cooldown ticks
	public int getCooldown();
	
	public void setCooldown(int value);
	
	public default void updateCooldown()
	{
		if (getCooldown() > 0)
			setCooldown(getCooldown() - 1);
	}

}
