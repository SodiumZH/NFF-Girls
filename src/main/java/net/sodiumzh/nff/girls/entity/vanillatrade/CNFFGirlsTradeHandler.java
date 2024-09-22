package net.sodiumzh.nff.girls.entity.vanillatrade;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.mojang.logging.LogUtils;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.network.PacketDistributor;
import net.sodiumzh.nautils.capability.SerializableCapabilityProvider;
import net.sodiumzh.nautils.entity.vanillatrade.CVanillaMerchant;
import net.sodiumzh.nautils.entity.vanillatrade.VanillaMerchant;
import net.sodiumzh.nautils.entity.vanillatrade.VanillaTradeListing;
import net.sodiumzh.nautils.statics.NaUtilsContainerStatics;
import net.sodiumzh.nautils.statics.NaUtilsNBTStatics;
import net.sodiumzh.nff.girls.entity.INFFGirlsTamed;
import net.sodiumzh.nff.girls.network.NFFGirlsChannels;
import net.sodiumzh.nff.girls.registry.NFFGirlsItems;
import net.sodiumzh.nff.girls.registry.NFFGirlsTrades;

public interface CNFFGirlsTradeHandler extends CVanillaMerchant
{
	public static final int[] LEVEL_REQUIREMENTS = {0, 5, 15, 30, 45};
	public static final int[] OFFER_COUNT_FOR_LEVEL = {2, 2, 2, 2, 2};
	public static final ItemListing INTRODUCTION = (e, rnd) -> 
	{
		ItemStack res = new ItemStack(NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get());
		NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get().write(res, INFFGirlsTamed.getBM(e));
		return new MerchantOffer(new ItemStack(Items.WRITABLE_BOOK), res, 1, 0, 0);
	};
	
	
	public void serverTick();

	public INFFGirlsTamed getBM();
	
	public List<NFFGirlsTradeOfferMetaData> getMeta();
	
	public boolean isValidOffers();

	/**
	 * How many points it needs to get an introduction letter
	 */
	public int getPointsPerIntroduction();
	
	/**
	 * Current trade points
	 */
	public int getPoints();
	
	/**
	 * Set current trade points
	 */
	public void setPoints(int val);
	
	/**
	 * Remove all offers and regenerate. This happens in game when specific item is used.
	 * @param setOutOfStock If true, the new offers will be out-of-stock, otherwise all available.
	 */
	public void regenerateTrades(boolean setOutOfStock);
	
	/**
	 * Ticks after sending a restock event.
	 * Note: a restock event doesn't necessarily restock all offers. The probability depends on the required level.
	 */
	public int getRestockTicks();
	
	public static class Impl extends VanillaMerchant implements CNFFGirlsTradeHandler
	{
		protected static final RandomSource RND = RandomSource.create();
		private List<NFFGirlsTradeOfferMetaData> meta = new ArrayList<>();
		private int cachedLevel = 1;
		private int restockTimer = 600 * 20;
		private boolean triedRegenerate = false;	// When tick encounters an error, it will try regenerating the offers. If it still errors, the offers will be set back and throw the error/exception.
		private MerchantOffers backupOffers = null;
		private List<NFFGirlsTradeOfferMetaData> backupMeta = null;
		private int tradePoints = 0;
		
		public Impl(INFFGirlsTamed bm)
		{
			super(bm.asMob());
		}
		
		@Override
		public INFFGirlsTamed getBM()
		{
			return (INFFGirlsTamed) this.getMob();
		}
		
		@Override
		public List<NFFGirlsTradeOfferMetaData> getMeta()
		{
			return meta;
		}
		
		@Override
		public int getPointsPerIntroduction()
		{
			return INFFGirlsTamed.isBM(this.getMob()) ? INFFGirlsTamed.getBM(this.getMob()).pointsPerIntroductionLetter() : 128;
		}
		
		@Override
		public int getPoints()
		{
			return this.tradePoints;
		}
		
		@Override
		public void setPoints(int val)
		{
			this.tradePoints = val;
		}
		
