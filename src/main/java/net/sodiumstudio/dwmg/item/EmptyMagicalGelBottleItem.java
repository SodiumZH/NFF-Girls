package net.sodiumstudio.dwmg.item;

import java.util.Optional;

import com.github.mechalopa.hmag.registry.ModEntityTypes;
import com.github.mechalopa.hmag.world.entity.MagicalSlimeEntity;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.nautils.math.LinearColor;

public class EmptyMagicalGelBottleItem extends Item
{

	public EmptyMagicalGelBottleItem(Properties pProperties)
	{
		super(pProperties);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity living, InteractionHand usedHand)
	{
		if (!player.level.isClientSide)
		{
			Optional<LinearColor> color = Optional.empty();
			// Blend magical slime
			if (living instanceof MagicalSlimeEntity ms && living.getType() == ModEntityTypes.MAGICAL_SLIME.get())
			{
				if (ms.isTiny())
				{
					color = Optional.of(MagicalGelColorUtils.getSlimeColor(ms));
					ms.discard();
				}
			}
		/*	else if (living instanceof Slime sl && living.getType() == EntityType.SLIME)
			{
				if (sl.isTiny())
				{
					color = Optional.of(MagicalGelBottleItem.VANILLA_SLIME_COLOR);
					sl.discard();
				}
			}
			else if (living instanceof MagmaCube mc && living.getType() == EntityType.MAGMA_CUBE)
			{
				if (mc.isTiny())
				{
					color = Optional.of(MagicalGelBottleItem.MAGMA_CUBE_COLOR);
					mc.discard();
				}
			} 			*/
			if (color.isPresent())
			{
				stack.shrink(1);
				ItemStack stack1 = MagicalGelBottleItem.create(DwmgItems.MAGICAL_GEL_BOTTLE.get(), color.get());
				if (!player.addItem(stack1))
					player.spawnAtLocation(stack1);
				return InteractionResult.sidedSuccess(player.level.isClientSide);
			}
		}
		return InteractionResult.PASS;
	}
}
