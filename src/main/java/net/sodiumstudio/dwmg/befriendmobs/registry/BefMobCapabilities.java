package net.sodiumstudio.dwmg.befriendmobs.registry;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CAttributeMonitor;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CBefriendableMob;
import net.sodiumstudio.dwmg.befriendmobs.entity.capability.CHealingHandler;
import net.sodiumstudio.dwmg.befriendmobs.item.capability.CMobRespawner;

public class BefMobCapabilities {

	public static Capability<CBefriendableMob> CAP_BEFRIENDABLE_MOB = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<CHealingHandler> CAP_HEALING_HANDLER = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<CMobRespawner> CAP_MOB_RESPAWNER = CapabilityManager.get(new CapabilityToken<>(){});
	public static Capability<CAttributeMonitor> CAP_ATTRIBUTE_MONITOR = CapabilityManager.get(new CapabilityToken<>(){});
	
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		// Entities
		event.register(CBefriendableMob.class);
		event.register(CHealingHandler.class);
		event.register(CMobRespawner.class);
		event.register(CAttributeMonitor.class);
	}
}
