package net.sodiumzh.nff.girls.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;
import com.llamalad7.mixinextras.injector.WrapWithCondition;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumzh.nautils.mixin.NaUtilsMixin;
import net.sodiumzh.nff.girls.eventlisteners.NFFGirlsHooks;

@Mixin(JackFrostEntity.class)
public class NFFGirlsJackFrostEntityMixin implements NaUtilsMixin<JackFrostEntity>
{
	@WrapWithCondition(method = "aiStep()V", at = @At(value = "INVOKE", 
			target = "Lcom/github/mechalopa/hmag/world/entity/JackFrostEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	private boolean allowMelting(JackFrostEntity entity, DamageSource dmg, float amount)
	{
		return !MinecraftForge.EVENT_BUS.post(new NFFGirlsHooks.JackFrostCheckMeltingBiomeEvent(entity));
	}
}
