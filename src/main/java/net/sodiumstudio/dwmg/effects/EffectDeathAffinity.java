package net.sodiumstudio.dwmg.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;


/**
 * Death Affinity makes undead mobs neutral to the effected mob
 */

public class EffectDeathAffinity extends MobEffect {

	public EffectDeathAffinity() {
		super(MobEffectCategory.BENEFICIAL, 0X483D8B /*DarkSlateBlue*/ );
	}
	
	// No explicit effect here. Configured in ServerEvents.
	public void applyEffectTick(LivingEntity livingEntityIn, int amplifier) {
	}
	
	// For an effect with a duration
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration > 0;
	}
	
}
