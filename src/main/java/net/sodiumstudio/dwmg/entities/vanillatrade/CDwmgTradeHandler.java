package net.sodiumstudio.dwmg.entities.vanillatrade;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.capabilities.Capability;
import net.sodiumstudio.nautils.capability.SerializableCapabilityProvider;
import net.sodiumstudio.nautils.entity.vanillatrade.CVanillaMerchant;

public interface CDwmgTradeHandler extends CVanillaMerchant
{
	public static class Impl extends CVanillaMerchant.Impl implements CDwmgTradeHandler
	{
		public Impl(Mob mob)
		{
			super(mob);
		}
	}
	
	public static class Prvd extends SerializableCapabilityProvider<CompoundTag, CDwmgTradeHandler>
	{

		public Prvd(Mob mob, Capability<CDwmgTradeHandler> holder)
		{
			super(() -> new Impl(mob), holder);
		}
		
	}
	
}
