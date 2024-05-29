package net.sodiumstudio.dwmg.entities.handlers.hmag;

import java.util.Map;

import java.util.function.Supplier;

import net.minecraft.world.item.Items;
import net.sodiumstudio.nautils.NaContainerUtils;
import net.sodiumstudio.nautils.containers.MapPair;
import net.sodiumstudio.nautils.math.RandomSelection;
import net.sodiumstudio.nautils.math.RndUtil;

public class HandlerKobold extends HandlerItemDropping
{

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Supplier<Double>> getDeltaProc() {
		return NaContainerUtils.<String, Supplier<Double>>mapOf(
				MapPair.of("minecraft:iron_ingot", () -> RndUtil.rndRangedDouble(0.02, 0.04)),
				MapPair.of("minecraft:gold_ingot", () -> RndUtil.rndRangedDouble(0.03, 0.05)),
				MapPair.of("minecraft:emerald", () -> RndUtil.rndRangedDouble(0.03, 0.05)),
				MapPair.of("minecraft:diamond", () -> RndUtil.rndRangedDouble(0.05, 0.10)),
				MapPair.of("minecraft:netherite_scrap", () -> RndUtil.rndRangedDouble(0.15, 0.25)),
				MapPair.of("minecraft:netherite_ingot", () -> RndUtil.rndRangedDouble(0.30, 0.50)),
				MapPair.of("minecraft:iron_pickaxe", () -> RndUtil.rndRangedDouble(0.05, 0.07)),
				MapPair.of("minecraft:golden_pickaxe", () -> RndUtil.rndRangedDouble(0.07, 0.10)),
				MapPair.of("minecraft:diamond_pickaxe", () -> RndUtil.rndRangedDouble(0.12, 0.18)),
				MapPair.of("minecraft:netherite_pickaxe", () -> RndUtil.rndRangedDouble(0.50, 1.00))
				);
	}

	@Override
	public int getHoldingItemTime() {
		return 5 * 20;
	}

}
