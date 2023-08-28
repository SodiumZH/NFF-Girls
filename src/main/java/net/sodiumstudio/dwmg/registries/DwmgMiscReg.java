package net.sodiumstudio.dwmg.registries;

import java.util.HashMap;

import com.github.mechalopa.hmag.registry.ModItems;

import net.minecraft.world.item.Item;
import net.sodiumstudio.nautils.ContainerHelper;
import net.sodiumstudio.nautils.containers.MapPair;

public class DwmgMiscReg
{

	// Healing items presets
	public static final HashMap<Item, Float> HEALING_ITEMS_UNDEAD = ContainerHelper.mapOf(
			MapPair.of(ModItems.SOUL_POWDER.get(), 5f),
			MapPair.of(ModItems.SOUL_APPLE.get(), 15f));
	
}
