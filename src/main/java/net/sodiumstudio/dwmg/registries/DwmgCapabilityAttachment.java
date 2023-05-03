package net.sodiumstudio.dwmg.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.befriendmobs.entity.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.capability.CAttributeMonitor;
import net.sodiumstudio.befriendmobs.entity.capability.CBefriendableMobProvider;
import net.sodiumstudio.befriendmobs.entity.capability.LivingSetupAttributeMonitorEvent;
import net.sodiumstudio.befriendmobs.util.TagHelper;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.capabilities.CUndeadMobProvider;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgCapabilityAttachment {

	// Attach capabilities
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof LivingEntity living)
		{
			if (living.getMobType() == MobType.UNDEAD && !(living instanceof IBefriendedMob) && !TagHelper.hasTag(living, Dwmg.MOD_ID, "ignore_death_affinity"))	// Befriended mobs aren't affected by Death Affinity
				event.addCapability(new ResourceLocation(Dwmg.MOD_ID, "cap_undead"), new CUndeadMobProvider());
			

		}
	}
	
	@SubscribeEvent
	public static void setupAttributeMonitor(LivingSetupAttributeMonitorEvent event)
	{
		if (event.living instanceof IBefriendedMob b && b.getModId().equals(Dwmg.MOD_ID))
		{
			event.addListen(Attributes.MAX_HEALTH);
		}
	}
}

