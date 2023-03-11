package com.sodium.dwmg.registries;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.befriendmobs.BefriendMobs;
import com.sodium.dwmg.befriendmobs.entitiy.IBefriendedMob;
import com.sodium.dwmg.befriendmobs.entitiy.capability.CBefriendableMobProvider;
import com.sodium.dwmg.befriendmobs.util.TagHelper;
import com.sodium.dwmg.entities.capabilities.CUndeadMobProvider;

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
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		if(event.getObject() instanceof LivingEntity living)
		{
			if (living.getMobType() == MobType.UNDEAD && !(living instanceof IBefriendedMob) && !TagHelper.hasTag(living, Dwmg.MOD_ID, "ignore_death_affinity"))	// Befriended mobs aren't affected by Death Affinity
				event.addCapability(new ResourceLocation(Dwmg.MOD_ID, "cap_undead"), new CUndeadMobProvider());
		}
	}
	
	// Actions to initialize befriendable mob capability on spawn
	// TODO: make this handler overridable
	/*
	public static void befriendableMobInit(AttachCapabilitiesEvent<Entity> event)
	{
		LivingEntity living = (LivingEntity)event.getObject();
		EntityType<?> type = living.getType();
		
		living.getCapability(RegCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			if (type == DwmgEntityTypes.ZOMBIE_GIRL.get())
			{
				float rnd = new Random().nextFloat();
				l.getNBT().put("cakes_required", IntTag.valueOf(rnd < 0.1 ? 1 : (rnd < 0.4 ? 2 : 3)));
			}
		});
	}
*/
}