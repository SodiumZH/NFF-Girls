package net.sodiumstudio.befriendmobs.entity.befriended;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.sodiumstudio.nautils.capability.SerializableCapabilityProvider;

public class CBefriendedMobProvider extends SerializableCapabilityProvider<CompoundTag, CBefriendedMob>
{

	public CBefriendedMobProvider(Supplier<CBefriendedMob> capabilitySupplier, Capability<CBefriendedMob> holder)
	{
		super(capabilitySupplier, holder);
		// TODO Auto-generated constructor stub
	}

}
