package net.sodiumstudio.dwmg.entities.vanillatrade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;
import net.sodiumstudio.dwmg.network.DwmgChannels;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.registries.DwmgTrades;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.NaMiscUtils;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.capability.SerializableCapabilityProvider;
import net.sodiumstudio.nautils.entity.vanillatrade.CVanillaMerchant;
import net.sodiumstudio.nautils.entity.vanillatrade.VanillaMerchant;
import net.sodiumstudio.nautils.entity.vanillatrade.VanillaTradeListing;

public interface CDwmgTradeHandler extends CVanillaMerchant
{
	public static final int[] LEVEL_REQUIREMENTS = {0, 5, 15, 30, 45};
	
	public static final int[] OFFER_COUNT_FOR_LEVEL = {2, 2, 2, 2, 2};
	public static final ItemListing INTRODUCTION = (e, rnd) -> 
	{
		ItemStack res = new ItemStack(DwmgItems.TRADE_INTRODUCTION_LETTER.get());
		DwmgItems.TRADE_INTRODUCTION_LETTER.get().write(res, IDwmgBefriendedMob.getBM(e));
		return new MerchantOffer(new ItemStack(Items.WRITABLE_BOOK), res, 1, 0, 0);
	};
	
	public void serverTick();

	public IDwmgBefriendedMob getBM();
	
	public List<DwmgTradeOfferMetaData> getMeta();
	
	public boolean isValidOffers();
	
	/**
	 * Ticks after sending a restock event.
	 * Note: a restock event doesn't necessarily restock all offers. The probability depends on the required level.
	 */
	public void setTicksRestock(int value);
	
	public static class Impl extends VanillaMerchant implements CDwmgTradeHandler
	{
		protected static final RandomSource RND = RandomSource.create();
		private List<DwmgTradeOfferMetaData> meta = new ArrayList<>();
		private int cachedLevel = 1;
		private int ticksRestock = 600 * 20;
		private int restockTimer = 600 * 20;
		
		public Impl(IDwmgBefriendedMob bm)
		{
			super(bm.asMob());
			this.setTicksRestock(bm.getRestockTicks());
		}
		
		@Override
		public IDwmgBefriendedMob getBM()
		{
			return (IDwmgBefriendedMob) this.getMob();
		}
		
		@Override
		public List<DwmgTradeOfferMetaData> getMeta()
		{
			return meta;
		}
		
		@Override
		public void generateTrades(){
			this.getOffersRaw().clear();
			this.getMeta().clear();
			List<VanillaTradeListing> trades = 
					DwmgTrades.TRADES.getListings(IDwmgBefriendedMob.getBM(this.getMob()).getData().getInitialEntityType()).pickListingForSpecifiedLevels(IDwmgBefriendedMob.getBM(this.getMob()).getTradeEntryCountEachLevel());
				/*var trades = DwmgTradeRegistry.getTradesImmutable(this.getMob().getType(), getProfession(), i);
				Collection<ItemListing> picked = NaContainerUtils.getRandomSubset
						(NaContainerUtils.iterableToSet(trades), Math.min(2, trades.size()));*/
			for (VanillaTradeListing listing: trades)
			{
				MerchantOffer offer = listing.getOffer(getMob(), RND);
				this.getOffersRaw().add(offer);
				DwmgTradeOfferMetaData meta = new DwmgTradeOfferMetaData(listing.getMerchantLevel(), 0, !this.getOffersRaw().get(this.getOffersRaw().size() - 1).getCostB().isEmpty());
				this.meta.add(meta);
			}
			

			// Finally add introduction letter entry
			this.getOffersRaw().add(INTRODUCTION.getOffer(getMob(), RND));
			this.getMeta().add(new DwmgTradeOfferMetaData(1, 0, false));
		}

		@Override
		public int getMerchantLevel() {
			if (this.getMob() instanceof IDwmgBefriendedMob bm)
			{
				for (int i = LEVEL_REQUIREMENTS.length; i > 0; --i)
				{
					if (bm.getXpLevel() >= LEVEL_REQUIREMENTS[i - 1])
						return i;
				}
				return 1;
			}
			return 1;
		}

		@Override
		public void onTrade(MerchantOffer offer) {
			var meta = this.getMeta(offer);
			if (meta != null)
				this.getBM().getFavorabilityHandler().addFavorability(meta.requiredMerchantLevel * 0.1f);
		}

		
		@Override
		public boolean canRestock() {
			return true;
		}

