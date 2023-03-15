package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class DwmgFoodProperties {

	public static final FoodProperties SOUL_CAKE_SLICE = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.1F).effect(() -> new MobEffectInstance(DwmgEffects.DEATH_AFFINITY.get(), 2400), 1.0f).build();
	
	
}
