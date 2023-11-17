package net.sodiumstudio.dwmg.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.github.mechalopa.hmag.world.entity.JackFrostEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.dwmg.events.hooks.DwmgHooks;
import net.sodiumstudio.nautils.mixins.NaUtilsMixin;

@Mixin(JackFrostEntity.class)
public class JackFrostEntityMixin implements NaUtilsMixin<JackFrostEntity>
{
	@Inject(method = "isMeltingBiome(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/world/level/Level;)Z", at = @At("HEAD"), cancellable = true, remap = false)
	private static void isMeltingBiome(Entity e, Level level, CallbackInfoReturnable<Boolean> cir)
	{
		if (e instanceof JackFrostEntity jf)
		{
			if (MinecraftForge.EVENT_BUS.post(new DwmgHooks.JackFrostCheckMeltingBiomeEvent(jf)))
			{
				cir.setReturnValue(false);
			}
		}
	}
}