		@Override
		public void serverTick() 
		{
			// Handle offer uses cache
			if (this.getOffers().size() != meta.size())
			{
				/*throw new IllegalStateException(String.format("CDwmgTradeHandler: offer meta data size error. Expected: %d; Actual: %d",
						this.getOffers().size(), this.meta.size()));*/
				this.generateTrades();
			}
			int level = this.getMerchantLevel();
			if (level != cachedLevel)
			{
				this.onMerchantLevelChange(cachedLevel, level);
			}
			MerchantOffers offers = this.getOffers();
			// Cache available offer count, then set unavailable offer count to max
			for (int i = 0; i < this.getOffers().size(); ++i)
			{
				if (meta.get(i).requiredMerchantLevel <= level)
					meta.get(i).cachedUse = offers.get(i).getUses();
				else offers.get(i).setToOutOfStock();
			}
			this.cachedLevel = level;
			
			// Handle restock
			this.restockTimer --;
			if (this.restockTimer <= 0)
			{
				for (int i = 0; i < this.getOffers().size(); ++i)
				{
					if (this.getMeta(i).requiredMerchantLevel <= this.getMerchantLevel())
					{
						if (RND.nextFloat() <= (1f / (float) (this.getMeta(i).requiredMerchantLevel)))
								this.getOffers().get(i).resetUses();
					}
				}
				this.restockTimer = this.getBM().getRestockTicks();
				/*if (getBM().isOwnerInDimension())
					NaMiscUtils.printToScreen("Restocked", getBM().getOwner());*/
			}
			if (restockTimer % 200 == 0)
				if (getBM().isOwnerInDimension())
					NaMiscUtils.printToScreen(String.format("Restock time left: %d s", restockTimer / 20) , getBM().getOwner());
			
			// Handle sync
			ClientboundDwmgTradeSyncPacket packet = new ClientboundDwmgTradeSyncPacket(this);
			if (this.getBM().isOwnerInDimension() && this.getBM().getOwner() instanceof ServerPlayer toPlayer)
				DwmgChannels.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> toPlayer), packet);
		}
		
		@Override
		public void setTicksRestock(int value)
		{
			this.ticksRestock = value;
		}
		
		@Override
		public CompoundTag serializeNBT()
		{
			var tag = super.serializeNBT();
			ListTag tagMeta = new ListTag();
			tagMeta.addAll(NaContainerUtils.castList(this.meta, meta -> meta.toTag()));
			tag.put("meta", tagMeta);
			tag.putInt("cached_level", this.cachedLevel);
			tag.putInt("ticks_restock", this.ticksRestock);
			tag.putInt("restock_timer", this.restockTimer);
			return tag;
		}
		
		@Override
		public void deserializeNBT(CompoundTag tag)
		{
			super.deserializeNBT(tag);
			ListTag tagCachedUse = tag.getList("meta", NbtHelper.TAG_COMPOUND_ID);
			this.meta.clear();
			for (int i = 0; i < tagCachedUse.size(); ++i)
			{
				meta.add(DwmgTradeOfferMetaData.fromTag(tagCachedUse.getCompound(i)));
			}
			this.cachedLevel = tag.getInt("cached_level");
			this.ticksRestock = tag.getInt("ticks_restock");
			this.restockTimer = tag.getInt("restock_timer");
		}

		protected void onMerchantLevelChange(int from, int to)
		{
			if (!isValidOffers()) return;
			for (int i = 0; i < this.getOffers().size(); ++i)
			{
				if (this.meta.get(i).requiredMerchantLevel <= to)
				{
					this.setOfferUses(i, this.meta.get(i).cachedUse);
				}
			}
		}

		// === Utils === //
		
		protected DwmgTradeOfferMetaData getMeta(int index)
		{
			return this.meta.get(index);
		}
		
		@Nullable
		protected DwmgTradeOfferMetaData getMeta(MerchantOffer offer)
		{
			for (int i = 0; i < this.getOffers().size(); ++i)
			{
				if (this.getOffers().get(i) == offer)
					return this.meta.get(i);
			}
			return null;
		}
		
		@Override
		public boolean isValidOffers()
		{
			return this.getOffers().size() == this.meta.size() && this.getOffers().size() != 0;
		}
	}
	
	public static class Prvd extends SerializableCapabilityProvider<CompoundTag, CDwmgTradeHandler>
	{

		public Prvd(IDwmgBefriendedMob bm, Capability<CDwmgTradeHandler> holder)
		{
			super(() -> new Impl(bm), holder);
		}
		
	}
	
}
