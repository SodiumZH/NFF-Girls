package net.sodiumstudio.dwmg.registries;

import com.github.mechalopa.hmag.HMaG;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.Dwmg;

public class DwmgPotions
{
	public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Dwmg.MOD_ID);
	
	/*public static final RegistryObject<Potion> UNDEAD_AFFINITY = POTIONS.register("dwmg.undead_affinity", 
			() -> new Potion(new MobEffectInstance(DwmgEffects.UNDEAD_AFFINITY.get(), 180 * 20, 0)));
	public static final RegistryObject<Potion> UNDEAD_AFFINITY_LONG = POTIONS.register("dwmg.undead_affinity_long", 
			() -> new Potion(new MobEffectInstance(DwmgEffects.UNDEAD_AFFINITY.get(), 480 * 20, 0)));*/
}
