package net.sodiumstudio.dwmg.dwmgcontent.registries;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.dwmgcontent.Dwmg;
import net.sodiumstudio.dwmg.dwmgcontent.effects.EffectDeathAffinity;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEffects {

	public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Dwmg.MOD_ID);
	

	// Registry body
	
	public static final RegistryObject<MobEffect> DEATH_AFFINITY = EFFECTS.register("death_affinity", () -> new EffectDeathAffinity());

}
