package net.sodiumstudio.nautils.entity.vanillatrade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * The main capability interface as Vanilla Merchant implementation for generic mobs.
 * This capability allows to attach Vanilla Merchant functions to any mobs.
 * <p> 
 */
public interface CVanillaMerchant extends Merchant, INBTSerializable<CompoundTag>
{
	/**
	 * Corresponding mob.
	 */
	public Mob getMob();
	
	/**
	 * Profession. It allows not specifying by using {@code VillagerProfession#NONE}.
	 */
	public VillagerProfession getProfession();
	
	public void generateTrades();
	
	/**
	 * @deprecated Use {@code getXp} instead. Only for vanilla internal use.
	 */
	@Deprecated
	@Override
	public int getVillagerXp();
	
	/**
	 * Merchant xp. Note: this isn't the xp reward on trade, or the villager level.
	 */
	public int getXp();
	
	/**
	 * Set merchant xp. Note: this isn't the xp reward on trade, or the villager level.
	 */
	public void setXp(int value);
	
	/**
	 * Get the merchant level. Note: this isn't the player level or the merchant xp.
	 * <p>Tip: you can also use other mechanisms to define the level besides xp.
	 */
	public int getMerchantLevel();
	
	/**
	 * Get the max merchant level. Note: this isn't the player level or the merchant xp.
	 * <p>Tip: you can also use other mechanisms to define the level besides xp.
	 */
	public int getMaxMerchantLevel();
	
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

	@Override
	public boolean canRestock();
	
	public default void playTradeSound()
	{
		Mob mob = getMob();
		mob.getLevel().playLocalSound(mob.getX(), mob.getY(), mob.getZ(), this.getNotifyTradeSound(), SoundSource.NEUTRAL, 1.0F, 1.0F, false);
	}

}
