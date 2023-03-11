package com.sodium.dwmg.befriendmobs.registry;

import com.sodium.dwmg.befriendmobs.BefriendMobs;
import com.sodium.dwmg.befriendmobs.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import com.sodium.dwmg.befriendmobs.entitiy.capability.CBefriendableMobProvider;
import com.sodium.dwmg.befriendmobs.util.TagHelper;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RegCapabilityAttachment {

	// Attach capabilities
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof LivingEntity living) {
			if (BefriendingTypeRegistry.contains(living.getType())
					&& !(living instanceof IBefriendedMob)) {
				event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_befriendable"),
						new CBefriendableMobProvider());
				// TODO: add an event here
			}
		}
	}

}
