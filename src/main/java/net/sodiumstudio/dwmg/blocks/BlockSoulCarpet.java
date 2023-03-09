package net.sodiumstudio.dwmg.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumstudio.dwmg.registries.ModEffects;

/**
 * Soul Carpet is a carpet which provides mobs 30s Death Affinity effect on it
 */

public class BlockSoulCarpet extends CarpetBlock 
{
	public BlockSoulCarpet(Properties prop) 
	{
		super(prop);
	}

	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) 
	{
		if (!level.isClientSide && entityIn != null && entityIn instanceof LivingEntity)
		{
			LivingEntity livingentity = (LivingEntity) entityIn;
			if (!livingentity.hasEffect(ModEffects.DEATH_AFFINITY.get()))
			{
				livingentity.addEffect(new MobEffectInstance(ModEffects.DEATH_AFFINITY.get(), 600, 0));
			}
		}
	}
}
