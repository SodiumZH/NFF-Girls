package net.sodiumstudio.dwmg.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectNecromancerWither extends MobEffect
{

	public EffectNecromancerWither()
	{
		super(MobEffectCategory.HARMFUL, 3484199);	// Same to wither
	}

	// Handled in EntityEvents
	@Override
	public void applyEffectTick(LivingEntity livingEntityIn, int amplifier) {
	}
	
	// For an effect with a duration
	@Override
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration > 0;
	}
	
	public static int deltaTickPerDamage(int amplifier)
	{
		int factor = 1;
		for (int i = 0; i < amplifier; ++i)
		{
			factor *= 2;
		}
		return Math.max(80 / factor, 1);
	}

}
