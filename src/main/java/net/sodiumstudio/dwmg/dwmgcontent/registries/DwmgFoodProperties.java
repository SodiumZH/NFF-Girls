package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;

public class DwmgFoodProperties {

	public static final FoodProperties SOUL_CAKE_SLICE = (new FoodProperties.Builder()).nutrition(7).saturationMod(0.1F).effect(() -> new MobEffectInstance(DwmgEffects.UNDEAD_AFFINITY.get(), 1800), 1.0f).build();
	public static final FoodProperties ENDERBERRY = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.1F).build();
	//public static final FoodProperties ENDERBERRY_JAM = (new FoodProperties.Builder()).nutrition(6).saturationMod(0.1F).build();
}
