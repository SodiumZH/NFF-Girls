package com.sodium.dwmg.registries;

import com.sodium.dwmg.Dwmg;
import com.sodium.dwmg.effects.EffectDeathAffinity;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEffects {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Dwmg.MODID);
	

	// Registry body
	
	public static final RegistryObject<MobEffect> DEATH_AFFINITY = EFFECTS.register("death_affinity", () -> new EffectDeathAffinity());

}
