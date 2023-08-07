package net.sodiumstudio.dwmg.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.sodiumstudio.dwmg.registries.DwmgItems;
import net.sodiumstudio.dwmg.registries.DwmgRecipes;

public class MagicalGelSeperateRecipe extends SimpleModificationRecipe
{

	public MagicalGelSeperateRecipe(ResourceLocation pId)
	{
		super(pId);
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return DwmgRecipes.MAGICAL_GEL_SEPERATE.get();
	}

	@Override
	public boolean isSubjectItem(ItemStack stack) {
		return stack.is(DwmgItems.MAGICAL_GEL_BOTTLE.get()) && DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(stack) > 1;
	}

	@Override
	public boolean isModifierItem(ItemStack stack) {
		return stack.is(DwmgItems.EMPTY_MAGICAL_GEL_BOTTLE.get());
	}

	@Override
	public ItemStack getResult(ItemStack subject, ItemStack modifier) {
		ItemStack res = subject.copy();
		DwmgItems.MAGICAL_GEL_BOTTLE.get().setAmount(res, DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) / 2);
		return res;
	}

	@Override
	public ItemStack getSubjectRemaining(ItemStack subject, ItemStack modifier)
	{
		ItemStack res = subject.copy();
		DwmgItems.MAGICAL_GEL_BOTTLE.get().setAmount(res, DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) - DwmgItems.MAGICAL_GEL_BOTTLE.get().getAmount(subject) / 2);
		return res;
	}
}
