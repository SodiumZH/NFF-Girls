package net.sodiumstudio.dwmg.befriendmobs.util;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

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
	}
	
	public void getFromMob(Mob mob)
	{
		this.set(mob.getItemBySlot(EquipmentSlot.HEAD), 0);
		this.set(mob.getItemBySlot(EquipmentSlot.CHEST), 1);
		this.set(mob.getItemBySlot(EquipmentSlot.LEGS), 2);
		this.set(mob.getItemBySlot(EquipmentSlot.FEET), 3);
		this.set(mob.getItemBySlot(EquipmentSlot.MAINHAND), 4);
		this.set(mob.getItemBySlot(EquipmentSlot.OFFHAND), 5);
	}
	
	public void setMobEquipment(Mob mob)
	{
		mob.setItemSlot(EquipmentSlot.HEAD, this.get(0));
		mob.setItemSlot(EquipmentSlot.CHEST, this.get(1));
		mob.setItemSlot(EquipmentSlot.LEGS, this.get(2));
		mob.setItemSlot(EquipmentSlot.FEET, this.get(3));
		mob.setItemSlot(EquipmentSlot.MAINHAND, this.get(4));
		mob.setItemSlot(EquipmentSlot.OFFHAND, this.get(5));
	}
	
	public ItemStack getItemFromSlot(EquipmentSlot slot)
	{
		switch (slot)
		{
		case HEAD:
		{
			return this.get(0);
		}
		case CHEST:
		{
			return this.get(1);
		}
		case LEGS:
		{
			return this.get(2);
		}
		case FEET:
		{
			return this.get(3);
		}
		case MAINHAND:
		{
			return this.get(4);
		}
		case OFFHAND:
		{
			return this.get(5);
		}
		}
		throw new IllegalArgumentException();
	}
	
}
