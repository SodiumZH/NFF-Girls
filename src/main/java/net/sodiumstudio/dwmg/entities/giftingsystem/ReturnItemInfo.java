package net.sodiumstudio.dwmg.entities.giftingsystem;

import java.util.function.BiPredicate;
import java.util.function.Consumer;

import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

/** Information of a returned item. */
public class ReturnItemInfo
{
	// Registry ID of the returned item
	protected ResourceLocation id;
	// Returning amount each time
	protected int count = 1;
	// Initializer of the output item stack. 
	protected Consumer<ItemStack> initializer = (i) -> {};
	
	protected float minFav = -1;
	protected BiPredicate<Player, IDwmgBefriendedMob> condition = (p, m) -> true;

	public ReturnItemInfo(ResourceLocation id)
	{
		this.id = id;
	}

	public ReturnItemInfo(String id)
	{
		this(new ResourceLocation(id));
	}
	
	public ReturnItemInfo count(int value) {
		this.count = value;
		return this;
	}

	public ReturnItemInfo initBy(Consumer<ItemStack> initializer) {
		this.initializer = initializer;
		return this;
	}

	public ReturnItemInfo minFav(float value) {
		this.minFav = value;
		return this;
	}

	public ReturnItemInfo condition(BiPredicate<Player, IDwmgBefriendedMob> cond) {
		this.condition = cond;
		return this;
	}

	public boolean canGet(IDwmgBefriendedMob mob, Player player)
	{
		if (mob.getOwner() != player)
			return false;
		if (mob.getFavorability().getFavorability() < this.minFav)
			return false;
		if (!condition.test(player, mob))
			return false;
		Item item = ForgeRegistries.ITEMS.getValue(id);
		if (item == null)
			return false;
		return true;
	}
	
	@Nullable
	public ItemStack get(IDwmgBefriendedMob mob, Player player) {
		if (mob.getOwner() != player)
			return null;
		if (mob.getFavorability().getFavorability() < this.minFav)
			return null;
		if (!condition.test(player, mob))
			return null;
		Item item = ForgeRegistries.ITEMS.getValue(id);
		if (item == null)
			return null;
		ItemStack stack = new ItemStack(item, count);
		initializer.accept(stack);
		return stack;
	}

	public boolean isValid()
	{
		return ForgeRegistries.ITEMS.getValue(id) != null;
	}
	
}
