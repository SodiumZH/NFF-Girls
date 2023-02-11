package com.sodium.dwmg.entities.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sodium.dwmg.registries.ModCapabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapUndeadMobProvider implements ICapabilityProvider, INBTSerializable<CompoundTag>
{

	private ICapUndeadMob capability = null;
	
	
	@Nonnull
	public ICapUndeadMob getOrCreateCapability()
	{
		if(capability == null)
		{
			capability = new CapUndeadMob();
		}
		return capability;
	}
	
	
	/* Interface implementations */
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) 
	{
		if(cap == ModCapabilities.CAP_UNDEAD_MOB)
			return LazyOptional.of(() -> {return this.getOrCreateCapability();}).cast();
		else
			return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() 
	{
		return capability != null ? capability.serializeNBT() : new CompoundTag();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		if(capability != null)
			capability.deserializeNBT(nbt);
	}

}
