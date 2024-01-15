package net.sodiumstudio.dwmg.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.befriendmobs.entity.befriending.handlerpreset.HandlerItemGivingProgress;
import net.sodiumstudio.befriendmobs.entity.befriending.registry.BefriendingTypeRegistry;
import net.sodiumstudio.befriendmobs.registry.BMCaps;
import net.sodiumstudio.dwmg.entities.handlers.hmag.HandlerItemDropping;
import net.sodiumstudio.nautils.NaMiscUtils;

public class BefriendingProgressProbeItem extends Item
{

	public BefriendingProgressProbeItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity target, InteractionHand hand) 
	{
		if (!player.level.isClientSide)
		{
			if (target instanceof Mob mob && target.getCapability(BMCaps.CAP_BEFRIENDABLE_MOB).isPresent())
			{
				if (BefriendingTypeRegistry.getHandler(mob) instanceof HandlerItemGivingProgress prog)
				{
					NaMiscUtils.printToScreen("Progress: " + Double.toString(prog.getProgressValue(mob, player)), player);
					return InteractionResult.CONSUME;
				}
				else if (BefriendingTypeRegistry.getHandler(mob) instanceof HandlerItemDropping dropping)
				{
					NaMiscUtils.printToScreen("Progress: " + Double.toString(dropping.getProgress(mob, player)), player);
					return InteractionResult.CONSUME;
				}
			}
		}
		return InteractionResult.PASS;
	}
}
