package net.sodiumstudio.nautils.entity.vanillatrade;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.sodiumstudio.nautils.NaReflectionUtils;

/**
 * <b>Abstract</b> implementation of {@code CVanillaMerchant}. This class doesn't allow to instantiate directly. 
 * To create custom implementation, first create an interface extending {@code CVanillaMerchant}, then use:
 * <p>{@code public class YourImplementation extends VanillaMerchant implements YourInterface}
 * <p>to create an instantiatable subclass.
 */
public abstract class VanillaMerchant implements CVanillaMerchant
{
	private final Mob mob;
	@Nullable
	private Player tradingPlayer = null;
	private MerchantOffers offers;
	private int xp;
	protected RandomSource rnd = RandomSource.create();
	
	public VanillaMerchant(Mob mob)
	{
		this.mob = mob;
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
		if (this.offers == null)
		{
			this.offers = new MerchantOffers();
		}
		if (this.offers.isEmpty() && !this.getMob().level().isClientSide)
		{
			this.generateTrades();
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
		return this.getMob().level().isClientSide();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.put("offers", this.getOffers().createTag());
		tag.putInt("xp", this.getXp());
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		this.offers = new MerchantOffers(nbt.getCompound("offers"));
		this.setXp(nbt.getInt("xp"));
	}

	@Override
	public VillagerProfession getProfession() {
		return VillagerProfession.NONE;
	}
	
	// === Internal Utils === //
	
	/**
	 * Get the offers without any initialization or checks.
	 * <p><b>Warning:</b> Call this only when you're modifying the offers. It won't check if offers initialized.
	 * Under other contexts (e.g. vanilla calls, external access) you'll probably want to use {@code getOffers} instead.
	 */
	@Nullable
	protected final MerchantOffers getOffersRaw()
	{
		return offers;
	}

	protected void setOfferUses(int index, int value)
	{
		NaReflectionUtils.forceSet(offers.get(index), MerchantOffer.class, "f_45313_", value);	// MerchantOffer#uses
	}
	
	protected void decreaseOfferUses(int index)
	{
		this.setOfferUses(index, Math.max(0, this.getOffers().get(index).getUses() - 1));
	}
	
	/**
	 * Redirect the offers. Only called on error processing.
	 */
	@Nullable
	protected final void setOffers(MerchantOffers newOffers)
	{
		this.offers = newOffers;
	}
}
