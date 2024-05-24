package net.sodiumstudio.dwmg.entities.vanillatrade;

import java.util.function.Supplier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.capabilities.Capability;
import net.sodiumstudio.nautils.capability.SerializableCapabilityProvider;
import net.sodiumstudio.nautils.entity.vanillatrade.CVanillaMerchant;
import net.sodiumstudio.nautils.entity.vanillatrade.VanillaMerchantImpl;
import net.sodiumstudio.nautils.entity.vanillatrade.VanillaTradeRegistry;

public interface CDwmgTradeHandler extends CVanillaMerchant
{
	public static class Impl extends VanillaMerchantImpl implements CDwmgTradeHandler
	{
		public Impl(Mob mob)
		{
			super(mob);
		}
		
		@Override
		public void updateTrades(){
			for (int i = 1; i <= this.getXp(); ++i)
			{
				var trades = VanillaTradeRegistry.getTradesImmutable(this.getMob().getType(), getProfession(), i);
				for (ItemListing offer: trades)
				{
					this.getOffersRaw().add(offer.getOffer(getMob(), rnd));
				}
			}
		}

		@Override
		public int getLevel() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void onTrade(MerchantOffer offer) {
			// TODO Auto-generated method stub
			
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
