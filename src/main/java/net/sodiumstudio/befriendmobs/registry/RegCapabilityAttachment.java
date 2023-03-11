package net.sodiumstudio.befriendmobs.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.entitiy.capability.CBefriendableMobProvider;
import net.sodiumstudio.befriendmobs.util.TagHelper;

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
