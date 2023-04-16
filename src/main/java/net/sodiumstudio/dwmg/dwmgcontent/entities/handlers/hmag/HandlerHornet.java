package net.sodiumstudio.dwmg.dwmgcontent.entities.handlers.hmag;

import java.util.HashSet;
import java.util.stream.Stream;

import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableAddHatredReason;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableMobInteractArguments;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.BefriendableMobInteractionResult;
import net.sodiumstudio.dwmg.befriendmobs.entity.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.dwmg.befriendmobs.util.math.RndUtil;

public class HandlerHornet extends HandlerItemGivingProgress
{

	@Override
	protected double getProcValueToAdd(ItemStack item) {
		if (item.is(Items.HONEY_BOTTLE))
			return RndUtil.rndRangedDouble(0.04d, 0.08d);
		else if (item.is(Items.HONEY_BLOCK))
			return RndUtil.rndRangedDouble(0.08d, 0.16d);
		else return 0;
	}

	@Override
	public boolean isItemAcceptable(Item item) {
		return item.equals(Items.HONEY_BOTTLE)
				|| item.equals(Items.HONEY_BLOCK);
	}

	@Override
	public BefriendableMobInteractionResult handleInteract(BefriendableMobInteractArguments args) 
	{
		// Get if using honey bottle
		ItemStack stack = args.getPlayer().getItemInHand(args.getHand());
		boolean flag = stack.is(Items.HONEY_BOTTLE);
		int count = stack.getCount();
		
		BefriendableMobInteractionResult res = super.handleInteract(args);
		
		// If consumed honey bottle, drop a glass bottle
		if (!args.isClient() && args.getPlayer().getItemInHand(args.getHand()).getCount() != count && flag)
		{
			if (!args.getPlayer().addItem(new ItemStack(Items.GLASS_BOTTLE, 1)))
				args.getPlayer().spawnAtLocation(new ItemStack(Items.GLASS_BOTTLE, 1));
		}
		return res;
	}
	
	@Override
	public boolean additionalConditions(Player player, Mob mob) {
		return !player.hasEffect(MobEffects.POISON) && has8HoneyBlocksAround(mob);
	}

	public boolean has8HoneyBlocksAround(Mob mob)
	{
		BlockPos pos = mob.blockPosition();
		// Search 9x9x9 area centered by mob
		AABB searchArea = new AABB(pos.getX() - 4, pos.getY() - 4, pos.getZ() - 4, pos.getX() + 4, pos.getY() + 4, pos.getZ() + 4);
		Stream<BlockState> blocks = mob.level.getBlockStates(searchArea);
		long count = blocks.filter(b -> b.is(Blocks.HONEY_BLOCK)).count();
		return count >= 8;
	}
	
	@Override
	public int getItemGivingCooldownTicks() {
		return 200;
	}
	
	@Override
	public void serverTick(Mob mob)
	{
		if (!has8HoneyBlocksAround(mob))
			this.interruptAll(mob, false);
	}

	@Override
	public HashSet<BefriendableAddHatredReason> getAddHatredReasons() {
		HashSet<BefriendableAddHatredReason> set = new HashSet<BefriendableAddHatredReason>();
		set.add(BefriendableAddHatredReason.ATTACKED);
		set.add(BefriendableAddHatredReason.ATTACKING);
		return set;
	}
	
	@Override
	public int getHatredDurationTicks(BefriendableAddHatredReason reason)
	{
		switch (reason)
		{
		case ATTACKED:
			return 18000;
		case ATTACKING:
			return 1200; 
		default:
			return 0;				
		}
	}
}
