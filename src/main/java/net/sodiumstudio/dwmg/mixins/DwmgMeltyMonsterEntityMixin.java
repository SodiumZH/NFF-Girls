package net.sodiumstudio.dwmg.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import com.github.mechalopa.hmag.world.entity.MeltyMonsterEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.sodiumstudio.dwmg.events.hooks.DwmgHooks;
import net.sodiumstudio.nautils.mixins.NaUtilsMixin;

@Mixin(MeltyMonsterEntity.class)
public class DwmgMeltyMonsterEntityMixin implements NaUtilsMixin<MeltyMonsterEntity>
{

	@Redirect(method = "aiStep()V", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z"))
	private boolean onSetFire(Level caller, BlockPos pos, BlockState state)
	{
		if (!MinecraftForge.EVENT_BUS.post(new DwmgHooks.MeltyMonsterSetFireEvent(get())))
		{
			return false;
		}
		else return caller.setBlockAndUpdate(pos, BaseFireBlock.getState(caller, pos));
	}
}
