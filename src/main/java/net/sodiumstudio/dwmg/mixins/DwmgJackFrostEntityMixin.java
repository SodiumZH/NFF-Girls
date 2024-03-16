package net.sodiumstudio.dwmg.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.dwmg.events.hooks.DwmgHooks;
import net.sodiumstudio.nautils.mixins.NaUtilsMixin;

@Mixin(JackFrostEntity.class)
public class DwmgJackFrostEntityMixin implements NaUtilsMixin<JackFrostEntity>
{
	@WrapWithCondition(method = "aiStep()V", at = @At(value = "INVOKE", 
			target = "com/github/mechalopa/hmag/world/entity/JackFrostEntity.hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"),
			expect = -1)
	private boolean canTakeMeltingBiomeDamage(JackFrostEntity caller, DamageSource dmgSource, float value)
	{
		return !MinecraftForge.EVENT_BUS.post(new DwmgHooks.JackFrostCheckMeltingBiomeEvent(caller));
	}
}
