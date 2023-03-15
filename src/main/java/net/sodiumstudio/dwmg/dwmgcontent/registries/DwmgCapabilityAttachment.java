package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.dwmg.befriendmobs.BefriendMobs;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.IBefriendedMob;
import net.sodiumstudio.dwmg.befriendmobs.entitiy.capability.CBefriendableMobProvider;
import net.sodiumstudio.dwmg.befriendmobs.util.TagHelper;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.entities.capabilities.CUndeadMobProvider;

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

}