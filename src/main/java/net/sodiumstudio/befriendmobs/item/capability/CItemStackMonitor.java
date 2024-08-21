package net.sodiumstudio.befriendmobs.item.capability;

import java.util.HashMap;
import java.util.function.Supplier;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.item.capability.wrapper.IItemStackMonitor;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.nautils.annotation.DontOverride;

public interface CItemStackMonitor {

	public LivingEntity getLiving();
	
	public HashMap<String, Supplier<ItemStack>> getListenedStacks();
	
	public void listen(String key, Supplier<ItemStack> getItem);
	
	@DontOverride
	public default void onChanged(String key, ItemStack from, ItemStack to)
	{
		MinecraftForge.EVENT_BUS.post(new ChangeEvent(getLiving(), key, from, to));
	}
		
	public void tick();
	
	//---------------------------------
	
	public static class ChangeEvent extends Event
	{
		public final LivingEntity living;
		public final String key;
		public final ItemStack from;
		public final ItemStack to;
		public ChangeEvent(LivingEntity living, String key, ItemStack from, ItemStack to)
		{
			this.living = living;
			this.key = key;
			this.from = from;
			this.to = to;
		}
	}
	
	//----------------------------------
	
	public static class Impl implements CItemStackMonitor
	{

		protected final LivingEntity living;
		protected HashMap<String, Supplier<ItemStack>> listened = new HashMap<>();
		protected HashMap<String, ItemStack> stacksLastTick = new HashMap<String, ItemStack>();
		
		public Impl(LivingEntity living)
		{
			this.living = living;
		}
		
		@Override
		public LivingEntity getLiving() {
			return living;
		}

		@Override
		public HashMap<String, Supplier<ItemStack>> getListenedStacks() {
			return listened;
		}

		@Override
		public void listen(String key, Supplier<ItemStack> getItem) {
			listened.put(key, getItem);
			ItemStack current = getItem.get().copy();
			stacksLastTick.put(key, current == null ? ItemStack.EMPTY : current);
		}

		@Override
		public void tick() {	
			for (String key: listened.keySet())
			{
				ItemStack newStack = listened.get(key).get().copy();
				if (newStack == null)
					newStack = ItemStack.EMPTY;
				if (!newStack.equals(stacksLastTick.get(key), false))
				{
					onChanged(key, stacksLastTick.get(key).copy(), newStack.copy());
					if (this.getLiving() instanceof IItemStackMonitor i)
					{
						i.onItemStackChange(key, stacksLastTick.get(key).copy(), newStack.copy());
					}
					stacksLastTick.put(key, newStack.copy());
				}
			}
		}
	}
	
	//------------------------------------------
	
	public static class Prvd implements ICapabilityProvider
	{
		public final CItemStackMonitor monitor;
		
		public Prvd(LivingEntity living)
		{
			monitor = new CItemStackMonitor.Impl(living);
		}
		
		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			if(cap == BMCaps.CAP_ITEM_STACK_MONITOR)
				return LazyOptional.of(() -> {return this.monitor;}).cast();
			else
				return LazyOptional.empty();
		}
	}
	
	//---------------------------------------------
	
	public static class SetupEvent extends Event
	{
		public final LivingEntity living;
		public final CItemStackMonitor monitor;
		public SetupEvent(LivingEntity living, CItemStackMonitor monitor)
		{
			this.living = living;
			this.monitor = monitor;
		}
		
	}
	
	
}
