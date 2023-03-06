package com.sodium.dwmg.befriendmobsapi.registry;

import com.sodium.dwmg.befriendmobsapi.entitiy.capability.CBefriendableMob;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RegCapabilities {

	public static Capability<CBefriendableMob> CAP_BEFRIENDABLE_MOB = CapabilityManager.get(new CapabilityToken<>(){});
	
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		// Entities
		event.register(CBefriendableMob.class);
	}
}