		@Override
		public void generateTrades(){
			this.getOffersRaw().clear();
			this.getMeta().clear();
			List<VanillaTradeListing> trades = 
					NFFGirlsTrades.TRADES.getListings(INFFGirlsTamed.getBM(this.getMob()).getData().getInitialEntityType()).pickListingForSpecifiedLevels(INFFGirlsTamed.getBM(this.getMob()).getTradeEntryCountEachLevel());
				/*var trades = DwmgTradeRegistry.getTradesImmutable(this.getMob().getType(), getProfession(), i);
				Collection<ItemListing> picked = NaUtilsContainerStatics.getRandomSubset
						(NaUtilsContainerStatics.iterableToSet(trades), Math.min(2, trades.size()));*/
			for (VanillaTradeListing listing: trades)
			{
				MerchantOffer offer = listing.getOffer(getMob(), RND);
				this.getOffersRaw().add(offer);
				NFFGirlsTradeOfferMetaData meta = new NFFGirlsTradeOfferMetaData(listing.getMerchantLevel(), 0, !this.getOffersRaw().get(this.getOffersRaw().size() - 1).getCostB().isEmpty());
				this.meta.add(meta);
			}
			

			// Finally add introduction letter entry
			this.getOffersRaw().add(INTRODUCTION.getOffer(getMob(), RND));
			this.getMeta().add(new NFFGirlsTradeOfferMetaData(1, 0, false));
		}

		@Override
		public int getMerchantLevel() {
			if (this.getMob() instanceof INFFGirlsTamed bm)
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
			if (offer.getResult().is(NFFGirlsItems.TRADE_INTRODUCTION_LETTER.get()))
				tradePoints = 0;
			else 
			{
				tradePoints += meta.requiredMerchantLevel;
				this.getBM().getLevelHandler().addExp((1 + meta.requiredMerchantLevel) * meta.requiredMerchantLevel / 2 );
			}
		}

		
		@Override
		public boolean canRestock() {
			return true;
		}

		@Override
		public void serverTick() 
		{
			//boolean tryingRegenerate = false;
			try {
				this.serverTickInternal();
			} catch (Throwable t) {
				if (!triedRegenerate)
				{
					LogUtils.getLogger().error("CNFFGirlsTradeHandler ticking encountered an error. Try regenerate offers.");
					t.printStackTrace();
					this.triedRegenerate = true;
					this.backupOffers = this.getOffersRaw();
					this.backupMeta = this.getMeta();
					this.setOffers(new MerchantOffers());
					this.meta = new ArrayList<>();
					this.generateTrades();
					//tryingRegenerate = true;
				}
				else 
				{
					LogUtils.getLogger().error("CNFFGirlsTradeHandler ticking encountered an error which cannot be fixed by regenerating offers.");
					if (this.backupOffers != null)
						this.setOffers(backupOffers);
					if (this.backupMeta != null)
						this.meta = this.backupMeta;
					throw t;
				}
			}
		}
		
		protected void serverTickInternal()
		{
			// Handle offer uses cache
			if (this.getOffers().size() != meta.size())
			{
				/*throw new IllegalStateException(String.format("CNFFGirlsTradeHandler: offer meta data size error. Expected: %d; Actual: %d",
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
			for (int i = 0; i < this.getOffers().size() - 1; ++i)	// The last element is introduction, no tick
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
				for (int i = 0; i < this.getOffers().size() - 1; ++i)	// The last element is introduction, no tick
				{
					if (this.getMeta(i).requiredMerchantLevel <= this.getMerchantLevel())
					{
						if (RND.nextFloat() <= 1d / Math.sqrt(this.getMeta(i).requiredMerchantLevel));
								this.getOffers().get(i).resetUses();
					}
				}
				this.restockTimer = this.getBM().getRestockTicks();
				/*if (getBM().isOwnerInDimension())
					NaUtilsMiscStatics.printToScreen("Restocked", getBM().getOwner());*/
			}
			/*if (restockTimer % 200 == 0)
				if (getBM().isOwnerInDimension())
			
			NaUtilsMiscStatics.printToScreen(String.format("Restock time left: %d s", restockTimer / 20) , getBM().getOwner());*/
			
			// Update introduction letter entry
			if (this.tradePoints < this.getPointsPerIntroduction())
				this.getOffers().get(this.getOffers().size() - 1).setToOutOfStock();
			else this.getOffers().get(this.getOffers().size() - 1).resetUses();

			// Update discount
			for (var offer: this.getOffers())
			{
				float factor = Mth.lerp(this.getBM().getNormalizedFavorability(), 0.5f, -0.5f);
				offer.setSpecialPriceDiff(Math.round(factor * offer.getBaseCostA().getCount()));
			}
			
			// Handle sync
			ClientboundNFFGirlsTradeSyncPacket packet = new ClientboundNFFGirlsTradeSyncPacket(this);
			if (this.getBM().isOwnerInDimension() && this.getBM().getOwner() instanceof ServerPlayer toPlayer)
				NFFGirlsChannels.SYNC_CHANNEL.send(PacketDistributor.PLAYER.with(() -> toPlayer), packet);
			
		}
		
