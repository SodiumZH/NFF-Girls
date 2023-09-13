package net.sodiumstudio.dwmg.entities;


import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.item.MobRespawnerItem;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.nautils.Wrapped;
import net.sodiumstudio.nautils.annotation.DontCallManually;
import net.sodiumstudio.nautils.annotation.DontOverride;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.entities.item.baublesystem.DwmgBaubleItem;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public interface IDwmgBefriendedMob extends IBefriendedMob, IBaubleHolder
{
	
	@DontOverride
	public default CFavorabilityHandler getFavorability()
	{
		Wrapped<CFavorabilityHandler> cap = new Wrapped<CFavorabilityHandler>(null);
		asMob().getCapability(DwmgCapabilities.CAP_FAVORABILITY_HANDLER).ifPresent((c) -> 
		{
			cap.set(c);
		});
		if (cap.get() == null)
			throw new IllegalStateException("Missing CFavorabilityHandler capability");
		return cap.get();
	}
	
	@DontOverride
	public default CLevelHandler getLevelHandler()
	{
		Wrapped<CLevelHandler> cap = new Wrapped<CLevelHandler>(null);
		asMob().getCapability(DwmgCapabilities.CAP_LEVEL_HANDLER).ifPresent((c) -> 
		{
			cap.set(c);
		});
		if (cap.get() == null)
			throw new IllegalStateException("Missing CLevelHandler capability");
		return cap.get();
	}

	@DontOverride
	@DontCallManually
	public default void touchEntity(Entity other)
	{
		MinecraftForge.EVENT_BUS.post(new OverlapEntityEvent(this, other));
	}
	
	@Override
	public default double getAnchoredStrollRadius()  
	{
		return 16.0d;
	}
	
	/**
	 * Fired EVERY TICK when a befriended mob overlaps an entity.
	 */
	public static class OverlapEntityEvent extends Event
	{
		public final IBefriendedMob thisMob;
		public final Entity touchedEntity;
		public OverlapEntityEvent(IBefriendedMob thisMob, Entity touchedEntity)
		{
			this.thisMob = thisMob;
			this.touchedEntity = touchedEntity;
		}
	}

	/* Bauble related */
	public default boolean hasDwmgBauble(String typeName)
	{
		for (ItemStack stack: this.getBaubleSlots().values())
		{
			if (!stack.isEmpty() && stack.getItem() instanceof DwmgBaubleItem bauble && bauble.is(typeName))
				return true;
		}
		return false;
	}
	
	public default boolean hasDwmgBaubleWithLevel(String typeName, int level)
	{
		for (ItemStack stack: this.getBaubleSlots().values())
		{
			if (!stack.isEmpty() && stack.getItem() instanceof DwmgBaubleItem bauble && bauble.is(typeName, level))
				return true;
		}
		return false;
	}
	
	public default boolean hasDwmgBaubleWithMinLevel(String typeName, int minLevel)
	{
		for (ItemStack stack: this.getBaubleSlots().values())
		{
			if (!stack.isEmpty() 
				&& stack.getItem() instanceof DwmgBaubleItem bauble 
				&& bauble.is(typeName)
				&& bauble.getLevel() >= minLevel)
			{
				return true;
			}
		}
		return false;
	}
	
	// === IBefriendedMob interface
	@Override
	public default MobRespawnerItem getRespawnerType()
	{
		return DwmgItems.MOB_RESPAWNER.get();
	}
}
