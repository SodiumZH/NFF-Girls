package net.sodiumstudio.dwmg.entities;

import java.util.HashSet;
import java.util.function.Predicate;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.item.Item;


import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.befriendmobs.util.Wrapped;
import net.sodiumstudio.befriendmobs.util.annotation.DontCallManually;
import net.sodiumstudio.befriendmobs.util.annotation.DontOverride;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;

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
	
}
