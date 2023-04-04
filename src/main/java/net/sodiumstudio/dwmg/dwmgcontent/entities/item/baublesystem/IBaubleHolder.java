package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import java.util.HashSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IBaubleHolder {

	public HashSet<ItemStack> getBaubleStacks();


	public BaubleHandler getHandler();
	
	/* Bauble existance & counting */
		
	public default boolean isBauble(Item item){
		return getHandler().isAccepted(item);
	}
	
	public default boolean isBauble(ItemStack stack){
		return getHandler().isAccepted(stack);
	}
	
	public default boolean containsBauble(Item item)
	{
		return getHandler().getHoldingCount(this, item) > 0;
	}
	
	public default int getBaubleCount(Item item)
	{
		return getHandler().getHoldingCount(this, item);
	}
	
	
	/* Apply effects */
	public default void updateEffects()
	{
		getHandler().updateBaubleEffects(this);
	}
	
	/* Misc */	
	public default Mob asMob()
	{
		return (Mob)this;
	}
	
	
	
	
}
