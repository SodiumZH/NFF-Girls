package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class AdditionalInventory
{

	private NonNullList<ItemStack> list;
	private int size;
	
	public AdditionalInventory(int size)
	{
		if (size < 0)
			throw new NegativeArraySizeException();
		this.size = size;
		list = NonNullList.withSize(size, ItemStack.EMPTY);
	}
	
	public void set(ItemStack stack, int index)
	{	
		if (index < 0 || index >= getSize())
			throw new IndexOutOfBoundsException();
		list.set(index, stack);
	}
	
	public ItemStack get(int index)
	{
		if (index < 0 || index >= getSize())
			throw new IndexOutOfBoundsException();
		return list.get(index);
	}
	
	public int getSize()
	{
		return size;
	}
	
	public void saveToTag(CompoundTag parent, String key)
	{
		CompoundTag tag = new CompoundTag();
		tag.put("size", IntTag.valueOf(this.size));
		for (int i = 0; i < size; ++i) 
		{
			NbtHelper.saveItemStack(this.get(i), tag, key);
		}
	}
	
	public void readFromTag(CompoundTag tag)
	{
		if (!tag.contains("size", 3))
			throw new IllegalArgumentException("AdditionalInventory: reading from illegal tag.");
		if (tag.getInt("size") != this.getSize())
			throw new IllegalStateException("AdditionalInventory reading from NBT: size not matching.");
		
		for (int i = 0; i < getSize(); ++i)
		{
			if (tag.contains(Integer.toString(i)))
				this.set(NbtHelper.readItemStack(tag, Integer.toString(i)), i);
			else this.set(ItemStack.EMPTY, i);
		}
	}
	
	public static AdditionalInventory makeFromTag(CompoundTag tag) 
	{
		if (!tag.contains("size", 3))
			throw new IllegalArgumentException("AdditionalInventory: making from illegal tag.");
		
		AdditionalInventory inv = new AdditionalInventory(tag.getInt("size"));
		inv.readFromTag(tag);
		return inv;
	}
	
	public SimpleContainer toContainer() {
		SimpleContainer container = new SimpleContainer(getSize());
		for (int i = 0; i < getSize(); ++i)
		{
			container.setItem(i, get(i));
		}
		return container;
	}
	
	// Make a new InventoryTag from container.
	public static AdditionalInventory makeFromContainer(SimpleContainer container)
	{
		AdditionalInventory inv = new AdditionalInventory(container.getContainerSize());
		for (int i = 0; i < inv.getSize(); ++i)
		{
			inv.set(container.getItem(i), i);
		}
		return inv;	
	}
	
	// Set the tag content from a container.
	// Note: the tag size and container size must match.
	public void setFromContainer(SimpleContainer container)
	{
		if (getSize() != container.getContainerSize())
			throw new IllegalArgumentException("Size not matching.");
		for (int i = 0; i < getSize(); ++i)
		{
			set(container.getItem(i), i);
		}
	}
	
	public void swapItem(int position_1, int position_2)
	{
		ItemStack stack1 = this.get(position_1);
		this.set(this.get(position_2), position_1);
		this.set(stack1, position_2);
	}
	
	public boolean consumeItem(int position)
	{
		ItemStack stack = this.get(position);
		if (stack.isEmpty())
			return false;
		else
		{
			if (stack.getCount() == 1)
				this.set(ItemStack.EMPTY, position);
			else
			{
				stack.setCount(stack.getCount() - 1);
				this.set(stack, position);
			}
			return true;			
		}		
	}
}
