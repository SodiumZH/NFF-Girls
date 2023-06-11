package net.sodiumstudio.dwmg.item;

import java.util.List;

import com.github.mechalopa.hmag.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.dwmg.entities.IDwmgBefriendedMob;

public class ItemCommandWand extends Item
{

	public ItemCommandWand(Properties pProperties)
	{
		super(pProperties);
	}
	
	@Override
	@SuppressWarnings("resource")
	public InteractionResult useOn(UseOnContext context)
	{
		if (context.getLevel().getBlockState(context.getClickedPos()).is(ModBlocks.EVIL_CRYSTAL_BLOCK.get()))
		{
			if (!context.getLevel().isClientSide && context.getLevel() instanceof ServerLevel sl)
			{
				BlockPos blockpos = context.getClickedPos();
				AABB bound = new AABB(blockpos.subtract(new Vec3i(8, 8, 8)), blockpos.offset(new Vec3i(8, 8, 8)));
				List<Entity> bmList = sl.getEntities(null, bound).stream().filter(e -> (e instanceof IDwmgBefriendedMob)).toList();
				for (Entity e: bmList)
				{
					((IDwmgBefriendedMob)e).setAIState(BefriendedAIState.FOLLOW, true);
				}
			}
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;
	}

}
