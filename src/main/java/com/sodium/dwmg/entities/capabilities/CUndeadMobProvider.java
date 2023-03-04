package com.sodium.dwmg.entities.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.sodium.dwmg.registries.ModCapabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CUndeadMobProvider implements ICapabilitySerializable<CompoundTag>
{

	private CUndeadMob capability = new CUndeadMobImpl();
	
	/* Interface implementations */
	
	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) 
	{
		if(cap == ModCapabilities.CAP_UNDEAD_MOB)
			return LazyOptional.of(() -> {return capability;}).cast();
		else
			return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() 
	{
		return capability.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		capability.deserializeNBT(nbt);
	}

}
