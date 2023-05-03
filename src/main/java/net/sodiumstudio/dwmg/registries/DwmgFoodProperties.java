package net.sodiumstudio.dwmg.registries;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class DwmgFoodProperties {

	public static final FoodProperties SOUL_CAKE_SLICE = (new FoodProperties.Builder()).nutrition(9).saturationMod(0.1F).effect(() -> new MobEffectInstance(DwmgEffects.UNDEAD_AFFINITY.get(), 1800), 1.0f).alwaysEat().build();
	public static final FoodProperties ENDERBERRY = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.1F).build();
	public static final FoodProperties ENDER_PIE = (new FoodProperties.Builder()).nutrition(16).saturationMod(0.6F).effect(() -> new MobEffectInstance(DwmgEffects.ENDER_PROTECTION.get(), 600), 1.0f).alwaysEat().build();

}
