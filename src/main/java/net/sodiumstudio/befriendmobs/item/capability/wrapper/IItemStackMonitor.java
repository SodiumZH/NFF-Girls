package net.sodiumstudio.befriendmobs.item.capability.wrapper;

import java.util.function.Supplier;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.item.capability.CItemStackMonitor;
import net.sodiumstudio.befriendmobs.registry.BMCaps;

/**
 * A wrapper interface of {@link CItemStackMonitor}. Living Entities implementing this interface will be automatically 
 * attached {@link CItemStackMonitor} capability.
 */
public interface IItemStackMonitor
{

	public default void addItemStack(String key, Supplier<ItemStack> itemStackAccess)
	{
		((LivingEntity)this).getCapability(BMCaps.CAP_ITEM_STACK_MONITOR).ifPresent(cap -> cap.listen(key, itemStackAccess));
	}
	
	/**
	 * @param key Monitoring key.
	 * @param from Copy of the old item stack.
	 * @param to Copy of the new item stack.
	 */
	public void onItemStackChange(String key, ItemStack from, ItemStack to);
	
}
