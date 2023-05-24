package net.sodiumstudio.dwmg.entities;

import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Predicate;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.item.Item;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.item.baublesystem.IBaubleHolder;
import net.sodiumstudio.befriendmobs.util.Wrapped;
import net.sodiumstudio.dwmg.entities.capabilities.CFavorabilityHandler;
import net.sodiumstudio.dwmg.entities.capabilities.CLevelHandler;
import net.sodiumstudio.dwmg.registries.DwmgCapabilities;

public interface IDwmgBefriendedMob extends IBefriendedMob, IBaubleHolder
{
	
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

	
	
	
}
