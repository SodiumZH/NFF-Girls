package net.sodiumstudio.dwmg.befriendmobs.item.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.sodiumstudio.dwmg.befriendmobs.registry.BefMobCapabilities;

public class CMobRespawnerProvider implements ICapabilitySerializable<CompoundTag>
{

	protected CMobRespawner resp;
	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
		if(cap == BefMobCapabilities.CAP_MOB_RESPAWNER && resp != null)
			return LazyOptional.of(() -> {return this.resp;}).cast();
		else
			return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		return resp.serializeNBT();
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		resp.deserializeNBT(nbt);
	}

	public CMobRespawnerProvider(ItemStack stack)
	{
		resp = new CMobRespawnerImpl(stack);
	}
	
}
