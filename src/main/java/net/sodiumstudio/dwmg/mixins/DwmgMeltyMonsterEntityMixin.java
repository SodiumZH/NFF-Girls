package net.sodiumstudio.dwmg.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import com.github.mechalopa.hmag.world.entity.MeltyMonsterEntity;
import com.llamalad7.mixinextras.injector.WrapWithCondition;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.dwmg.events.hooks.DwmgHooks;
import net.sodiumstudio.nautils.mixins.NaUtilsMixin;

@Mixin(MeltyMonsterEntity.class)
public class DwmgMeltyMonsterEntityMixin implements NaUtilsMixin<MeltyMonsterEntity>
{

	@WrapWithCondition(method = "aiStep()V", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"), expect = -1)
	private boolean canSetFire(Level instance, BlockPos pos, BlockState blockstate)
	{
		return !MinecraftForge.EVENT_BUS.post(new DwmgHooks.MeltyMonsterSetFireEvent(caller(), pos, blockstate));	
	}
}
