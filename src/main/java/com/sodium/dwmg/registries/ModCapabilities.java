package com.sodium.dwmg.registries;


import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.entities.capabilities.CapUndeadMobProvider;
import com.sodium.dwmg.entities.capabilities.ICapUndeadMob;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCapabilities {

	public static Capability<ICapUndeadMob> CAP_UNDEAD_MOB = CapabilityManager.get(new CapabilityToken<>(){});
	
	// Register capabilities
	@SubscribeEvent
	public static void register(RegisterCapabilitiesEvent event)
	{
		// Entities
		event.register(ICapUndeadMob.class);
	}
	

	
}
