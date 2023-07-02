package net.sodiumstudio.dwmg.entities.giftingsystem;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import net.sodiumstudio.befriendmobs.util.math.RandomSelection;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

/**
 * The item and probability table of Gifting-Returning mechanism.
 * 
 */
public class GiftingTable
{
	public GiftingTable() {}
	
	/**
	 * Map of all accepted items and their returning information.
	 * Key: Registry ID of the accepted item. 
	 * Value: Returning info of corresponding item.
	 */
	protected Map<AcceptItemInfo, Entry> content = new HashMap<AcceptItemInfo, Entry>();

	// Init //
	
	protected AcceptItemInfo editingAccept = null;
	protected ReturnItemInfo editingReturn = null;
	
	/**
	 * Add a new accepted item.
	 */
	public GiftingTable addAccept(String itemId)
	{
		AcceptItemInfo newInfo = new AcceptItemInfo(itemId);
		content.put(newInfo, new Entry());
		editingAccept = newInfo;
		editingReturn = null;
		return this;
	}
	
	/**
	 * Set the accept info lastly added.
	 */
	public GiftingTable setAccept(Consumer<AcceptItemInfo> actions)
	{
		if (editingAccept != null)
			actions.accept(editingAccept);
		else throw new IllegalArgumentException("GiftingTable build: no active AcceptItemInfo.");
		return this;
	}
	
	/**
	 * Add a return item info to lastly added accept info.
	 */
	public GiftingTable addReturn(String itemId, double probability)
	{
		if (editingAccept != null)
		{
			editingReturn = new ReturnItemInfo(itemId);
			content.get(editingAccept).table.put(editingReturn, probability);
		}
		else throw new IllegalArgumentException("GiftingTable build: no active AcceptItemInfo.");
		return this;
	}
	
	/**
	 * Add default return item info to lastly added accept info.
	 */
	public GiftingTable addDefaultReturn(String itemId, double probability)
	{
		if (editingAccept != null)
		{
			editingReturn = new ReturnItemInfo(itemId);
			content.get(editingAccept).defaultItem = editingReturn;
		}
		else throw new IllegalArgumentException("GiftingTable build: no active AcceptItemInfo.");
		return this;
	}
	
	/**
	 * Set the lastly added return item info.
	 */
	public GiftingTable setReturn(Consumer<ReturnItemInfo> actions)
	{
		if (editingReturn != null)
		{
			actions.accept(editingReturn);
			return this;
		}
		else throw new IllegalArgumentException("GiftingTable build: no active return item info.");
	}
	
	protected static class Entry
	{
		public Map<ReturnItemInfo, Double> table = new HashMap<ReturnItemInfo, Double>();
		@Nullable
		public ReturnItemInfo defaultItem = null;
		
		public RandomSelection<ReturnItemInfo> selection(Player player, IDwmgBefriendedMob mob)
		{
			RandomSelection<ReturnItemInfo> sel = RandomSelection.create(defaultItem);
			for (ReturnItemInfo info: table.keySet())
			{
				if (info != null && info.canGet(mob, player))
					sel.add(info, table.get(info));
			}
			return sel;
		}
		
		@Nonnull
		public ItemStack returnItem(Player player, IDwmgBefriendedMob mob)
		{
			ReturnItemInfo ret = selection(player, mob).getValue();//.get(mob, player);
			if (ret == null)
				return ItemStack.EMPTY;
			ItemStack stack = ret.get(mob, player);
			return stack == null ? ItemStack.EMPTY : stack;
		}
	}
	
	// Handle //
	/*public boolean handleGiving(ItemStack stack, Player player, IDwmgBefriendedMob mob)
	{
		
	}*/
	
}
