package net.sodiumstudio.dwmg.dwmgcontent.entities.item.baublesystem;

import java.util.HashSet;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public interface IBaubleHolder {

	public HashSet<ItemStack> getBaubleStacks();
	
	public default boolean isBauble(Item item){
		return BaubleHandlerRegistry.get(asMob().getType())
	}
	
	public default Mob asMob()
	{
		return (Mob)this;
	}
}
