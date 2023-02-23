package com.sodium.dwmg.registries;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.entities.IBefriendedMob;
import com.sodium.dwmg.entities.capabilities.CapBefriendableMobProvider;
import com.sodium.dwmg.entities.capabilities.CapUndeadMobProvider;
import com.sodium.dwmg.util.NbtHelper;
import com.sodium.dwmg.util.TagHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

import com.github.mechalopa.hmag.registry.ModEntityTypes;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCapabilityAttachment {

	// Attach capabilities
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<LivingEntity> event)
	{
		LivingEntity living = event.getObject();

		if (living.getMobType() == MobType.UNDEAD && !(living instanceof IBefriendedMob) && !TagHelper.hasTag(living, "dwmg", "ignore_death_affinity"))	// Befriended mobs aren't affected by Death Affinity
			event.addCapability(new ResourceLocation(Dwmg.MOD_ID, "cap_undead"), new CapUndeadMobProvider());
		if (TagHelper.hasTag(living, "dwmg", "befriendable") && !(living instanceof IBefriendedMob))
		{
			// TODO: make this action overridable
			event.addCapability(new ResourceLocation(Dwmg.MOD_ID, "cap_befriendable"), new CapBefriendableMobProvider());
			befriendableMobInit(event);
		}
	}
	
	// Actions to initialize befriendable mob capability on spawn
	// TODO: make this method overridable
	public static void befriendableMobInit(AttachCapabilitiesEvent<LivingEntity> event)
	{
		LivingEntity living = event.getObject();
		EntityType<?> type = living.getType();
		
		living.getCapability(ModCapabilities.CAP_BEFRIENDABLE_MOB).ifPresent((l) -> {
			l.getNBT().put("player_data", new CompoundTag());
			if (type == ModEntityTypes.ZOMBIE_GIRL.get())
			{
				float rnd = new Random().nextFloat();
				l.getNBT().put("cakes_needed", IntTag.valueOf(rnd < 0.1 ? 1 : (rnd < 0.4 ? 2 : 3)));
			}
		});
	}
	
}
