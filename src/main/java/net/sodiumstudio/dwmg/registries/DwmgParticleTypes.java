package net.sodiumstudio.dwmg.registries;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.Dwmg;

public class DwmgParticleTypes
{
	public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, Dwmg.MOD_ID);
	
	public static final RegistryObject<SimpleParticleType> MAGICAL_GEL_BALL = PARTICLE_TYPES.register("magical_gel_ball", () -> new SimpleParticleType(false));
}
