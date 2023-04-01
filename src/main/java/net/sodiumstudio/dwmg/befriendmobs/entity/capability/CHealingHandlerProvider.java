package net.sodiumstudio.dwmg.befriendmobs.entity.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;

public class CHealingHandlerProvider implements ICapabilitySerializable<IntTag>
{

	protected CHealingHandler handler;
	
	public CHealingHandlerProvider(CHealingHandler handler, LivingEntity owner)
	{
		this.handler = handler;
		handler.setOwner(owner);
	}
	
	public CHealingHandlerProvider(LivingEntity owner)
	{
		this(new CHealingHandlerImplDefault(owner), owner);
	}
	
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		if(cap == BefMobCapabilities.CAP_HEALING_HANDLER && handler != null)
			return LazyOptional.of(() -> {return this.handler;}).cast();
		else
			return LazyOptional.empty();
	}

	@Override
	public IntTag serializeNBT() {
		return handler.serializeNBT();
	}

	@Override
	public void deserializeNBT(IntTag nbt) {
		handler.deserializeNBT(nbt);
	}
	
}
