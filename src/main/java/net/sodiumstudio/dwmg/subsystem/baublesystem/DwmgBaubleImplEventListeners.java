package net.sodiumstudio.dwmg.subsystem.baublesystem;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.sodiumstudio.befriendmobs.subsystems.baublesystem.BaubleSystem;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.entities.hmag.HmagCrimsonSlaughtererEntity;
import net.sodiumstudio.nautils.EntityHelper;
@EventBusSubscriber(modid = Dwmg.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class DwmgBaubleImplEventListeners
{
	@SubscribeEvent
	public static void poisonousThornImpl(LivingDamageEvent event)
	{
		if (event.getSource().getEntity() != null && event.getSource().getEntity() instanceof Mob mob && event.getAmount() >= 0.01)
		{
			BaubleSystem.ifCapabilityPresent(mob, () -> 
			{
				int poisonLevel = DwmgBaubleStatics.countBaubles(mob, new ResourceLocation("dwmg:poisonous_thorn"));
				if (poisonLevel > 0)
				{
					if (poisonLevel > 0)
						EntityHelper.addEffectSafe(event.getEntity(), MobEffects.POISON, 5 * Math.min(poisonLevel, 3) * 20, Math.min(poisonLevel, 3) - 1);
					if (mob instanceof HmagCrimsonSlaughtererEntity cs)
						EntityHelper.addEffectSafe(event.getEntity(), MobEffects.MOVEMENT_SLOWDOWN, (20 + 10 * poisonLevel) * 20, 1);
				}
			});
		}
	}
	
	
}
