package net.sodiumstudio.dwmg.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.CarpetBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumstudio.nautils.EntityHelper;
import net.sodiumstudio.dwmg.registries.DwmgBlocks;
import net.sodiumstudio.dwmg.registries.DwmgEffects;

/**
 * Soul Carpet is a carpet which provides mobs Death Affinity effect on it
 */

public class BlockSoulCarpet extends CarpetBlock 
{
	public BlockSoulCarpet(Properties prop) 
	{
		super(prop);
	}

	@Override
	@SuppressWarnings("deprecation")
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) 
	{
		if (!level.isClientSide && entityIn != null && entityIn instanceof LivingEntity)
		{
			LivingEntity livingentity = (LivingEntity) entityIn;
			EntityHelper.addEffectIfNotHaving(livingentity, new MobEffectInstance(DwmgEffects.UNDEAD_AFFINITY.get(), 3, 0, true, true));
			// If player has <2s Wither I (usually brought by Necromancer's Hat), remove it
			if (livingentity.hasEffect(DwmgEffects.NECROMANCER_WITHER.get()) 
					&& livingentity.getEffect(DwmgEffects.NECROMANCER_WITHER.get()).getAmplifier() == 0
					&& livingentity.getEffect(DwmgEffects.NECROMANCER_WITHER.get()).getDuration() <= 40)
			{
				livingentity.removeEffect(DwmgEffects.NECROMANCER_WITHER.get());
			}
		}
	}
	
	public static boolean isEntityInside(LivingEntity living)
	{

		return living.level.getBlockState(living.blockPosition()).is(DwmgBlocks.SOUL_CARPET.get());
	}
	
}
