package net.sodiumzh.nff.girls.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.common.ToolAction;
import net.minecraftforge.common.ToolActions;

public class UnsweepableSwordItem extends SwordItem
{

	public UnsweepableSwordItem(Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier,
			Properties pProperties)
	{
		super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
	}

	@Override
	public boolean canPerformAction(ItemStack stack, ToolAction toolAction) {
		return toolAction == ToolActions.SWORD_DIG;
	}

}
