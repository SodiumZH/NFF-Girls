package net.sodiumstudio.dwmg.registries;

import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.effects.EffectEnderProtection;
import net.sodiumstudio.dwmg.effects.EffectUndeadAffinity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DwmgEffects {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Dwmg.MOD_ID);
	
	// Registry body
	
	public static final RegistryObject<MobEffect> UNDEAD_AFFINITY = EFFECTS.register("undead_affinity", () -> new EffectUndeadAffinity());
	public static final RegistryObject<MobEffect> ENDER_PROTECTION = EFFECTS.register("ender_protection", () -> new EffectEnderProtection());
}
