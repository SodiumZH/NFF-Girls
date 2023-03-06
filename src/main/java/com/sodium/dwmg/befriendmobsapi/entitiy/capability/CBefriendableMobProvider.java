package com.sodium.dwmg.befriendmobsapi.entitiy.capability;

import javax.annotation.Nonnull;

import com.sodium.dwmg.befriendmobsapi.registry.RegCapabilities;
import com.sodium.dwmg.registries.ModCapabilities;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CBefriendableMobProvider implements ICapabilitySerializable<CompoundTag> {

	private CBefriendableMob capability = new CBefriendableMobImpl();
	
	
	
	@Override
	public CompoundTag serializeNBT()
	{
		return capability != null ? capability.serializeNBT() : new CompoundTag();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) 
	{
		capability.deserializeNBT(nbt);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		if(cap == RegCapabilities.CAP_BEFRIENDABLE_MOB)
			return LazyOptional.of(() -> {return this.capability;}).cast();
		else
			return LazyOptional.empty();
	}

}
