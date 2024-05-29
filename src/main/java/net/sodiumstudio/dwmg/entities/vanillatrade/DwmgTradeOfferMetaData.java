package net.sodiumstudio.dwmg.entities.vanillatrade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

public class DwmgTradeOfferMetaData
{
	// Minimum merchant level required to unlock this offer. Note: merchant level starts from 1.
	public int requiredMerchantLevel;
	// Cached value of usage. Offers locked due to merchant level will always out of stack which wipes the actual used count,
	// So store it here and read when the offer is available again.
	public int cachedUse;
	
	public DwmgTradeOfferMetaData(int requiredMerchantLevel, int cachedUse)
	{
		this.requiredMerchantLevel = requiredMerchantLevel;
		this.cachedUse = cachedUse;
	}
	
	public CompoundTag toTag()
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("required_merchant_level", requiredMerchantLevel);
		tag.putInt("cached_use", cachedUse);
		return tag;
	}
	
	public static DwmgTradeOfferMetaData fromTag(CompoundTag tag)
	{
		return new DwmgTradeOfferMetaData(tag.getInt("required_merchant_level"), tag.getInt("cached_use"));
	}
	
	public void writeToBuf(FriendlyByteBuf buf)
	{
		buf.writeInt(requiredMerchantLevel);
		buf.writeInt(cachedUse);
	}
	
	public static DwmgTradeOfferMetaData readBuf(FriendlyByteBuf buf)
	{
		int level = buf.readInt();
		int use = buf.readInt();
		return new DwmgTradeOfferMetaData(level, use);
	}
	
	@Override
	public String toString()
	{
		return String.format("DwmgTradeOfferMetaData{requiredMerchantLevel = %d, cachedUse = %d}", this.requiredMerchantLevel, this.cachedUse);
	}
}
