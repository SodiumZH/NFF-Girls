package com.sodium.dwmg.registries;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.entities.capabilities.CapUndeadMobProvider;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilityAttachment {

	// Attach capabilities
	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		Entity entity = event.getObject();
		if(entity instanceof LivingEntity mob)
		{
			if (mob.getMobType() == MobType.UNDEAD)
					event.addCapability(new ResourceLocation(Dwmg.MODID, "cap_undead"), new CapUndeadMobProvider());
		}
	}
	
}
