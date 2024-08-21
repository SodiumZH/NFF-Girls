package net.sodiumstudio.befriendmobs.item;

import java.util.ArrayList;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.sodiumstudio.nautils.NaItemUtils;
import net.sodiumstudio.nautils.NbtHelper;

public class ItemPackageItem extends Item
{
	public ItemPackageItem(Properties pProperties)
	{
		super(pProperties);
		// TODO Auto-generated constructor stub
	}

	public void addItem(ItemStack itemPackage, ItemStack toAdd)
	{
		if (!(itemPackage.getItem() instanceof ItemPackageItem))
			throw new IllegalArgumentException("Wrong item type.");
		
		ListTag list;
		if (itemPackage.getOrCreateTag().contains("containing_items", NbtHelper.TAG_LIST_ID))
			list = itemPackage.getOrCreateTag().getList("containing_items", NbtHelper.TAG_COMPOUND_ID);
		else 
		{
			list = new ListTag();
			itemPackage.getOrCreateTag().put("containing_items", list);
		}
		CompoundTag newTag = new CompoundTag();
		toAdd.save(newTag);
		list.add(newTag);
	}
	
	public ArrayList<ItemStack> getContainingItems(ItemStack itemPackage)
	{
		if (!(itemPackage.getItem() instanceof ItemPackageItem))
			throw new IllegalArgumentException("Wrong item type.");
		ArrayList<ItemStack> items = new ArrayList<>();
		if (!itemPackage.hasTag() || !itemPackage.getTag().contains("containing_items", NbtHelper.TAG_LIST_ID))
			return items;
		for (int i = 0; i < itemPackage.getTag().getList("containing_items", NbtHelper.TAG_COMPOUND_ID).size(); ++i)//Tag tag: itemPackage.getTag().getList("containing_items", NbtHelper.TAG_COMPOUND_ID))
		{
			items.add(ItemStack.of(itemPackage.getTag().getList("containing_items", NbtHelper.TAG_COMPOUND_ID).getCompound(i)));
		}
		return items;
	}
	
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand usedHand)
	{
		if (!level.isClientSide())
		{
			for (ItemStack stack: getContainingItems(player.getItemInHand(usedHand)))
			{
				NaItemUtils.giveOrDrop(player, stack);
			}
			player.getItemInHand(usedHand).shrink(1);
		}
		return InteractionResultHolder.sidedSuccess(player.getItemInHand(usedHand), level.isClientSide());
	}
	
	public ItemStack create(ItemStack... stacks)
	{
		Instance inst = Instance.of(this.getDefaultInstance());
		for (int i = 0; i < stacks.length; ++i)
		{
			inst.addItem(stacks[i]);
		}
		return inst.get();
	}
	
	public static class Instance
	{
		protected final ItemStack itemPackageStack;
		
		protected Instance(ItemStack itemPackageStack)
		{
			if (!(itemPackageStack.getItem() instanceof ItemPackageItem))
				throw new IllegalArgumentException("Wrong item type.");
			this.itemPackageStack = itemPackageStack;
		}
		
		public static Instance of(ItemStack itemPackageStack)
		{
			return new Instance(itemPackageStack);
		}
		
		public Instance addItem(ItemStack stack)
		{
			((ItemPackageItem)(itemPackageStack.getItem())).addItem(itemPackageStack, stack);
			return this;
		}
		
		public ItemStack get()
		{
			return itemPackageStack.copy() ;
		}
	}
}
