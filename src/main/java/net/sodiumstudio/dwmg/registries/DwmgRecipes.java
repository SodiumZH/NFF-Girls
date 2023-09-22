package net.sodiumstudio.dwmg.registries;

import net.minecraft.core.Registry;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleRecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.sodiumstudio.dwmg.Dwmg;
import net.sodiumstudio.dwmg.recipes.MagicalGelEnchantRecipe;
import net.sodiumstudio.dwmg.recipes.MagicalGelSeperateRecipe;
import net.sodiumstudio.dwmg.recipes.MagicalGelStainRecipe;

public class DwmgRecipes
{
	public static final DeferredRegister<RecipeSerializer<?>> RECIPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, Dwmg.MOD_ID);
	
	public static final RegistryObject<RecipeSerializer<?>> MAGICAL_GEL_STAIN = RECIPES.register("magical_gel_stain", () -> new SimpleRecipeSerializer<>(MagicalGelStainRecipe::new));
	public static final RegistryObject<RecipeSerializer<?>> MAGICAL_GEL_SEPERATE = RECIPES.register("magical_gel_seperate", () -> new SimpleRecipeSerializer<>(MagicalGelSeperateRecipe::new));
	public static final RegistryObject<RecipeSerializer<?>> MAGICAL_GEL_ENCHANT = RECIPES.register("magical_gel_enchant", () -> new SimpleRecipeSerializer<>(MagicalGelEnchantRecipe::new));
}