		@Override
		public CompoundTag serializeNBT()
		{
			var tag = super.serializeNBT();
			ListTag tagMeta = new ListTag();
			tagMeta.addAll(NaUtilsContainerStatics.castList(this.meta, meta -> meta.toTag()));
			tag.put("meta", tagMeta);
			tag.putInt("cached_level", this.cachedLevel);
			tag.putInt("restock_timer", this.restockTimer);
			tag.putInt("points", this.tradePoints);
			return tag;
		}
		
		@Override
		public void deserializeNBT(CompoundTag tag)
		{
			super.deserializeNBT(tag);
			ListTag tagCachedUse = tag.getList("meta", NaUtilsNBTStatics.TAG_COMPOUND_ID);
			this.meta.clear();
			for (int i = 0; i < tagCachedUse.size(); ++i)
			{
				meta.add(NFFGirlsTradeOfferMetaData.fromTag(tagCachedUse.getCompound(i)));
			}
			this.cachedLevel = tag.getInt("cached_level");
			this.restockTimer = tag.getInt("restock_timer");
			this.tradePoints = tag.getInt("points");
			this.checkAndRemoveInvalidOffers();
		}

		protected void onMerchantLevelChange(int from, int to)
		{
			if (!isValidOffers()) return;
			for (int i = 0; i < this.getOffers().size() - 1; ++i)	// The last element is introduction, skip
			{
				if (this.meta.get(i).requiredMerchantLevel <= to)
				{
					this.setOfferUses(i, this.meta.get(i).cachedUse);
				}
			}
		}

		// === Utils === //
		
		protected NFFGirlsTradeOfferMetaData getMeta(int index)
		{
			return this.meta.get(index);
		}
		
		@Nullable
		protected NFFGirlsTradeOfferMetaData getMeta(MerchantOffer offer)
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

		@Override
		public int getRestockTicks() {
			return this.getBM().getRestockTicks();
		}

		protected void setAllOffersOutOfStock() {
			for (int i = 0; i < this.getOffers().size(); ++i)
			{
				this.getOffers().get(i).setToOutOfStock();
				this.getMeta().get(i).cachedUse = this.getOffers().get(i).getMaxUses();
			}
		}

		@Override
		public void regenerateTrades(boolean setOutOfStock) {
			this.generateTrades();
			if (setOutOfStock)
				this.setAllOffersOutOfStock();
		}
		
		protected void checkAndRemoveInvalidOffers()
		{
			if (!isValidOffers())
				return;
			List<MerchantOffer> toRemove0 = new ArrayList<>();
			List<NFFGirlsTradeOfferMetaData> toRemove1 = new ArrayList<>();
			for (int i = 0; i < this.getOffers().size(); ++i)
			{
				if (this.getOffers().get(i).getBaseCostA().isEmpty() || this.getOffers().get(i).getResult().isEmpty()
						|| (this.getOffers().get(i).getCostB().isEmpty() && this.getMeta().get(i).hasB))
				{
					toRemove0.add(this.getOffers().get(i));
					toRemove1.add(this.getMeta().get(i));
				}
			}
			for (var elem: toRemove0)
			{
				this.getOffers().remove(elem);
			}
			for (var elem: toRemove1)
			{
				this.getMeta().remove(elem);
			}
		}
	}
	
	public static class Prvd extends SerializableCapabilityProvider<CompoundTag, CNFFGirlsTradeHandler>
	{

		public Prvd(INFFGirlsTamed bm, Capability<CNFFGirlsTradeHandler> holder)
		{
			super(() -> new Impl(bm), holder);
		}
		
	}
	
}
