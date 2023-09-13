package net.sodiumstudio.dwmg.item;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Nullable;

import com.github.mechalopa.hmag.registry.ModBlocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.sodiumstudio.befriendmobs.entity.befriended.BefriendedHelper;
import net.sodiumstudio.befriendmobs.item.MobRespawnerInstance;
import net.sodiumstudio.befriendmobs.registry.BMItems;
import net.sodiumstudio.nautils.InfoHelper;
import net.sodiumstudio.nautils.NbtHelper;
import net.sodiumstudio.nautils.math.RndUtil;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.registries.DwmgItems;

public class ItemEvilMagnet extends Item implements IWithDuration
{

	public ItemEvilMagnet(Properties pProperties)
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
				Iterable<Entity> entities = sl.getAllEntities();
				List<ItemEntity> respawners = new ArrayList<>();
				for (Entity e: entities)
				{
					if (e instanceof ItemEntity ie)
					{
						if (ie.getItem().is(DwmgItems.MOB_RESPAWNER.get()))
						{
							MobRespawnerInstance mr = MobRespawnerInstance.create(ie.getItem());
							if (mr != null 
									&& BefriendedHelper.getModIdFromNbt(mr.getMobNbt()).equals(Dwmg.MOD_ID)
									&& BefriendedHelper.getOwnerUUIDFromNbt(mr.getMobNbt()).equals(context.getPlayer().getUUID()))
							{
								respawners.add(ie); 							
							}
						}
					}
				}
				respawners = respawners.stream().sorted(Comparator.comparingDouble((ItemEntity ie) -> ie.distanceToSqr(context.getPlayer()))).toList();
				for (ItemEntity ie: respawners)
				{
					if (consumeDuration(context.getItemInHand(), 1))
					{
						ie.moveTo(blockpos.getX() + 0.5 + RndUtil.rndRangedDouble(-0.2,  0.2), blockpos.getY()+ 1.5, blockpos.getZ() + 0.5 + RndUtil.rndRangedDouble(-0.2,  0.2));
					}
					else break;
				}
			}
			return InteractionResult.sidedSuccess(context.getLevel().isClientSide);
		}
		return InteractionResult.PASS;
	}
	
	// ======== IWithDuration interface

	@Override
	public int getMaxDuration() {
		return 8;
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, level, list, tooltipFlag);
		list.add(this.getDurationDescription(stack));
	}

	
}
