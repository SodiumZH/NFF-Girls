package net.sodiumstudio.dwmg.item;

import java.util.List;

import com.github.mechalopa.hmag.registry.ModBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.phys.AABB;
import net.sodiumstudio.befriendmobs.entity.BefriendedHelper;
import net.sodiumstudio.befriendmobs.entity.ai.BefriendedAIState;
import net.sodiumstudio.befriendmobs.item.ItemMobRespawner;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;
import net.sodiumstudio.befriendmobs.registry.BefMobItems;
import net.sodiumstudio.befriendmobs.util.math.RndUtil;
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
				AABB bound = new AABB(blockpos.subtract(new Vec3i(16, 16, 16)), blockpos.offset(new Vec3i(16, 16, 16)));
				List<Entity> bmList = sl.getEntities(null, bound);
				for (Entity e: bmList)
				{
					if (e instanceof IDwmgBefriendedMob bm && bm.getOwnerUUID().equals(context.getPlayer().getUUID()))
						((IDwmgBefriendedMob)e).setAIState(BefriendedAIState.FOLLOW, true);
					else if (e instanceof ItemEntity ie)
					{
						if (ie.getItem().is(BefMobItems.MOB_RESPAWNER.get()))
						{
							MobRespawnerInstance mr = MobRespawnerInstance.create(ie.getItem());
							if (mr != null && BefriendedHelper.getOwnerUUIDFromNbt(mr.getMobNbt()).equals(context.getPlayer().getUUID()))
							{
								ie.moveTo(blockpos.getX() + 0.5 + RndUtil.rndRangedDouble(-0.2,  0.2), blockpos.getY()+ 1.2, blockpos.getZ() + 0.5 + RndUtil.rndRangedDouble(-0.2,  0.2));
							}
						}
					}
				}
			}
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;
	}

}
