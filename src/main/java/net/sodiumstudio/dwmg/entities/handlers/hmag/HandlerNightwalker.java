package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.HashSet;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.sodiumstudio.befriendmobs.entity.befriended.IBefriendedMob;
import net.sodiumstudio.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.dwmg.registries.DwmgBlocks;
import net.sodiumstudio.dwmg.registries.DwmgTags;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.block.ColoredBlocks;
import net.sodiumstudio.nautils.entity.RepeatableAttributeModifier;
import net.sodiumstudio.nautils.math.RndUtil;

public class HandlerNightwalker extends HandlerItemGivingProgress
{
	
	@Override
	protected double getProcValueToAdd(ItemStack itemstack, Player player, Mob mob, double oldProc) {
		if (itemstack.is(Items.CLAY_BALL))
			return RndUtil.rndRangedDouble(0.03, 0.06);
		else if (itemstack.is(ModItems.ANCIENT_STONE.get()))
			return RndUtil.rndRangedDouble(0.05, 0.10);
		else if (itemstack.is(DwmgTags.HMAG_BERRIES))
			return RndUtil.rndRangedDouble(0.06, 0.14);
		else return 0d;
	}

	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return mob.level.getBlockState(mob.blockPosition().below()).is(DwmgTags.CAN_BEFRIEND_NIGHTWALKERS_ON);
	}

	@Override
	public int getItemGivingCooldownTicks() {
		return 10 * 20;
	}

	@Override
	public boolean isItemAcceptable(ItemStack itemstack) {
		return getProcValueToAdd(itemstack, null, null, 0) > 0;
	}

	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		return NaContainerUtils.setOf(BefriendableAddHatredReason.ATTACKED, BefriendableAddHatredReason.ATTACKING);
	}

	@Override
	public int getHatredDurationTicks(BefriendableAddHatredReason reason)
	{
		switch (reason)
		{
		case ATTACKED:
			return 300 * 20;
		case ATTACKING:
			return 30 * 20;
		default:
			return 0;				
		}
	}
	
	@Override
	public void afterItemGiven(Player player, Mob mob, ItemStack item) 
	{
		convertBlockOnGiven(mob.level, mob.blockPosition().below());
		convertBlockOnGiven(mob.level, mob.blockPosition().below().east());
		convertBlockOnGiven(mob.level, mob.blockPosition().below().west());
		convertBlockOnGiven(mob.level, mob.blockPosition().below().south());
		convertBlockOnGiven(mob.level, mob.blockPosition().below().north());
		convertBlockOnGiven(mob.level, mob.blockPosition().below(2));
	}
	
	@Override
	public IBefriendedMob finalActions(Player player, Mob mob)
	{
		afterItemGiven(player, mob, null);
		return super.finalActions(player, mob);
	}
	
	protected void convertBlockOnGiven(Level level, BlockPos pos)
	{
		BlockState blockstate = level.getBlockState(pos);
		if (blockstate.getBlock() == null)
			return;
		Block convertTo = null;
		
		if (blockstate.is(DwmgBlocks.ENHANCED_LUMINOUS_TERRACOTTA.get()))
			convertTo = DwmgBlocks.LUMINOUS_TERRACOTTA.get();
		else if (blockstate.is(DwmgBlocks.LUMINOUS_TERRACOTTA.get()))
			convertTo = ColoredBlocks.GLAZED_TERRACOTTA_BLOCKS.ofRandomColor();
		else if (ColoredBlocks.GLAZED_TERRACOTTA_BLOCKS.contains(blockstate.getBlock()))
			convertTo = ColoredBlocks.TERRACOTTA_BLOCKS.ofColor(ColoredBlocks.GLAZED_TERRACOTTA_BLOCKS.getColor(blockstate.getBlock()));
		else if (ColoredBlocks.TERRACOTTA_BLOCKS.contains(blockstate.getBlock()))
			convertTo = Blocks.CLAY;
		if (convertTo != null)
			level.setBlock(pos, convertTo.defaultBlockState(), 1 | 2);
	}
	
}
