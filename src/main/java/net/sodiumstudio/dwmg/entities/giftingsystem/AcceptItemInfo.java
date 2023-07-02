package net.sodiumstudio.dwmg.entities.giftingsystem;

import java.util.Optional;
import java.util.function.Predicate;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

public class AcceptItemInfo
{
	protected ResourceLocation itemId = null;
	protected float minFav = -1;	// Min Favorability
	protected Predicate<ItemStack> condition = null;
	protected int consumeCount = 1;
	protected Optional<Float> getFav = Optional.empty();
	
	public AcceptItemInfo(ResourceLocation itemId)
	{
		this.itemId = itemId;
	}
	
	public AcceptItemInfo(String itemId)
	{
		this(new ResourceLocation(itemId));
	}
	
	public AcceptItemInfo(String domain, String name)
	{
		this(new ResourceLocation(domain, name));
	}
	
	public AcceptItemInfo minFav(float value)
	{
		this.minFav = value;
		return this;
	}
	
	public AcceptItemInfo condition(Predicate<ItemStack> cond)
	{
		this.condition = cond;
		return this;
	}
	
	public AcceptItemInfo consumeCount(int count)
	{
		this.consumeCount = count;
		return this;
	}
	
	public AcceptItemInfo getFav(float value)
	{
		this.getFav = Optional.of(value);
		return this;
	}
	
	public boolean canAccept(ItemStack item, Player player, IDwmgBefriendedMob mob)
	{
		if (mob.getFavorability().getFavorability() < this.minFav)
			return false;
		if (itemId == null)
			return false;
		Item itemType = ForgeRegistries.ITEMS.getValue(itemId);
		if (itemType == null)
			return false;
		if (!item.is(itemType))
			return false;
		if (condition != null && !condition.test(item))
			return false;
		if (item.getCount() < consumeCount)
			return false;
		return true;
	}
	
	public boolean accept(ItemStack item, Player player, IDwmgBefriendedMob mob)
	{
		if (canAccept(item, player, mob) && 
			!MinecraftForge.EVENT_BUS.post(new DwmgGiftGivingEvent(player, mob, item, this)))
		{
			item.shrink(consumeCount);
			if (getFav.isPresent())
				mob.getFavorability().addFavorability(getFav.get());
			return true;
		}
		else return false;
	}

	
}
