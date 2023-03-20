package net.sodiumstudio.dwmg.befriendmobs.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.capability.CBefriendableMobProvider;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BefMobCapabilityAttachment {

	// Attach capabilities
	@SuppressWarnings("unchecked")
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Mob mob) {
			if (BefriendingTypeRegistry.contains((EntityType<? extends Mob>) mob.getType())
					&& !(mob instanceof IBefriendedMob)) {
				event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_befriendable"),
						new CBefriendableMobProvider(mob));
				// TODO: add an event here
			}
		}
	}

}
