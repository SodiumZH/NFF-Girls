package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class InventoryTag
{
	private CompoundTag tag = new CompoundTag();
	
	public InventoryTag(int size)
	{
		if (size < 0)
			throw new NegativeArraySizeException();
		tag.putInt("inventory_tag_size", size);
		for (int i = 0; i < size; ++i)
		{
			this.put(ItemStack.EMPTY, i);
		}
	}
	
	public void put(ItemStack stack, int index)
	{	
		if (index < 0 || index >= getSize())
			throw new IndexOutOfBoundsException();
		NbtHelper.saveItemStack(stack, tag, Integer.toString(index));
	}
	
	public ItemStack get(int index)
	{
		if (index < 0 || index >= getSize())
			throw new IndexOutOfBoundsException();
		return NbtHelper.readItemStack(tag, Integer.toString(index));
	}
	
	public int getSize()
	{
		return tag.getInt("inventory_tag_size");
	}
	
	public void saveTo(CompoundTag parent, String key)
	{
		parent.put(key, tag);
	}
	
	public void readFrom(CompoundTag tag)
	{
		if (!tag.contains("inventory_tag_size", 3))
			throw new IllegalStateException("Inventory Tag: reading from illegal tag.");
		
		for (int i = 0; i < getSize(); ++i)
		{
			if (tag.contains(Integer.toString(i)))
				put(NbtHelper.readItemStack(tag, Integer.toString(i)), i);
		}
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
	public static InventoryTag makeFromContainer(SimpleContainer container)
	{
		InventoryTag tag = new InventoryTag(container.getContainerSize());
		for (int i = 0; i < tag.getSize(); ++i)
		{
			tag.put(container.getItem(i), i);
		}
		return tag;	
	}
	
	// Set the tag content from a container.
	// Note: the tag size and container size must match.
	public void setFromContainer(SimpleContainer container)
	{
		if (getSize() != container.getContainerSize())
			throw new IllegalArgumentException("Size not matching.");
		for (int i = 0; i < getSize(); ++i)
		{
			put(container.getItem(i), i);
		}
	}
	
	public void swapItem(int position_1, int position_2)
	{
		ItemStack stack1 = this.get(position_1);
		this.put(this.get(position_2), position_1);
		this.put(stack1, position_2);
	}
	
	public boolean consumeItem(int position)
	{
		ItemStack stack = this.get(position);
		if (stack.isEmpty())
			return false;
		else
		{
			if (stack.getCount() == 1)
				this.put(ItemStack.EMPTY, position);
			else
			{
				stack.setCount(stack.getCount() - 1);
				this.put(stack, position);
			}
			return true;			
		}
			
	}
	
}
