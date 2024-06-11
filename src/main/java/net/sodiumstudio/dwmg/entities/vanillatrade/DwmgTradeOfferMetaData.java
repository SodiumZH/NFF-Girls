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
	// Whether this meta should have input B. If it's true but the input B is missing, it will be regarded as invalid and removed.
	public boolean hasB = false;
	
	public DwmgTradeOfferMetaData(int requiredMerchantLevel, int cachedUse, boolean hasB)
	{
		this.requiredMerchantLevel = requiredMerchantLevel;
		this.cachedUse = cachedUse;
		this.hasB = hasB;
	}
	
	public CompoundTag toTag()
	{
		CompoundTag tag = new CompoundTag();
		tag.putInt("required_merchant_level", requiredMerchantLevel);
		tag.putInt("cached_use", cachedUse);
		tag.putBoolean("has_b", hasB);
		return tag;
	}
	
	public static DwmgTradeOfferMetaData fromTag(CompoundTag tag)
	{
		return new DwmgTradeOfferMetaData(tag.getInt("required_merchant_level"), tag.getInt("cached_use"), tag.getBoolean("has_b"));
	}
	
	public void writeToBuf(FriendlyByteBuf buf)
	{
		buf.writeInt(requiredMerchantLevel);
		buf.writeInt(cachedUse);
		buf.writeBoolean(hasB);
	}
	
	public static DwmgTradeOfferMetaData readBuf(FriendlyByteBuf buf)
	{
		int level = buf.readInt();
		int use = buf.readInt();
		boolean hasB = buf.readBoolean();
		return new DwmgTradeOfferMetaData(level, use, hasB);
	}
	
	@Override
	public String toString()
	{
		return String.format("DwmgTradeOfferMetaData{requiredMerchantLevel = %d, cachedUse = %d, hasB = %s}", this.requiredMerchantLevel, this.cachedUse, this.hasB);
	}
}
