package net.sodiumstudio.dwmg.dwmgcontent.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectEnderProtection extends MobEffect
{

	public EffectEnderProtection() {
		super(MobEffectCategory.BENEFICIAL, 0XC900FA /* Color of ender man eye */ );
	}
	
	// No explicit effect here. Configured in ServerEvents.
	public void applyEffectTick(LivingEntity livingEntityIn, int amplifier) {
	}
	
	// For an effect with a duration
	public boolean isDurationEffectTick(int duration, int amplifier) {
		return duration > 0;
	}
	
}
