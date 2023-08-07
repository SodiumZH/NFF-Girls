package net.sodiumstudio.dwmg.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.registries.DwmgRecipes;

public class MagicalGelEnchantRecipe extends SimpleModificationRecipe
{

	public MagicalGelEnchantRecipe(ResourceLocation pId)
	{
		super(pId);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DwmgRecipes.MAGICAL_GEL_ENCHANT.get();
	}

	@Override
	public boolean isSubjectItem(ItemStack stack) {
		return stack.is(DwmgItems.MAGICAL_GEL_BOTTLE.get());
	}

	@Override
	public boolean isModifierItem(ItemStack stack) {
		return stack.is(Items.SLIME_BALL);
	}

	@Override
	public ItemStack getResult(ItemStack subject, ItemStack modifier) {
		return DwmgItems.MAGICAL_GEL_BALL.get().getDefaultInstance();
	}

	@Override
	public ItemStack getSubjectRemaining(ItemStack subject, ItemStack modifier) {
		if (DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) == 1)
			return DwmgItems.EMPTY_MAGICAL_GEL_BOTTLE.get().getDefaultInstance();
		else
		{
			ItemStack res = subject.copy();
			DwmgItems.MAGICAL_GEL_BOTTLE.get().setAmount(res, DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) - 1);
			return res;
		}
	}

}
