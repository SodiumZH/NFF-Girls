package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;

/** Inventory tag in which a mob's equipment is saved at fixed positions
 * 0: helmet; 1: chest; 2: legs; 3: feet; 4: main hand; 5: off hand
 */
public class AdditionalInventoryWithEquipment extends AdditionalInventory
{
	public AdditionalInventoryWithEquipment(int size)
	{
		super(size);
		if (size < 6)
			throw new IllegalArgumentException("AdditionalInventoryWithEquipment must have at least 6 slots for a mob's equipment slots.");
		updateOwner();
	}
	
	public AdditionalInventoryWithEquipment(int size, IBefriendedMob owner)
	{
		super(size, owner);
		updateOwner();
	}
	
	public void getFromMob(Mob mob)
	{
		this.setItem(0, mob.getItemBySlot(EquipmentSlot.HEAD));
		this.setItem(1, mob.getItemBySlot(EquipmentSlot.CHEST));
		this.setItem(2, mob.getItemBySlot(EquipmentSlot.LEGS));
		this.setItem(3, mob.getItemBySlot(EquipmentSlot.FEET));
		this.setItem(4, mob.getItemBySlot(EquipmentSlot.MAINHAND));
		this.setItem(5, mob.getItemBySlot(EquipmentSlot.OFFHAND));
		updateOwner();
		
	}
	
	public void setMobEquipment(Mob mob)
	{
		mob.setItemSlot(EquipmentSlot.HEAD, this.getItem(0));
		mob.setItemSlot(EquipmentSlot.CHEST, this.getItem(1));
		mob.setItemSlot(EquipmentSlot.LEGS, this.getItem(2));
		mob.setItemSlot(EquipmentSlot.FEET, this.getItem(3));
		mob.setItemSlot(EquipmentSlot.MAINHAND, this.getItem(4));
		mob.setItemSlot(EquipmentSlot.OFFHAND, this.getItem(5));
		updateOwner();
	}
	
	public ItemStack getItemFromSlot(EquipmentSlot slot)
	{
		updateOwner();
		switch (slot)
		{
		case HEAD:
		{
			return this.getItem(0);
		}
		case CHEST:
		{
			return this.getItem(1);
		}
		case LEGS:
		{
			return this.getItem(2);
		}
		case FEET:
		{
			return this.getItem(3);
		}
		case MAINHAND:
		{
			return this.getItem(4);
		}
		case OFFHAND:
		{
			return this.getItem(5);
		}
		}
		throw new IllegalArgumentException();
	}
	
}
