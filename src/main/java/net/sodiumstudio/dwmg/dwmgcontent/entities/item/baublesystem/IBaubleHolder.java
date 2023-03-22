package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

public interface IBaubleHolder
{
	public default LivingEntity getLiving()
	{
		return (LivingEntity)this;
	}
	
//	public default 
	
	public int[] getBaubleInventoryIndexes();
	
	public BaubleEffectTable getEffectTable();
	
	/*public AttributeModifier makeModifier(Item baubleType)
	{
		
	}*/
	
	
	
	
	
}
