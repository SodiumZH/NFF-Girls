package net.sodiumstudio.nautils.entity.vanillatrade;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraftforge.common.util.INBTSerializable;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;

/**
 * The main capability interface as Vanilla Merchant implementation for generic mobs.
 * This capability allows to attach Vanilla Merchant functions to any mobs.
 * <p> 
 */
public interface CVanillaMerchant extends Merchant, INBTSerializable<CompoundTag>
{
	
	public Mob getMob();
	
	public VillagerProfession getProfession();
	
	public void updateTrades();
	
	/**
	 * @deprecated Use {@code getXp} instead.
	 */
	@Deprecated
	@Override
	public int getVillagerXp();
	
	public int getXp();
	
	public void setXp(int value);
	
	@Override
	default void openTradingScreen(Player pPlayer, Component pDisplayName, int pLevel)
	{
		this.setTradingPlayer(pPlayer);
		Merchant.super.openTradingScreen(pPlayer, pDisplayName, pLevel);
	}

	/**
	 * Invoked on notifying trade (when player takes an item from the trade result box).
	 */
	public void onTrade(MerchantOffer offer);
	
	/**
	 * <b>Abstract</b> implementation. This class doesn't allow to instantiate directly. To create custom implementation, 
	 * first create an interface extending {@code CVanillaMerchant}, then use:
	 * <p>{@code public class YourImplementation extends CVanillaMerchant.Impl implements YourInterface}
	 * <p>to create an instantiatable subclass.
	 */
	public static class Impl implements CVanillaMerchant
	{

		private final Mob mob;
		private CompoundTag tag;
		@Nullable
		private Player tradingPlayer = null;
		private MerchantOffers offers;
		private int xp;
		protected RandomSource rnd = RandomSource.create();
		
		public Impl(Mob mob)
		{
			this.mob = mob;
			this.tag = new CompoundTag();
			xp = 1;
		}
		
		@Override
		public Mob getMob() {
			return mob;
		}
		
		@Override
		public void setTradingPlayer(Player tradingPlayer) {
			this.tradingPlayer = tradingPlayer;
		}

		@Override
		public Player getTradingPlayer() {
			return this.tradingPlayer;
		}

		@Override
		public MerchantOffers getOffers() {
			if (this.offers == null || this.offers.isEmpty())
			{
				this.offers = new MerchantOffers();
				this.updateTrades();
			}

			return this.offers;
		}

		@Override
		public void overrideOffers(MerchantOffers pOffers) {
		}

		@Override
		public void notifyTrade(MerchantOffer offer) {
			offer.increaseUses();
			this.onTrade(offer);
		}

		@Override
		public void notifyTradeUpdated(ItemStack pStack) {
		}

		@Deprecated
		@Override
		public final int getVillagerXp() {
			return getXp();
		}

		@Override
		public int getXp()
		{
			return xp;
		}
		
		@Override
		public void setXp(int value)
		{
			this.xp = value;
		}
		
		@Override
		public void overrideXp(int pXp) {
		}

		@Override
		public boolean showProgressBar() {
			return false;
		}

		@Override
		public SoundEvent getNotifyTradeSound() {
			return null;
		}

		@Override
		public boolean isClientSide() {
			return this.getMob().getLevel().isClientSide();
		}

		@Override
		public CompoundTag serializeNBT() {
			CompoundTag tag = new CompoundTag();
			tag.put("offers", this.getOffers().createTag());
			return tag;
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			this.offers = new MerchantOffers(nbt.getCompound("offers"));
		}

		@Override
		public void updateTrades(){
			for (int i = 1; i <= this.getVillagerXp(); ++i)
			{
				var trades = VanillaTradeRegistry.getTradesImmutable((EntityType<? extends Mob>) this.getMob().getType(), getProfession(), i);
				for (ItemListing offer: trades)
				{
					this.offers.add(offer.getOffer(getMob(), rnd));
				}
			}
		}

		@Override
		public VillagerProfession getProfession() {
			return VillagerProfession.NONE;
		}
		
		@Override
		public void onTrade(MerchantOffer offer) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
