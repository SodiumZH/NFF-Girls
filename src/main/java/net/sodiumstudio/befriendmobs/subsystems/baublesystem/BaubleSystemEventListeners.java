package net.sodiumstudio.befriendmobs.subsystems.baublesystem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.sodiumstudio.befriendmobs.BefriendMobs;
import net.sodiumstudio.nautils.annotation.DontCallManually;

@Mod.EventBusSubscriber(modid = BefriendMobs.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
class BaubleSystemEventListeners
{

	@SuppressWarnings("unchecked")
	@DontCallManually
	@SubscribeEvent
	public static void attachLivingEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof Mob mob)
		{
			if (BaubleEquippableMobRegistries.containsMobType(mob.getClass()))
			{
				CBaubleEquippableMobPrvd prvd = new CBaubleEquippableMobPrvd(mob);
				event.addCapability(new ResourceLocation(BefriendMobs.MOD_ID, "cap_bauble_equippable_mob")
						, prvd);
			}
		}
	}
	
	@DontCallManually
	@SubscribeEvent
	public static void onLivingTick(LivingTickEvent event)
	{
		event.getEntity().getCapability(BaubleSystemCapabilities.CAP_BAUBLE_EQUIPPABLE_MOB).ifPresent(cap -> cap.tick());
	}
}
